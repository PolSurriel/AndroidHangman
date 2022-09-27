package com.example.enti_di_unica_pol_surriel.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.model.DatabaseUtils
import com.example.enti_di_unica_pol_surriel.model.UserScore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel(){

    companion object{
        val logStatus : MutableLiveData<LogStatus> = MutableLiveData<LogStatus>()

        fun postUserSettings(){
            if(DatabaseUtils.logged()){
                DatabaseUtils.postUserSettings(
                    App.username.value!!,
                    App.soundActive.value!!,
                    App.notifiactionsActive.value!!
                )
            }
        }

        fun registerAnalyticsLevelStart(context: Context){
            FirebaseAnalytics.getInstance(context).logEvent("level_start", Bundle())
        }

        fun registerAnalyticsNewChance(context: Context, viewAd:Boolean){
            FirebaseAnalytics.getInstance(context).logEvent("new_chance", Bundle().apply {
                putBoolean("view_ad", viewAd)
            })

        }

        fun getScoreRanking(callback: (List<UserScore>)->Unit){
            DatabaseUtils.getScoreRanking(callback)
        }
    }


    private fun createUserSettings(){

        val email = currentUserID!!
        DatabaseUtils.createUserSettings(
            email,
            email.substring(0, email.indexOf('@')),
            App.notifiactionsActive.value!!,
            App.soundActive.value!!
        )

    }


    init {
        if(logged())
            logStatus.value = LogStatus.Logged
        else
            logStatus.value = LogStatus.Anon
    }

    private val currentUserID : String?
        get(){
            return FirebaseAuth.getInstance().currentUser?.email
        }

    enum class LogStatus{
        Anon,
        Logged
    }


    fun logged() :Boolean{
        return DatabaseUtils.logged()
    }

    fun anonLogin(){
        DatabaseUtils.anonLogin()
    }

    fun logout(){
        DatabaseUtils.logout()
        App.username.postValue("")
        logStatus.postValue(LogStatus.Anon)


    }

    fun loadUserSettings(){
        if(DatabaseUtils.logged()) {
            DatabaseUtils.loadUserSettings(currentUserID!!) {
                App.soundActive.postValue(it["soundActivated"]!!.toString().contains("true"))
                App.notifiactionsActive.postValue(
                    it["notificationsActivated"]!!.toString().contains("true")
                )
                val username = it["username"]!!.toString()
                App.username.postValue(username)
            }
        }
    }

    fun login(
        email:String,
        pass:String,
        succeedCallback: ()->Unit = {},
        failCallback: ()->Unit = {}
    ){

        DatabaseUtils.login(
            email,
            pass,
            succeedCallback= {
                loadUserSettings()
                succeedCallback()
                logStatus.postValue(LogStatus.Logged)
            },
            failCallback
        )

    }

    fun register(
        email:String,
        pass:String,
        succeedCallback: ()->Unit = {},
        failCallback: ()->Unit = {}
    ){
        DatabaseUtils.register(
            email,
            pass,
            succeedCallback = {
                createUserSettings()
                succeedCallback()
                logStatus.postValue(LogStatus.Logged)
            },
            failCallback
        )

    }

}

fun registerAnalyticsLevelShowAd(context: Context){
    FirebaseAnalytics.getInstance(context).logEvent("show_ad", Bundle())
}