package ca.kendallroth.expensesapp.utils;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.model.AuditLog;

/**
 * Audit Log utility functions
 */
public abstract class AuditLogUtils {
  /**
   * Log a user action
   * @param userId      Actor user id
   * @param action      Action code
   * @param description Short description of action
   */
  public void log(int userId, String action, String description) {
    AppDatabase db = AppDatabase.getDatabase();

    Date dateAdded = new Date();

    // Get the user's email
    String userEmail = db.userDao().getUserEmail(userId);

    // Create and add the audit log
    AuditLog auditLog = new AuditLog(0, userId, userEmail, action, description, dateAdded);
    db.auditLogDao().addLog(auditLog);

    AppDatabase.destroyInstance();
  }
}
