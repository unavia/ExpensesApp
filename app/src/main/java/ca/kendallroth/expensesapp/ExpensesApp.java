package ca.kendallroth.expensesapp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

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

    // Initialize Stetho database testing connection
    Stetho.initializeWithDefaults(this);

    // Set the static app context (to enable access from "outside" files)
    appContext = getBaseContext();
  }
}
