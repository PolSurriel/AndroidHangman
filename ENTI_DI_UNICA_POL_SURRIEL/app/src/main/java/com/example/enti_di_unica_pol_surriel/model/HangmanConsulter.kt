package com.example.enti_di_unica_pol_surriel.model

import retrofit2.Response
import retrofit2.http.*

/**
 * Hangman API Consulter. Contains all the API possible petitions.
 * */
interface HangmanConsulter {

    /**
     * Creates a new game server-size.
     *
     * @return a new game with the game token and the hangman word
     * */
    @POST("hangman")
    suspend fun createNewGame(@Body game: HangmanAPI.Game = HangmanAPI.Game(
        "",
        ""
    )
    ) : Response<HangmanAPI.Game>

    /**
     * Sends a letter to guess.
     *
     * @param token the ID of the game given by the [createNewGame] method
     * @param letter the guessed letter to check if is correct or not
     *
     * @return Response. if the letter was correct, the .correct attribute will be true.
     * the hangmanText attribute contains a string with '_' in no matched positions and '[letter]'
     * int the matched positions.
    * */
    @PUT("hangman")
    suspend fun guessLetter(@Query("token") token: String, @Query("letter") letter: String) : Response<HangmanAPI.LetterGuessResponse>

    /**
     * Gets the solution of the game.
     *
     * @param token the ID of the game given by the [createNewGame] method
     *
     * @return an object that contains the solution of the game in its .solution attribute.
     * */
    @GET("hangman")
    suspend fun getSolution(@Query("token") token: String) : Response<HangmanAPI.GameSolution>

    /**
     * Gets a clue (one of the not guessed letters).
     *
     * @param token the ID of the game given by the [createNewGame] method
     *
     * @return the response with one of the not guessed letters
     * */
    @GET("hangman")
    suspend fun getHint(@Query("token") token: String) : Response<HangmanAPI.Hint>

}