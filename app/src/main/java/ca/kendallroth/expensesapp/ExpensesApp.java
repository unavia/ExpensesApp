package ca.kendallroth.expensesapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import ca.kendallroth.expensesapp.utils.AuthUtils;
import ca.kendallroth.expensesapp.utils.Response;
import ca.kendallroth.expensesapp.utils.XMLFileUtils;

/**
 * Custom Application class to handle checking for authentication file on app start.
 *   Taken from https://stackoverflow.com/questions/7360846/how-can-i-execute-something-just-once-per-application-start
 */
public class ExpensesApp extends Application {

  // Expose the app context
  private static Context appContext;

  public static Context getContext() {
    return appContext;
  }

  public ExpensesApp() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();

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

    // TODO: Do something with response
    Log.d("ExpensesApp.auth", String.format("checkAuthenticationFile response: %s", createAuthFileResponse.toString()));
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
    Log.d("ExpensesApp", authFileStatus);

    return authFileExists;
  }
}