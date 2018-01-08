package ca.kendallroth.expensesapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ca.kendallroth.expensesapp.R;

/**
 * Firebase service to receive messages (push notifications)
 */
public class AppFirebaseMessagingService extends FirebaseMessagingService {

  public AppFirebaseMessagingService() {
  }

  /**
   * Receive a Firebase message and trigger a notification
   * @param remoteMessage Firebase message
   */
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Get notification and trigger the display
    sendNotification(remoteMessage.getNotification().getBody());

    super.onMessageReceived(remoteMessage);
  }

  /**
   * Display a notification from Firebase
   * @param message Firebase notification message
   */
  private void sendNotification(String message) {
    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    // Android O requires Notification Channels
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Create notification channel for Android O clients
      NotificationChannel channel = new NotificationChannel(
          NotificationUtils.NOTIFICATION_CHANNEL_FIREBASE,
          "Firebase Notifications",
          NotificationManager.IMPORTANCE_HIGH);

      // Set channel properties and attach to notification manager
      channel.setDescription("Firebase Notifications"); // Description as shown in Notification settings
      channel.enableLights(true);
      channel.setLightColor(Color.RED);
      channel.enableVibration(true);
      notificationManager.createNotificationChannel(channel);
    }

    // Notification sound
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    // Create notification and set properties (with conditional notification channel)
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationUtils.NOTIFICATION_CHANNEL_FIREBASE);
    builder.setSmallIcon(R.drawable.ic_notification);
    builder.setContentTitle("Firebase Notification");
    builder.setContentText(message);
    builder.setAutoCancel(true);
    builder.setSound(soundUri);
    builder.setDefaults(Notification.DEFAULT_ALL);
    builder.setPriority(Notification.PRIORITY_HIGH);

    // Display the notification
    notificationManager.notify(0, builder.build());
  }
}
