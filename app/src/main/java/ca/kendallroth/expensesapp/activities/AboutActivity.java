package ca.kendallroth.expensesapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ca.kendallroth.expensesapp.R;

/**
 * About activity with child fragment to display the About screen.
 */
public class AboutActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);

    // Toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("About");

    setSupportActionBar(toolbar);
  }
}
