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
public class Category extends BaseModel {

  @ColumnInfo(name = "id")
  @PrimaryKey(autoGenerate = true)
  public final int id;

  @ColumnInfo(name = "user_id")
  public final int userId;

  @ColumnInfo(name = "name")
  public String name;

  @ColumnInfo(name = "description")
  public String description;

  @ColumnInfo(name = "colour")
  public String colour;

  @ColumnInfo(name = "icon")
  public String icon;

  public Category(
      int id, int userId, String name, String description, String colour, String icon,
      Date dateCreated, Date dateModified, boolean deleted
  ) {
    super(dateCreated, dateModified, deleted);

    this.id = id;
    this.userId = userId;
    this.name = name;
    this.description = description;
    this.colour = colour;
    this.icon = icon;
  }
}
