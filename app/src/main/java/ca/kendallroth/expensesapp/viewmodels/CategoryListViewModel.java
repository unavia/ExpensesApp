package ca.kendallroth.expensesapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ca.kendallroth.expensesapp.persistence.AppDatabase;
import ca.kendallroth.expensesapp.persistence.model.Category;

/**
 * View model for the Category List
 */
public class CategoryListViewModel extends AndroidViewModel {

  private LiveData<List<Category>> mCategoryList;

  /**
   * Create a Category List view model
   * @param application Application reference
   */
  public CategoryListViewModel(@NonNull Application application) {
    super(application);

    AppDatabase mDatabase = AppDatabase.getDatabase();

    // TODO: Change to use authenticated user id
    // Initialize the Live Data connection to the user's categories
    mCategoryList = mDatabase.categoryDao().findCategoriesForUser(1);
  }

  /**
   * Return the observable of the user's categories
   * @return Observable of user's categories
   */
  public LiveData<List<Category>> getCategoryList() {
    return mCategoryList;
  }

  @Override
  protected void onCleared() {
    AppDatabase.destroyInstance();

    super.onCleared();
  }
}
