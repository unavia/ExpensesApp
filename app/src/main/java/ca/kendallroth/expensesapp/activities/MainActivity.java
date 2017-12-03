package ca.kendallroth.expensesapp.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.fragments.CategoriesFragment;
import ca.kendallroth.expensesapp.fragments.DashboardFragment;
import ca.kendallroth.expensesapp.fragments.TransactionsFragment;

/**
 * Main activity with Dashboard and related fragments (managed by a Drawer). When a menu item is
 * clicked the Activity loads the corresponding fragment and updates the UI as necessary.
 */
public class MainActivity extends AppCompatActivity {

  private DrawerLayout mDrawerLayout;
  private NavigationView mNavigationView;
  private Toolbar mToolbar;

  // Fragment name tags (for Drawer menu item selection)
  private static final String TAG_DASHBOARD = "dashboard";
  private static final String TAG_CATEGORIES = "categories";
  private static final String TAG_TRANSACTIONS = "transactions";
  private static final String TAG_ABOUT = "about";
  private static final String TAG_SETTINGS = "settings";
  public static String CURRENT_FRAGMENT = TAG_DASHBOARD;

  // List of activity titles
  private String[] activityTitles;

  // Current nav menu item
  private int drawerItemIndex = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Initialize the UI
    initView(savedInstanceState);
  }

  /**
   * Initialize the Activity view
   * @param savedInstanceState Saved instance state
   */
  private void initView(Bundle savedInstanceState) {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);

    // Drawer components
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mNavigationView = (NavigationView) findViewById(R.id.drawer_nav_view);

    // Create the Drawer navigation component
    initNavigationView();

    // Restore an existing state if one exists
    if (savedInstanceState == null) {
      drawerItemIndex = 0;
      CURRENT_FRAGMENT = TAG_DASHBOARD;
      loadFragment();
    }
  }

  /**
   * Initialize the Drawer navigation component
   */
  private void initNavigationView() {
    // Set listener for Drawer menu item clicks
    mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return onDrawerItemSelected(item);
      }
    });

    // Set the list of activity/fragment titles accessible from the Drawer menu
    activityTitles = getResources().getStringArray(R.array.main_drawer_activity_titles);

    // Toggle the App Menu hamburger icon when the drawer is opened or closed
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mDrawerLayout.addDrawerListener(toggle);

    // Sync the initial App Menu hamburger icon state (necessary for icon to display)
    toggle.syncState();
  }

  /**
   * Handler for selecting a Drawer navigation menu item. Updates the selected fragment and any other
   * necessary UI.
   * @param item Selected menu item
   */
  private boolean onDrawerItemSelected(MenuItem item) {
    // Prepare to load the selected menu item fragment
    switch (item.getItemId()) {
      case R.id.nav_dashboard: {
        drawerItemIndex = 0;
        CURRENT_FRAGMENT = TAG_DASHBOARD;
        break;
      }
      case R.id.nav_transactions: {
        drawerItemIndex = 1;
        CURRENT_FRAGMENT = TAG_TRANSACTIONS;
        break;
      }
      case R.id.nav_categories: {
        drawerItemIndex = 2;
        CURRENT_FRAGMENT = TAG_CATEGORIES;
        break;
      }
      case R.id.nav_about: {
        mDrawerLayout.closeDrawers();
        return true;
      }
      case R.id.nav_settings: {
        mDrawerLayout.closeDrawers();

        // Start the Settings activity
        Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(settingsIntent);
        return true;
      }
      default: {
        drawerItemIndex = 0;
        CURRENT_FRAGMENT = TAG_DASHBOARD;
        break;
      }
    }

    item.setChecked(true);

    // Load the selected menu item fragment
    loadFragment();

    return true;
  }

  /**
   * Get the currently selected fragment (after a Drawer menu item is tapped)
   * @return Currently selected fragment
   */
  private Fragment getFragment() {
    switch (drawerItemIndex) {
      case 0: {
        return new DashboardFragment();
      }
      case 1: {
        return new TransactionsFragment();
      }
      case 2: {
        return new CategoriesFragment();
      }
      default: {
        return new DashboardFragment();
      }
    }
  }

  /**
   * Load the selected fragment (after a Drawer menu item is tapped)
   */
  private void loadFragment() {
    // Update the highlighted drawer item
    setSelectedDrawerItem();

    // Update the Toolbar title
    setToolbarTitle();

    // Close the drawer if the user selected the current fragment again
    if (getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT) != null) {
      mDrawerLayout.closeDrawers();
      return;
    }

    // Load the selected fragment
    Fragment fragment = getFragment();
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frame, fragment, CURRENT_FRAGMENT).commit();

    // Close the Drawer and refresh the Toolbar menu
    mDrawerLayout.closeDrawers();
    invalidateOptionsMenu();
  }

  /**
   * Highlight the currently fragment menu item in the Drawer
   */
  private void setSelectedDrawerItem() {
    mNavigationView.getMenu().getItem(drawerItemIndex).setChecked(true);
  }

  /**
   * Update the App Bar title
   */
  private void setToolbarTitle() {
    getSupportActionBar().setTitle(activityTitles[drawerItemIndex]);
  }

  /**
   * Create and inflate the App Bar menu based on the current fragment
   * @param menu Menu items
   * @return Whether menu inflated successfully
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // DashboardFragment screen menu options
    if (drawerItemIndex == 0) {
      // Inflate the action bar menu
      getMenuInflater().inflate(R.menu.toolbar_menu_dashboard, menu);
    }

    return true;
  }

  /**
   * Handler for selecting an option from the App Bar menu
   * @param item Selected menu item
   * @return Whether menu item was processed successfully
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings: {
        // Start the Settings activity
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);

        return true;
      }
      default: {
        return super.onOptionsItemSelected(item);
      }
    }
  }

  /**
   * Handler for the phone's "Back" button
   */
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

    // Close drawer on "Back" or call parent "Back"
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawers();
    } else {
      // Only move the task to the background if at DashboardFragment fragment
      if (drawerItemIndex != 0) {
        drawerItemIndex = 0;
        loadFragment();
        return;
      }

      // Move the app to the Android background rather than allowing the user to return
      //  to "Login" activity
      moveTaskToBack(true);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
