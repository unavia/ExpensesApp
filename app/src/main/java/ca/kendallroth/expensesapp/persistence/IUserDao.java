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

  @Query("SELECT * FROM user")
  public List<User> getAllUsers();

  @Query("SELECT COUNT(*) FROM user")
  public int countUsers();

  @Query("SELECT * FROM user WHERE id = :user_id LIMIT 1")
  public User getUser(long user_id);

  @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
  public User getUser(String email);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int updateUser(User user);

  @Query("DELETE FROM user WHERE email = :email")
  int hardDeleteUser(String email);

  @Query("UPDATE user SET deleted=true WHERE email = :email")
  int deleteUser(String email);

  @Query("DELETE FROM user")
  int clear();
}
