package ca.kendallroth.expensesapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.adapters.CategoryListAdapter;
import ca.kendallroth.expensesapp.persistence.model.Category;
import ca.kendallroth.expensesapp.viewmodels.CategoryListViewModel;

/**
 * Fragment to display categories and offer management options
 */
public class CategoriesFragment extends Fragment {
  private static final String ARG_USER_ID = "argUserId";

  // Fragment bundle arguments
  private int argUserId;

  private CategoryListViewModel mViewModel;
  private CategoryListAdapter mListAdapter;
  private RecyclerView mRecyclerView;

  public CategoriesFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided parameters.
   *
   * @param userId Current user id
   * @return A new instance of fragment CategoriesFragment.
   */
  public static CategoriesFragment newInstance(int userId) {
    CategoriesFragment fragment = new CategoriesFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_USER_ID, userId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      argUserId = getArguments().getInt(ARG_USER_ID);
    }

    // Initialize the UI data dependencies
    initData();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_categories, container, false);

    // Recycler view and adapter
    mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mListAdapter);

    // Inflate the layout for this fragment
    return root;
  }

  /**
   * Initialize the data and LiveData observer connection when the Fragment is loaded
   */
  public void initData() {
    mListAdapter = new CategoryListAdapter(new ArrayList<Category>());

    mViewModel = ViewModelProviders.of(this).get(CategoryListViewModel.class);
    mViewModel.getCategoryList().observe(this, new Observer<List<Category>>() {
      @Override
      public void onChanged(@Nullable List<Category> categories) {
        mListAdapter.addItems(categories);
      }
    });
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}