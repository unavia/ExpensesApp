package ca.kendallroth.expensesapp.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ca.kendallroth.expensesapp.BuildConfig;
import ca.kendallroth.expensesapp.R;
import ca.kendallroth.expensesapp.utils.PermissionsUtils;
import ca.kendallroth.expensesapp.utils.response.FileDownloadResponse;
import ca.kendallroth.expensesapp.utils.response.StatusCode;

/**
 * About fragment with to display the About screen and contact information.
 */
public class AboutFragment extends Fragment {

  private Button mContactEmailButton;
  private Button mContactSiteButton;
  private Button mResumeButton;
  private ProgressDialog mProgressDialog;

  private final String URL_RESUME = "https://unavia.ca/resume.pdf";

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
    mResumeButton = (Button) view.findViewById(R.id.button_download_resume);

    mProgressDialog = new ProgressDialog(getActivity());
    mProgressDialog.setMessage("Downloading file...");
    mProgressDialog.setIndeterminate(false);
    mProgressDialog.setMax(100);

    // Set click listeners
    mContactEmailButton.setOnClickListener(v -> startContactEmailIntent());
    mContactSiteButton.setOnClickListener(v -> startContactSiteIntent());
    mResumeButton.setOnClickListener(v -> startResumeDownload());
  }

  /**
   * Respond to requests for user-granted permissions
   * @param requestCode  Permission request code
   * @param permissions  List of requested permissions
   * @param grantResults List of permission request grants
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      // Resume download on About page
      case PermissionsUtils.DOWNLOAD_RESUME_REQUEST_PERMISSIONS: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          // Permission granted to download file
          new DownloadFile().execute();
        } else {
          // Permission denied to download file, show an explanation
          new AlertDialog.Builder(getActivity())
              .setMessage(R.string.dialog_permission_denied_download_resume_text)
              .setTitle(R.string.dialog_permission_denied_download_resume_title)
              .setPositiveButton(R.string.ok, (dialog, id) -> {}) // Simple close dialog
              .create()
              .show();
        }
        break;
      }
    }
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

  /**
   * Download and display resume
   */
  private void startResumeDownload() {
    // Determine if app has permissions to save files to downloads
    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      // Request permissions to download the file and save to "external" storage
      requestPermissions(
          new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
          PermissionsUtils.DOWNLOAD_RESUME_REQUEST_PERMISSIONS);

      return;
    }

    // Download file if permission has been granted already
    new DownloadFile().execute();
  }


  /**
   * Download class to simply download my resume from the about page
   */
  private class DownloadFile extends AsyncTask<Void, Integer, FileDownloadResponse> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      // Show the download progress dialog
      mProgressDialog.show();
    }

    @Override
    protected FileDownloadResponse doInBackground(Void... params) {
      int byteCount;

      try {
        URL url = new URL(URL_RESUME);
        URLConnection connection = url.openConnection();
        connection.connect();

        // Get file length
        int lengthOfFile = connection.getContentLength();

        InputStream input = new BufferedInputStream(url.openStream(), 8192);

        // Output stream to downloads folder
        String downloadsFolderPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String filePath = String.format("%s/kendall_resume.pdf", downloadsFolderPath);
        OutputStream output = new FileOutputStream(filePath);

        byte data[] = new byte[1024];
        long total = 0;

        // Read file until fully downloaded
        while ((byteCount = input.read(data)) != -1) {
          total += byteCount;

          // Update progress (for dialog)
          publishProgress((int)((total * 100) / lengthOfFile));

          // Write data to file
          output.write(data, 0, byteCount);
        }

        // Flush and close streams
        output.flush();
        input.close();
        output.close();
      } catch (Exception e) {
        Log.e("ExpensesApp", e.getMessage());

        return new FileDownloadResponse(StatusCode.ERROR, "download_resume_failure", null);
      }

      return new FileDownloadResponse(StatusCode.SUCCESS, "download_resume_success", null);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
      // Update the progress dialog
      mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(FileDownloadResponse response) {
      mProgressDialog.dismiss();

      // Need to use the android "content" layout as the snackbar anchor (since this is a fragment)
      View snackbarRoot = getActivity().findViewById(android.R.id.content);

      // Define a snackbar based on the operation status
      CharSequence snackbarResource = response.getStatusCode().equals(StatusCode.SUCCESS)
          ? getString(R.string.success_download_resume)
          : getString(R.string.failure_download_resume);
      Snackbar resultSnackbar = Snackbar.make(snackbarRoot, snackbarResource, Snackbar.LENGTH_SHORT);
      resultSnackbar.show();

      // Only display downloaded PDF if download was successful
      if (response.getStatusCode() != StatusCode.SUCCESS) {
        return;
      }

      // Get downloaded file
      File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/kendall_resume.pdf");
      Uri filePath = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", downloadedFile);

      // Open PDF with an intent
      Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
      pdfIntent.setDataAndType(filePath, "application/pdf");
      pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

      try {
        startActivity(pdfIntent);
      } catch (Exception e) {
        Snackbar.make(snackbarRoot, "No application found for PDF", Snackbar.LENGTH_SHORT).show();
        Log.e("ExpensesApp", "view_resume_succeeded");
      }
    }
  }
}
