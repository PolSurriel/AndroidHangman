package com.example.enti_di_unica_pol_surriel.model

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
Static class that exposes a ready-to-use HangmanConsulter.

Uses Retrofit v2

**/
class HangmanAPI{

    companion object{

        private val baseEndpoint:String get() { return "https://hangman-api.herokuapp.com/"}

        private var m_retrofit : Retrofit? = null
        private var m_consulter : HangmanConsulter? = null

        val consulter : HangmanConsulter get() {

            if(m_retrofit == null){
                m_retrofit = Retrofit.Builder()
                    .baseUrl(baseEndpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            if(m_consulter == null){

                m_consulter = m_retrofit!!
                    .create(HangmanConsulter::class.java)

            }

            return m_consulter!!

        }

    }

    data class Game(
        @SerializedName("token") val token: String,
        @SerializedName("hangman") val hangmanText: String
    )

    data class GameSolution(
        @SerializedName("token") val token: String,
        @SerializedName("solution") val solution: String
    )

    data class Hint(
        @SerializedName("token") val token: String,
        @SerializedName("hint") val hint: String
    )

    data class LetterGuessResponse(
        @SerializedName("token") val token: String,
        @SerializedName("hangman") val hangmanText: String,
        @SerializedName("correct") val correct: Boolean

    )





}