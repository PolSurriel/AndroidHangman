package com.example.enti_di_unica_pol_surriel.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

/**
* Static class that brings a function set to interact with the app's DB services.
*
* Uses:
*  - Firebase Firestore
*  - Firebase Realtime database
*
* */
class DatabaseUtils {
    companion object {


        private const val NOTIFICATIONS_ACTIVE_PREF_ID = "notificationsActivated"
        private const val SOUND_ACTIVE_PREF_ID = "soundActivated"
        private const val USERNAME_PREF_ID = "username"
        private const val USERS_COLLECTION_ID = "users"
        private const val SCORE_NODE_ID = "scores"

        /**
         * Used handle name changes. Allows the user to skip passing by parameter
         * the previous username in memory when calling DatabaseUtils.changeUsername()
         * */
        private var previousUsername : String = ""

        /**
         * Updates all existing user config.
         *
         * If the user or some field do not exists, creates it.
         *
         * REQUIRES the user to be logged. If the user is not logged the function will do nothing.
         *
         * @param username the user's new username
         * @param soundActive the user's preference reproducing sounds in the app.
         * @param notificationsActive the user's preference about receiving notifications
         * */
        fun postUserSettings(username:String, soundActive:Boolean, notificationsActive:Boolean) {
            try {
                val email = FirebaseAuth.getInstance().currentUser!!.email!!
                val db = FirebaseFirestore.getInstance()
                db.collection(USERS_COLLECTION_ID).document(email).update(
                    mapOf(
                        NOTIFICATIONS_ACTIVE_PREF_ID to notificationsActive,
                        SOUND_ACTIVE_PREF_ID to soundActive,
                        USERNAME_PREF_ID to username
                    )
                ).addOnSuccessListener {
                    val root = FirebaseDatabase.getInstance().reference

                    root
                        .child(SCORE_NODE_ID)
                        .child(emailToRealtimeDatabaseID(email))
                        .get().addOnSuccessListener {
                            if(it != null){
                                val lastScore = it.getValue(UserScore::class.java)
                                if (lastScore != null){
                                    root
                                        .child(SCORE_NODE_ID)
                                        .child(emailToRealtimeDatabaseID(email))
                                        .setValue(UserScore(lastScore.score, username))
                                }

                            }
                        }

                }
            }catch (e: Exception) {
                Log.e("POST_USER_SETTINGS", e.message.toString())
            }

        }

        /***
         * Gets the existing registered scores in the db ORDERED descending by score punctuation.
         *
         * Executed from other thread.
         *
         * @param callback the code that will be executed once the request was successful
         */
        fun getScoreRanking(callback: (List<UserScore>)->Unit){
            val root = FirebaseDatabase.getInstance().reference

            root.child(SCORE_NODE_ID).get().addOnSuccessListener {

                val result : MutableList<UserScore> = mutableListOf()
                for(data in it.children){
                    result.add(data.getValue(UserScore::class.java)!!)
                }
                result.sortByDescending { userScore ->
                    userScore.score
                }
                callback(result)
            }

        }

        /**
         * Creates a user with the given user preferences.
         *
         * @param username the user's new username
         * @param soundActive the user's preference reproducing sounds in the app.
         * @param notificationsActive the user's preference about receiving notifications
         * */
        fun createUserSettings(
            email:String,
            username : String,
            notificationsActive:
            Boolean, soundActive: Boolean
        ){

            FirebaseFirestore.getInstance().collection(USERS_COLLECTION_ID).document(email).set(
                mapOf(
                    USERNAME_PREF_ID to username,
                    NOTIFICATIONS_ACTIVE_PREF_ID to notificationsActive,
                    SOUND_ACTIVE_PREF_ID to soundActive,
                )
            )

        }

        /**
         * Tells if the user is logged.
         *
         * @return True: Logged and NOT anonymously logged / False: Not Logged OR
         * anonymously logged
         */
        fun logged() :Boolean{
            return FirebaseAuth.getInstance().currentUser != null
                        && !FirebaseAuth.getInstance().currentUser?.isAnonymous!!
        }

        /**
         * Anonymously logs the user.
         * */
        fun anonLogin(){
            if(FirebaseAuth.getInstance().currentUser == null)
                FirebaseAuth.getInstance().signInAnonymously()
        }

        /**
         * Logs aut
         * */
        fun logout(){
            FirebaseAuth.getInstance().signOut()
        }

        /**
         * Loads all user's settings.
         *
         * @param callback the code that will be executed once the request was successful
         * */
        fun loadUserSettings(email:String, callback: (Map<String, Any>) -> Unit = {}){
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION_ID).document(email).get()
                .addOnSuccessListener {
                    previousUsername = it.data!![USERNAME_PREF_ID] as String
                    callback(it.data!!)
                }
        }

        /**
         * Logs the user in with the required email-pass auth information.
         *
         * @param email the ID of the user.
         * @param pass the password of the user.
         * @param succeedCallback the code that will be executed once the request was successful
         * @param failCallback the code that will be executed once the request was failed
         *
         * */
        fun login(
            email:String,
            pass:String,
            succeedCallback: ()->Unit = {},
            failCallback: ()->Unit = {}
        ){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        succeedCallback()

                    }else{
                        failCallback()
                    }
                }
        }

        /**
         * Registers the user AND LOGS IN in with the required email-pass auth information.
         *
         * @param email the ID of the user.
         * @param pass the password of the user.
         * @param succeedCallback the code that will be executed once the request was successful
         * @param failCallback the code that will be executed once the request was failed
         *
         * */
        fun register(
            email:String,
            pass:String,
            succeedCallback: ()->Unit = {},
            failCallback: ()->Unit = {}
        ){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        succeedCallback()

                    }else {
                        failCallback()
                    }
                }
        }

        /**
         * Converts an email string to a valid user ID to Firebase Realtime Database.
         *
         * Realtime Database does not allow characters such as '@' nor '.'. They are replaced by
         * the words 'arr' and 'dot' respectively
         *
         * @param email the ID of the user.
         *
         * @return the parsed email to a Firebase Realtime Database valid ID
         * */
        private fun emailToRealtimeDatabaseID(email: String):String{
            return email.replace("@","arr").replace(".","dot")
        }

        /**
         * Publish the score to the database ONLY if the given score is greater than the previous.
         * If there is no previous score the given will be published.
         *
         * @param email the user ID
         * @param score the score to publish
         * @param username a username associated with the score (published too)
         *
         * */
        fun postScore(email: String, score:Int, username: String){

            val root = FirebaseDatabase.getInstance().reference

            val dbUserID = emailToRealtimeDatabaseID(email)

            root.child(SCORE_NODE_ID).child(dbUserID).get().addOnSuccessListener {
                val lastScore = it.getValue(UserScore::class.java)
                if(lastScore == null || score > lastScore.score){
                    root.child(SCORE_NODE_ID).child(dbUserID).setValue(UserScore(score, username))
                }

            }

        }



    }
}