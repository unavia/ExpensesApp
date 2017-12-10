package ca.kendallroth.expensesapp.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import ca.kendallroth.expensesapp.persistence.DateConverter;

public class BaseModel {

  @ColumnInfo(name = "date_created")
  @TypeConverters({ DateConverter.class })
  public Date dateCreated;

  @ColumnInfo(name = "date_modified")
  @TypeConverters({ DateConverter.class })
  public Date dateModified;

  @ColumnInfo(name = "deleted")
  public boolean deleted;

  public BaseModel(Date dateCreated, Date dateModified, boolean deleted) {
    this.dateCreated = dateCreated;
    this.dateModified = dateModified;
    this.deleted = deleted;
  }
}
