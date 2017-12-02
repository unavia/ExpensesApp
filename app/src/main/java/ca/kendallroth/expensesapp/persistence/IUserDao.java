package ca.kendallroth.expensesapp.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IUserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long addUser(User user);

  @Query("select * from user")
  public List<User> getAllUser();

  @Query("select * from user where id = :user_id")
  public List<User> getUser(long user_id);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int updateUser(User user);

  @Query("delete from user")
  int removeAllUsers();
}
