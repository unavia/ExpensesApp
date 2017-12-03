package ca.kendallroth.expensesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import ca.kendallroth.expensesapp.fragments.LoginFragment;
import ca.kendallroth.expensesapp.fragments.LoginFragment.ILoginAttemptListener;
import ca.kendallroth.expensesapp.fragments.RegisterFragment;
import ca.kendallroth.expensesapp.fragments.RegisterFragment.IAccountCreateListener;
import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.components.ContentSwitcher;
import ca.kendallroth.expensesapp.adapters.AccountTabAdapter;

/**
 * Authorization activity that displays Login and Register workflows in a ViewPager
 */
public class AuthActivity extends AppCompatActivity implements IAccountCreateListener, ILoginAttemptListener {

  private ArrayList<Fragment> mAdapterFragments;
  private AccountTabAdapter mTabAdapter;
  private ContentSwitcher mContentSwitcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    // Instantiate fragments and add to fragment list (never removed/destroyed)
    Fragment loginFragment = LoginFragment.newInstance("Login");
    Fragment registerFragment = RegisterFragment.newInstance("Register");

    mAdapterFragments = new ArrayList<Fragment>();
    mAdapterFragments.add(loginFragment);
    mAdapterFragments.add(registerFragment);

    // Create the Account ContentSwitcher (for switching between "Login" and "Register" screens)
    mContentSwitcher = (ContentSwitcher) findViewById(R.id.content_switcher);
    mTabAdapter = new AccountTabAdapter(getSupportFragmentManager(), mAdapterFragments);
    mContentSwitcher.setPagerAdapter(mTabAdapter);
    //mContentSwitcher.setPageChangeListener(mTabChangeListener);
  }

  /**
   * Callback from the LoginFragment after a login attempt
   * @param success Whether login attempt was successful
   */
  @Override
  public void onLoginAttempt(boolean success) {
    // Only redirect to the home page when the login is successful
    if (!success) {
      return;
    }

    // Set navigation history (main) and start the Main activity
    Intent mainActivityIntent = new Intent(this, MainActivity.class);
    mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
    startActivity(mainActivityIntent);
    finish();
  }

  /**
   * Callback from the RegisterFragment after an account has been created
   * @param email    Account email
   * @param password Account password
   */
  @Override
  public void onAccountCreateRequest(String email, String password) {
    // TODO: Do something here with account creation

    Log.d("ExpensesApp", String.format("onAccountCreateRequest for '%s' with credentials '%s'", email, password));

    // Set to the login page fragment
    mContentSwitcher.setPage(0);

    // Clear the login page fragment and prefill with created account information
    Fragment fragment = mAdapterFragments.get(0);
    LoginFragment loginFragment = (LoginFragment) fragment;
    loginFragment.clearInputs();
    loginFragment.scrollToTop(false);
    loginFragment.prefillLogin(email);
  }
}
