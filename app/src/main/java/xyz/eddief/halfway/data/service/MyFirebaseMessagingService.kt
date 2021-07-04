package xyz.eddief.halfway.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.eddief.halfway.R
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.repository.UserRepository
import xyz.eddief.halfway.ui.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")

        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val locationObject = convertDataToLocationObject(remoteMessage.data)
            updateUserData(locationObject)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            sendNotification(it.body ?: "NO BODY")//TODO
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

    }

    private fun convertDataToLocationObject(data: Map<String, String>) = LocationObject(
        latitude = data.getValue("latitude").toDouble(),
        longitude = data.getValue("longitude").toDouble(),
        title = data.getValue("title"),
        address = data.getValue("address")
    )

    private fun updateUserData(locationObject: LocationObject) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                userRepository.updateUserLocation(locationObject)
            } catch (e: Exception) {
                Log.d(TAG, "updateUserData exception: $e")
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }


    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_public_black_48dp)
            .setColor(this.getColor(R.color.quantum_googgreen))
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "HalfWayMsg"
        private const val CHANNEL_NAME = "halfway notifications"
    }

}