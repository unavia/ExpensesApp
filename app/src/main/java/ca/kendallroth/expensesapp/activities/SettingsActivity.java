package ca.kendallroth.expensesapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.fragments.SettingsFragment;

/**
 * Settings activity that displays the Preferences fragment
 */
public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    // Toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Settings");

    setSupportActionBar(toolbar);

    // Display the Preferences fragment as the main content.
    getFragmentManager().beginTransaction()
        // .replace(android.R.id.content, new SettingsFragment())
        .replace(R.id.settings_content, new SettingsFragment())
        .commit();
  }
}
