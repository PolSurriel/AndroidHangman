package com.example.enti_di_unica_pol_surriel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.model.DatabaseUtils
import com.example.enti_di_unica_pol_surriel.model.HangmanAPI
import com.example.enti_di_unica_pol_surriel.model.KeyboardLetter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HangmanViewModel : ViewModel() {

    val keyboardLetters : MutableList<KeyboardLetter> = mutableListOf()
    var triedLetters : String = ""
    var failsCount = 0
    var score = 0
    var adChanceActive = true
    private var game: HangmanAPI.Game? = null

    fun gameRunning() : Boolean{
        return game != null
    }

    fun endGame(){
        guessedWord.value = null
        game = null
    }



    companion object {
        var guessedWord: MutableLiveData<String?> = MutableLiveData<String?>()

        fun postScore(score:Int){

            var username = "anon"
            if(App.username.value != null)
                username = App.username.value!!

            val email = FirebaseAuth.getInstance().currentUser!!.email!!

            DatabaseUtils.postScore(email, score, username)
        }
    }

    fun updateScore(){
        val word = guessedWord.value!!

        val vocals = listOf('a','e','i','o','u')
        var newScore = 0
        val evaluated: MutableSet<Char> = mutableSetOf()
        for(l in word){
            if(evaluated.contains(l))
                continue

            if(l in vocals){
                newScore += 5
            }else if(l in 'a'..'z'){
                newScore +=10
            }
            evaluated.add(l)
        }

        score = newScore

    }


    private fun updateWord(hangman : String){

        var word = hangman
        if(guessedWord.value != null){
            word = guessedWord.value!!
        }

        var idx = 0
        for(letter in hangman){
            if(letter in 'a'..'z'){
                word = word.substring(0, idx) + letter + word.substring(idx+1)
            }
            idx++
        }

        guessedWord.postValue(word)

    }

    fun guessLetter(letter: String, succeedCallback : (Boolean)->Unit = {}, failCallback : ()->Unit = {}){
        CoroutineScope(Dispatchers.IO).launch {
            val response = HangmanAPI.consulter.guessLetter(game!!.token, letter)

            if(response.isSuccessful){
                updateWord(response.body()!!.hangmanText)
                succeedCallback(response.body()!!.correct)
            }else {
                failCallback()
            }
        }
    }

    fun createNewGame(succeedCallback : ()->Unit = {}, failCallback : ()->Unit = {}){
        CoroutineScope(Dispatchers.IO).launch {
            val response = HangmanAPI.consulter.createNewGame()

            if(response.isSuccessful){
                game = response.body()!!
                updateWord(response.body()!!.hangmanText)

                succeedCallback()
            }else {
                failCallback()
            }

        }
    }



}