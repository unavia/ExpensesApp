package ca.kendallroth.expensesapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.persistence.model.Category;

/**
 * Recycle view list adapter for Categories
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.RecyclerViewHolder> {

  private List<Category> mCategoryList;

  public CategoryListAdapter(List<Category> categoryList) {
    this.mCategoryList = categoryList;
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new RecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
  }

  @Override
  public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
    Category category = mCategoryList.get(position);
    holder.mCategoryNameTextView.setText(category.name);
  }

  @Override
  public int getItemCount() {
    return mCategoryList.size();
  }

  /**
   * Update the category list items
   * @param categoryList Updated category items
   */
  public void addItems(List<Category> categoryList) {
    this.mCategoryList = categoryList;

    notifyDataSetChanged();
  }

  /**
   * View holder for each recycler item
   */
  static class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView mCategoryNameTextView;

    RecyclerViewHolder(View view) {
      super(view);

      mCategoryNameTextView = (TextView) view.findViewById(R.id.category_name);
    }
  }
}
