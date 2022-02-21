package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private val downloadManager: DownloadManager by lazy {
        applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var downloadChoice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            onRadioButtonChoice()?.let {
                download(it)
                custom_button.setState(ButtonState.Loading)
            }
        }

        createChannel(
            getString(R.string.not_channel_id),
            getString(R.string.not_chanel_name)
        )

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                DownloadManager.Query().setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 1))
                val cursor: Cursor = downloadManager.query(DownloadManager.Query())
                if (cursor.moveToFirst()) {
                    val description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        custom_button.setState(ButtonState.Completed)
                        notificationManager.sendNotification(context!!, true, description)
                    } else {
                        custom_button.setState(ButtonState.Completed)
                        notificationManager.sendNotification(context!!, false, description)
                    }
                }
            }
        }

    }

    private fun onRadioButtonChoice(): String? {

        when (radioGroup.checkedRadioButtonId) {
            R.id.radioButtonGlide -> {
                downloadChoice = R.string.glide.toString()
                return GLIDE
            }
            R.id.radioButtonUdacity -> {
                downloadChoice = R.string.loadappstarter.toString()
                return URL
            }
            R.id.radioButtonRetrofit -> {
                downloadChoice = R.string.retrofit.toString()
                return RETROFIT
            }
            else -> Toast.makeText(this, "please make a choice", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Download"
            }

            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {
        private const val GLIDE =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT =
            "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
    }

}
