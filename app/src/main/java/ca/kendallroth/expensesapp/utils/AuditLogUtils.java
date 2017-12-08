package ca.kendallroth.expensesapp.utils;

import android.util.Log;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.model.AuditLog;

/**
 * Audit Log utility functions
 */
public abstract class AuditLogUtils {
  /**
   * Log a user action with a debug message tag
   * @param userId      Actor user id
   * @param action      Action code
   * @param description Short description of action
   * @param tag         Debugging tag
   */
  public static void log(int userId, String action, String description, String tag) {
    AppDatabase db = AppDatabase.getDatabase();

    Date dateAdded = new Date();

    // Get the user's email
    String userEmail = db.userDao().getUserEmail(userId);

    // Create and add the audit log
    AuditLog auditLog = new AuditLog(0, userId, userEmail, action, description, dateAdded);
    db.auditLogDao().addLog(auditLog);

    // Print the log to the console (adding tag if provided)
    if (tag.isEmpty()) {
      Log.d("ExpensesApp", description);
    } else {
      Log.d(String.format("ExpensesApp.%s", tag), description);
    }

    AppDatabase.destroyInstance();
  }

  /**
   * Log a user action
   * @param userId      Actor user id
   * @param action      Action code
   * @param description Short description of action
   */
  public static void log(int userId, String action, String description) {
    // Call base method with no debugging tag
    log(userId, action, description, "");
  }
}
