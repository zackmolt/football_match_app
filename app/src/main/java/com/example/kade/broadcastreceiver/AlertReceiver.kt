package com.example.kade.broadcastreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kade.R
import com.example.kade.activities.MainActivity
import com.example.kade.db.database
import com.example.kade.model.FavoriteMatch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class AlertReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "MatchNotifier"
    }

    var favorites: MutableList<FavoriteMatch> = mutableListOf()

    override fun onReceive(p0: Context?, p1: Intent?) {
        var eventId = p1?.getIntExtra(FavoriteMatch.EVENT_ID, 0)

        p0?.database?.use {
            val result = select(FavoriteMatch.TABLE_FAV)
            val favorite = result.parseList(classParser<FavoriteMatch>())
            favorites.addAll(favorite)
        }

        for (i in 0 until favorites.size) {
            if (eventId == favorites[i].eventId) {
                notif(
                    p0,
                    favorites[i].strHome + " vs " + favorites[i].strAway,
                    "Kick Off " + favorites[i].strTime!! + ", ${favorites[i].dateEvent}"
                )
            }
        }

    }

    private fun notif(
        mContext: Context?, head: String,
        body: String
    ) {
        val mNotificationManager =
            mContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resultPendingIntent =
            PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(mContext, AlertReceiver.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_launcher)
                ).setContentTitle(head).setContentText(body)
                .setSubText("Match Will Be Started Soon")
                .setContentIntent(resultPendingIntent).setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlertReceiver.CHANNEL_ID,
                "MatchNotifierChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mBuilder.setChannelId(AlertReceiver.CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification: Notification = mBuilder.build()
        mNotificationManager.notify(0, notification)
    }
}