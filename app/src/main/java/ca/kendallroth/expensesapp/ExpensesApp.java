package ca.kendallroth.expensesapp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import ca.kendallroth.expensesapp.modules.AppComponent;
import ca.kendallroth.expensesapp.modules.AppModule;
import ca.kendallroth.expensesapp.modules.DaggerAppComponent;

/**
 * Custom Application class to handle checking for authentication file on app start.
 *   Taken from https://stackoverflow.com/questions/7360846/how-can-i-execute-something-just-once-per-application-start
 */
public class ExpensesApp extends Application {

  // Dagger 2 app context component
  private AppComponent mAppComponent;

  // Expose the app context
  private static Context appContext;

  public static Context getContext() {
    return appContext;
  }

  public AppComponent getDaggerComponent() {
    return mAppComponent;
  }

  public ExpensesApp() {
    super();
  }

  @Override
  public void onCreate() {
    super.onCreate();

    // Build the Dagger app component
    mAppComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(getApplicationContext()))
        .build();

    // Initialize Stetho database testing connection
    Stetho.initializeWithDefaults(this);

    // Set the static app context (to enable access from "outside" files)
    appContext = getBaseContext();
  }
}
