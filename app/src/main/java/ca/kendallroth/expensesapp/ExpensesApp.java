package ca.kendallroth.expensesapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.User;
import ca.kendallroth.expensesapp.utils.AuthUtils;
import ca.kendallroth.expensesapp.utils.Response;
import ca.kendallroth.expensesapp.utils.StatusCode;
import ca.kendallroth.expensesapp.utils.XMLFileUtils;

/**
 * Custom Application class to handle checking for authentication file on app start.
 *   Taken from https://stackoverflow.com/questions/7360846/how-can-i-execute-something-just-once-per-application-start
 */
public class ExpensesApp extends Application {

  // Expose the app context and database
  private static Context appContext;
  private AppDatabase mDatabase;

  public static Context getContext() {
    return appContext;
  }

  public ExpensesApp() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize Stetho database testing connection
    Stetho.initializeWithDefaults(this);

    // Set the static app context (to enable access from "outside" files)
    appContext = getBaseContext();

    // Set the Authentication file context
    AuthUtils.fileContext = getBaseContext();

    // Check for the authentication file or create if it doesn't exist
    checkAuthenticationFile();
  }

  /**
   * Create the authentication file with initial values
   */
  private void checkAuthenticationFile() {
    // Skip this step if the authentication file already exists
    if(findAuthFile()) {
      return;
    }

    // Create the authentication file
    Response createAuthFileResponse = AuthUtils.createAuthFile();
    boolean createAuthFileSuccess = createAuthFileResponse.getStatusCode() == StatusCode.SUCCESS;

    // Add the sample user to the database and authentication file
    if (!createAuthFileSuccess) {
      Response createAccountResponse = AuthUtils.addAuthUser("kendall@example.com", "Kendall Roth", "password");

      // Only add to database if authentication file insert succeeded
      if (createAccountResponse.getStatusCode().equals(StatusCode.SUCCESS)) {
        mDatabase = AppDatabase.getDatabase(getApplicationContext());

        Date currentDate = new Date();
        User newUser = new User(0, "kendall@example.com", null, "Kendall Roth", true, currentDate, currentDate, null, false);
        long newRowId = mDatabase.userDao().addUser(newUser);

        // TODO: Handle database failures

        AppDatabase.destroyInstance();
      }
    }

    // TODO: Do something with response
  }

  /**
   * Determine whether the authentication file already exists
   * @return Whether the authentication file already exists
   */
  private boolean findAuthFile() {
    // Find the authentication file in internal storage
    boolean authFileExists = getBaseContext().getFileStreamPath(XMLFileUtils.USERS_FILE_NAME).exists();

    String authFileStatus = authFileExists
        ? "Authentication file already exists"
        : "Authentication file doesn't exist";
    Log.d("MileageApp", authFileStatus);

    return authFileExists;
  }
}
