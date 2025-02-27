package fr.isen.boussougou.isensmartcompanion.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // This class is no longer needed as we're using WorkManager for notifications
    }
}
