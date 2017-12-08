package ca.kendallroth.expensesapp.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ca.kendallroth.expensesapp.persistence.model.AuditLog;

@Dao
public interface IAuditLogDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long addLog(AuditLog category);

  @Query("SELECT * FROM audit_log WHERE user_id=:user_id")
  List<AuditLog> findLogsForUser(int user_id);

  @Query("SELECT * FROM audit_log WHERE \"action\"=:action")
  List<AuditLog> findLogsForAction(String action);

  @Query("DELETE FROM audit_log")
  int clear();
}
