package com.example.enti_di_unica_pol_surriel

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.MobileAds
import java.util.*

class App : Application() {

    companion object{
        var soundActive = MutableLiveData<Boolean>()
        var username = MutableLiveData<String>()
        var notifiactionsActive = MutableLiveData<Boolean>()

        const val LAST_LAUNCH_PREF_ID = "LAST_LAUNCH_DATE_MS"
        const val NOTIFICATIONS_ALLOWED_PREF_ID = "notificationsAllowed"
    }

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this)

        sharedPreferences.edit()
            .putLong(LAST_LAUNCH_PREF_ID, Date().time).apply()


        soundActive.value = true
        notifiactionsActive.value = true

        MobileAds.initialize(this)
    }

}