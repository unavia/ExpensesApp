package ca.kendallroth.expensesapp.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

@Entity(
    tableName = "user"
)
public class User extends BaseModel {

  @ColumnInfo(name = "id")
  @PrimaryKey(autoGenerate = true)
  public final int id;

  @ColumnInfo(name = "email")
  public String email;

  @ColumnInfo(name = "username")
  public String username;

  @ColumnInfo(name = "name")
  public String name;

  @ColumnInfo(name = "active")
  public boolean active;

  @ColumnInfo(name = "date_activated")
  @TypeConverters({ DateConverter.class })
  public Date dateActivated;

  public User(
      int id, String email, String username, String name, boolean active, Date dateActivated,
      Date dateCreated, Date dateModified, boolean deleted
  ) {
    super(dateCreated, dateModified, deleted);

    this.id = id;
    this.email = email;
    this.username = username;
    this.name = name;
    this.active = active;
    this.dateActivated = dateActivated;
  }
}
