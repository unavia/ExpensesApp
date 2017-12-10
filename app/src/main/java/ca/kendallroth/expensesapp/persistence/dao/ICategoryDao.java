package ca.kendallroth.expensesapp.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ca.kendallroth.expensesapp.persistence.model.Category;


@Dao
public interface ICategoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long addCategory(Category category);

  @Query("SELECT * FROM category WHERE user_id=:user_id")
  LiveData<List<Category>> findCategoriesForUser(int user_id);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int updateCategory(Category category);

  @Query("DELETE FROM category WHERE id = :id")
  int deleteCategory(long id);

  @Query("DELETE FROM user")
  int clear();
}
