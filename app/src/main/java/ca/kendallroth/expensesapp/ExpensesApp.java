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
    // Skip this step if the authentication file already exists and is not empty
    if(AuthUtils.findAuthFile() && AuthUtils.getAuthUserCount() > 0) {
      return;
    }

    boolean createAuthFileSuccess = false;

    // Create the authentication file only if necessary (normally will be empty after a reset)
    if (!AuthUtils.findAuthFile()) {
      Response createAuthFileResponse = AuthUtils.createAuthFile();
      createAuthFileSuccess = createAuthFileResponse.getStatusCode() == StatusCode.SUCCESS;
    }

    String sampleUserEmail = "kendall@example.com";
    String sampleUserPassword ="hello";

    // DEBUG: This is only for debugging purposes

    // Add the sample user to the database and authentication file
    if (createAuthFileSuccess) {
      Response createAccountResponse = AuthUtils.addAuthUser(sampleUserEmail, "Kendall Roth", sampleUserPassword);

      // Only add to database if authentication file insert succeeded
      if (createAccountResponse.getStatusCode().equals(StatusCode.SUCCESS)) {
        mDatabase = AppDatabase.getDatabase(getApplicationContext());

        Date currentDate = new Date();
        User newUser = new User(0, sampleUserEmail, null, "Kendall Roth", true, currentDate, currentDate, null, false);
        long newRowId = mDatabase.userDao().addUser(newUser);

        // Handle database insert failures
        if (newRowId <= 0) {
          AuthUtils.removeAuthUser(sampleUserEmail, sampleUserPassword);
        }

        Log.d("MileageApp", "Database test account creation failed. All changes rolled back.");

        AppDatabase.destroyInstance();
      }
    }

    // TODO: Do something with response
  }
}
