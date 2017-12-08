package ca.kendallroth.expensesapp.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            parentColumns = "id",
            childColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        )
    },
    tableName = "category"
)
public class AuditLog {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public final int id;

    @ColumnInfo(name = "user_id")
    public final int userId;

    @ColumnInfo(name = "user_email")
    public String userEmail;

    @ColumnInfo(name = "action")
    public String action;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "date_created")
    public Date dateCreated;

    public AuditLog(
        int id, int userId, String userEmail, String action, String description, Date dateCreated
    ) {

      this.id = id;
      this.userId = userId;
      this.userEmail = userEmail;
      this.action = action;
      this.description = description;
      this.dateCreated = dateCreated;
    }
}
