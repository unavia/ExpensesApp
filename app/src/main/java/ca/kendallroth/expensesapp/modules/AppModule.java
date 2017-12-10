package ca.kendallroth.expensesapp.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
  private Context mContext;

  public AppModule(Context context) {
    this.mContext = context;
  }

  @Singleton
  @Provides
  public Context provideContext() {
    return mContext;
  }

  @Singleton
  @Provides
  public SharedPreferences provideSharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Singleton
  @Provides
  public AppDatabase provideAppDatabase(Context context) {
    return AppDatabase.getDatabase();
  }
}
