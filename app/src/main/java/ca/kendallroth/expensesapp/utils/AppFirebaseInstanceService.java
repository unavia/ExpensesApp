package ca.kendallroth.expensesapp.utils;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Firebase service to submit device tokens
 */
public class AppFirebaseInstanceService extends FirebaseInstanceIdService {

  /**
   * Get the current firebase token after it is refreshed
   */
  @Override
  public void onTokenRefresh() {
    // Get updated IntanceID token
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d("ExpensesApp", String.format("Refreshed token: %s", refreshedToken));

    // Enable sending messages to app or managing subscription from server
    sendRegistrationToServer(refreshedToken);
  }

  /**
   * Send token registration information in an email
   * @param token Firebase token generated after a refresh
   */
  private void sendRegistrationToServer(String token) {
    String registrationMessage = String.format("A device with token '%s' has registered for the ExpensesApp application.\n\n%s", token, token);

    // Create and prompt the user to send an email with the token information
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent
        .setType("message/rfc822")
        .putExtra(Intent.EXTRA_EMAIL , new String[]{ "kroth@unavia.ca" })
        .putExtra(Intent.EXTRA_SUBJECT, "ExpensesApp Token Registration")
        .putExtra(Intent.EXTRA_TEXT, registrationMessage);

    try {
      // Prompt user to send email with an app
      startActivity(Intent.createChooser(emailIntent, "Send Firebase token email"));

      Log.d("ExpensesApp", "email_firebase_token_succeeded");
    } catch (Exception e) {
      Log.e("ExpensesApp", "email_firebase_token_failed");
    }
  }
}
