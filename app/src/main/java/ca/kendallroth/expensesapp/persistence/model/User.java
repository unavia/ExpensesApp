package ca.kendallroth.expensesapp.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.DateConverter;

@Entity(
    tableName = "user",
    indices = {@Index(value = "email", unique = true)}
)
public class User extends BaseModel {

  @ColumnInfo(name = "id")
  @PrimaryKey(autoGenerate = true)
  public final int id;

  @ColumnInfo(name = "email")
  public String email;

  @ColumnInfo(name = "password")
  public String password;

  @ColumnInfo(name = "name")
  public String name;

  @ColumnInfo(name = "active")
  public boolean active;

  @ColumnInfo(name = "date_activated")
  @TypeConverters({ DateConverter.class })
  public Date dateActivated;

  public User(
      int id, String email, String password, String name, boolean active, Date dateActivated,
      Date dateCreated, Date dateModified, boolean deleted
  ) {
    super(dateCreated, dateModified, deleted);

    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.active = active;
    this.dateActivated = dateActivated;
  }
}
