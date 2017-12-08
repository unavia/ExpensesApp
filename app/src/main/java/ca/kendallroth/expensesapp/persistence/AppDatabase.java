package ca.kendallroth.expensesapp.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import ca.kendallroth.expensesapp.ExpensesApp;
import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.persistence.dao.ICategoryDao;
import ca.kendallroth.expensesapp.persistence.dao.IUserDao;
import ca.kendallroth.expensesapp.persistence.model.Category;
import ca.kendallroth.expensesapp.persistence.model.User;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

/**
 * Application database management class
 */
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

  /**
   * Room database callbacks to initialize database
   */
  private static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
    /**
     * Initialize database seed data upon creation
     * @param db SQLite database reference
     */
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      // DEBUG: Seed user data
      try {
        resetDatabase();
      } catch (Exception e) {
        Log.e("ExpensesApp.db", "Adding seed user to database failed");
      }

      Log.d("ExpensesApp", "Database created and populated");
    }

    /**
     * Callback for each time the database is opened
     * @param db SQLite database reference
     */
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
      Log.d("ExpensesApp.db", "Database opened");
    }
  };

  /**
   * Get a reference to the database (singleton)
   * @return Database reference
   */
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

  /**
   * Clean up the current database instance
   */
  public static void destroyInstance() {
    INSTANCE = null;
  }

  /**
   * Clear all database tables in preparation for sample data insertion
   */
  private static void clearTables() {
    SupportSQLiteDatabase db = getDatabase().mDatabase;

    db.delete("user", null, null);
    db.delete("category", null, null);
  }

  /**
   * Reset the database with sample data
   * @return Whether database reset succeeded
   */
  public static boolean resetDatabase() {
    // Access to the SQLite database is necessary to work both at setup and when rest from Settings
    SupportSQLiteDatabase db = getDatabase().mDatabase;

    // Clear the database tables
    clearTables();

    boolean didSucceed = false;
    Document document;

    try {
      // Get the database seed file
      InputStream dbSeedFile = ExpensesApp.getContext().getResources().openRawResource(R.raw.db_seed);
      SAXReader reader = new SAXReader();
      document = reader.read(dbSeedFile);

      // Root node
      Element data = (Element) document.selectSingleNode("db");

      // Users node
      Element usersNode = (Element) data.selectSingleNode("users");
      List<Node> usersList = usersNode.selectNodes("user");

      db.beginTransaction();

      // DEBUG: Insert the seed users
      for (Node user : usersList) {
        Node meta = user.selectSingleNode("meta");
        String email = user.selectSingleNode("email").getText();
        String password = user.selectSingleNode("password").getText();
        String name = user.selectSingleNode("name").getText();

        ContentValues userValues = new ContentValues();
        userValues.put("id", parseInt(meta.valueOf("@id")));
        userValues.put("email", email);
        userValues.put("password", password);
        userValues.put("name", name);
        userValues.put("active", parseBoolean(meta.valueOf("@active")));
        userValues.put("date_created", new Date().getTime());
        userValues.put("date_activated", new Date().getTime());
        userValues.put("deleted", parseBoolean(meta.valueOf("@deleted")));

        db.insert("user", OnConflictStrategy.IGNORE, userValues);

        Log.d("ExpensesApp.db", "Database 'User' table created and populated");
      }

      // Categories node
      Element categoriesNode = (Element) data.selectSingleNode("categories");
      List<Node> categoriesList = categoriesNode.selectNodes("category");

      for (Node category : categoriesList) {
        Node meta = category.selectSingleNode("meta");
        String name = category.selectSingleNode("name").getText();
        String description = category.selectSingleNode("description").getText();
        String colour = category.selectSingleNode("colour").getText();
        String icon = category.selectSingleNode("icon").getText();

        ContentValues categoryValues = new ContentValues();
        categoryValues.put("id", meta.valueOf("@id"));
        categoryValues.put("user_id", meta.valueOf("@userId"));
        categoryValues.put("name", name);
        categoryValues.put("description", description);
        categoryValues.put("colour", colour);
        categoryValues.put("icon", icon);
        categoryValues.put("date_created", new Date().getTime());
        categoryValues.put("date_modified", new Date().getTime());
        categoryValues.put("deleted", parseBoolean(meta.valueOf("@deleted")));

        db.insert("category", OnConflictStrategy.IGNORE, categoryValues);

        Log.d("ExpensesApp.db", "Database 'Category' table created and populated");
      }

      // End transaction (will commit if reached with no errors)
      db.setTransactionSuccessful();
      db.endTransaction();

      didSucceed = true;
    } catch (Exception e) {
      db.endTransaction();

      Log.e("ExpensesApp.db", e.getMessage());
    }

    return didSucceed;
  }
}
