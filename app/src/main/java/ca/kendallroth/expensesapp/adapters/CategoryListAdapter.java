package ca.kendallroth.expensesapp.adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.kendallroth.expensesapp.ExpensesApp;
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
    // Get current category
    Category category = mCategoryList.get(position);

    // TODO: Handle not found icons or no specified icons
    // TODO: Handle invalid category colours

    // Icons are dynamically attached as drawables (based on the database category icon string)
    int iconId = ExpensesApp.getContext().getResources().getIdentifier(
        String.format("ic_category_%s_grey_24dp", category.icon),
       "drawable",
        ExpensesApp.getContext().getPackageName());
    int categoryColor = Color.parseColor(category.colour);

    holder.mCategoryIcon.setImageResource(iconId);
    holder.mCategoryIcon.setColorFilter(categoryColor);
    holder.mCategoryNameLabel.setText(category.name);
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
    private ConstraintLayout mCategoryView;
    private ImageView mCategoryIcon;
    private TextView mCategoryNameLabel;

    RecyclerViewHolder(View view) {
      super(view);

      mCategoryView = view.findViewById(R.id.category_view);
      mCategoryIcon = (ImageView) view.findViewById(R.id.category_icon);
      mCategoryNameLabel = (TextView) view.findViewById(R.id.category_name);
    }
  }
}
