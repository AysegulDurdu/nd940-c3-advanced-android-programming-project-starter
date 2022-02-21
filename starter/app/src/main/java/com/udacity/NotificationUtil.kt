package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat

private const val REQUEST_CODE = 0

fun NotificationManager.sendNotification(context: Context, isSuccess: Boolean, title: String) {

    cancelAll()

    val intent = DetailActivity.getIntent(context = context, status = isSuccess, title = title)

    val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(
        context,
        "channel_id"
    )
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .addAction(R.drawable.ic_assistant_black_24dp, context.getString(R.string.notification_button), pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    notify(REQUEST_CODE, builder.build())
}