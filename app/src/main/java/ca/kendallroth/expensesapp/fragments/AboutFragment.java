package ca.kendallroth.expensesapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.kendallroth.expensesapp.R;

/**
 * About fragment with to display the About screen and contact information.
 */
public class AboutFragment extends Fragment {

  Button mContactEmailButton;
  Button mContactSiteButton;

  public AboutFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_about, container, false);

    // Initialize the UI
    initUI(view);

    return view;
  }

  /**
   * Initialize the UI
   * @param view Parent view reference
   */
  private void initUI(View view) {
    mContactEmailButton = (Button) view.findViewById(R.id.button_email);
    mContactSiteButton = (Button) view.findViewById(R.id.button_site);

    // Set click listeners
    mContactEmailButton.setOnClickListener(v -> startContactEmailIntent());
    mContactSiteButton.setOnClickListener(v -> startContactSiteIntent());
  }

  /**
   * Open an email intent to contact me
   */
  private void startContactEmailIntent() {
    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:kroth@unavia.ca"));
    startActivity(emailIntent);
  }

  /**
   * Open a web intent to view my website
   */
  private void startContactSiteIntent() {
    Intent siteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unavia.ca"));
    startActivity(siteIntent);
  }
}
