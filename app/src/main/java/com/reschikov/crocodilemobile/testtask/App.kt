package com.reschikov.crocodilemobile.testtask

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.onesignal.OneSignal
import com.reschikov.crocodilemobile.testtask.di.appModule
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private const val CHANNEL_ID_PUSH = "2"

private const val SESSION_TIMEOUT = 30

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext (this@App)
            modules(listOf(appModule))
        }
        initChannelsNotifications()
        val config = YandexMetricaConfig
            .newConfigBuilder(BuildConfig.APP_METRICA_KEY)
            .withSessionTimeout(SESSION_TIMEOUT)
            .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(false)
            .init()
    }

    private fun initChannelsNotifications(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelPush = NotificationChannel(CHANNEL_ID_PUSH, getString(R.string.notif_name_one_signals),
                NotificationManager.IMPORTANCE_DEFAULT)
            channelPush.description = getString(R.string.notif_push)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelPush)
        }
    }
}