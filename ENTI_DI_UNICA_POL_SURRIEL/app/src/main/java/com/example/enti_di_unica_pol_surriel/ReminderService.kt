package com.example.enti_di_unica_pol_surriel

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.example.enti_di_unica_pol_surriel.view.activities.MainActivity
import java.util.*

/**
 * Foreground service that reminds the user to open the app each [REMIND_FREQUENCY_IN_DAYS]
 * */
class ReminderService : Service() {

    companion object{
        private const val REMIND_FREQUENCY_IN_DAYS = 3
        private const val FOREGROUND_CHANNEL_ID = "servicechannel"
        private const val REMINDER_CHANNEL_ID = "reminderchannel"
        private const val FOREGROUND_CHANNEL_NAME = "Services"
        private const val REMINDER_CHANNEL_NAME = "Reminders"
    }

    private var lastNotificationID = 2

    /**
     * Do the foreground notification
     */
    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                FOREGROUND_CHANNEL_ID,
                FOREGROUND_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE
            )
            val pendingIntent: PendingIntent =
                Intent(this, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE)
                }


            val notification: Notification = Notification.Builder(
                this,
                FOREGROUND_CHANNEL_ID
            )
                .setSmallIcon(R.drawable.hm6)
                .setContentIntent(pendingIntent)
                .setVisibility(Notification.VISIBILITY_SECRET)
                .build()

            startForeground(1, notification)
        }
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNotificationChannel(
        channelID : String = REMINDER_CHANNEL_ID,
        channelName:String = REMINDER_CHANNEL_NAME,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText ="channel_description"
            val channel = NotificationChannel(channelID, channelName, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createNotificationChannel()
        }
        serviceLoop()

        return super.onStartCommand(intent, flags, startId)
    }



    /**
     * Sends a notification if the last time since the app was opened is greater or
     *  equal than [REMIND_FREQUENCY_IN_DAYS]
     * */
    private fun serviceLoop(){

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        Thread {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                createNotificationChannel(REMINDER_CHANNEL_ID)
            else
                return@Thread

            while (true){

                val lastTimeAppOppened = sharedPreferences.getLong(App.LAST_LAUNCH_PREF_ID,0)

                val timeDiff: Long = Date().time - lastTimeAppOppened
                val timeDiffInDays: Long = ((timeDiff/ (1000 * 60 * 60 * 24)) % 365)

                if(
                    timeDiffInDays >= 3
                    && sharedPreferences.getBoolean(App.NOTIFICATIONS_ALLOWED_PREF_ID,true)
                ){

                    sharedPreferences.edit().putLong(App.LAST_LAUNCH_PREF_ID, Date().time).apply()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val pendingIntent: PendingIntent =
                            Intent(this, MainActivity::class.java).let {
                                notificationIntent ->
                                PendingIntent.getActivity(
                                    this,
                                    0,
                                    notificationIntent,
                                    PendingIntent.FLAG_IMMUTABLE
                                )
                            }


                        val notification: Notification = Notification.Builder(
                            this,
                            REMINDER_CHANNEL_ID
                        )
                            .setSmallIcon(R.drawable.hm6)
                            .setContentIntent(pendingIntent)
                            .setContentTitle(resources.getText(R.string.reminder_title))
                            .setContentText(resources.getText(R.string.reminder_text))
                            .build()
                        val notificationManager: NotificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        notificationManager.notify(lastNotificationID++, notification)


                    }

                }

                Thread.sleep(10000)

            }
        }.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}
