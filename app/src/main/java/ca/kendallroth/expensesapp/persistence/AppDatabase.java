package ca.kendallroth.expensesapp.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.Date;

import ca.kendallroth.expensesapp.ExpensesApp;

@Database(
    entities = {
        User.class, Category.class
    },
    version = 16,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
  private static AppDatabase INSTANCE;

  public abstract IUserDao userDao();
  public abstract ICategoryDao categoryDao();

  static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
    public void onCreate(SupportSQLiteDatabase db) {
      // DEBUG: Seed user data
      try {
        ContentValues userValues = new ContentValues();
        userValues.put("id", 1);
        userValues.put("email", "kendall@example.com");
        userValues.put("password", "hello");
        userValues.put("name", "Kendall Roth");
        userValues.put("active", true);
        userValues.put("date_activated", new Date().getTime());
        userValues.put("date_created", new Date().getTime());
        // userValues.put("date_modified", null);
        userValues.put("deleted", false);

        db.insert("user", OnConflictStrategy.IGNORE, userValues);
      } catch (Exception e) {
        Log.e("ExpensesApp.db", "Adding seed user to database failed");
      }

      // TODO: Seed categories
      for (int i = 0; i < 10; i++) {
        try {
          ContentValues categoryValues = new ContentValues();
          categoryValues.put("id", i);
          categoryValues.put("user_id", 1);
          categoryValues.put("name", i);
          categoryValues.put("description", "");
          categoryValues.put("colour", "");
          categoryValues.put("icon", "");
          categoryValues.put("date_created", new Date().toString());
          categoryValues.put("date_modified", new Date().toString());
          categoryValues.put("deleted", false);

          db.insert("category", OnConflictStrategy.IGNORE, categoryValues);
        } catch(Exception e) {

        }
      }

      Log.d("ExpensesApp", "Database created and populated");
    }

    public void onOpen(SupportSQLiteDatabase db) {
      Log.d("ExpensesApp.db", "Database opened");
    }
  };

  public static AppDatabase getDatabase() {
    // TODO: Not sure what will happen if this fails
    Context context = ExpensesApp.getContext();

    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(context, AppDatabase.class, "expensesapp")
              //Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
              // To simplify the exercise, allow queries on the main thread.
              // Don't do this on a real app!
              .allowMainThreadQueries()
              // Recreate the database if necessary
              .fallbackToDestructiveMigration()
              .addCallback(rdc)
              .build();
    }
    return INSTANCE;
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }
}
