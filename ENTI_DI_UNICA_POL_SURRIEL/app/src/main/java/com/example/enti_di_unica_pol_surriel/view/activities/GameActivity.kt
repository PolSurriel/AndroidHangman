package com.example.enti_di_unica_pol_surriel.view.activities

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.databinding.ActivityGameBinding
import com.example.enti_di_unica_pol_surriel.model.KeyboardLetter
import com.example.enti_di_unica_pol_surriel.view.adapters.GuessedWordAdapter
import com.example.enti_di_unica_pol_surriel.view.adapters.KeyLetterAdapter
import com.example.enti_di_unica_pol_surriel.view.dialogs.*
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel
import com.example.enti_di_unica_pol_surriel.viewmodel.HangmanViewModel
import com.google.android.flexbox.*


class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    private val hangmanViewModel : HangmanViewModel by viewModels()

    /**
     * Used to prevent fast clicks to cause multiple event calls.
     *
     * ONLY USED IN THE METHOD [onKeyPressed]
     *
     * */
    private var processingKeyPressed = false

    /**
     * A list of all Hangman state images.
     *
     * index+1 = number of fails
     * */
    private val hangmanImages = listOf(
        com.example.enti_di_unica_pol_surriel.R.drawable.hm0,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm1,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm2,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm3,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm4,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm5,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm6,
        com.example.enti_di_unica_pol_surriel.R.drawable.hm7,
    )

    private lateinit var soundtrack : MediaPlayer
    private lateinit var rewardSound : MediaPlayer
    private lateinit var failSound : MediaPlayer
    private lateinit var endSound : MediaPlayer

    companion object {
        private const val SONG_VOLUME = 0.4f
        private const val PAUSED_SONG_VOLUME = SONG_VOLUME * 0.5f
        private const val PAUSED_SONG_SPEED = 0.92f

    }


    // -------------- ACTIVITY LIFECYCLE METHODS --------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAudio()


        binding.pauseButton.setOnClickListener {
            PauseDialog(this){
                if(App.soundActive.value!!) {
                    soundtrack.setVolume(SONG_VOLUME, SONG_VOLUME)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        soundtrack.playbackParams = soundtrack.playbackParams.setSpeed(1f)
                    }
                }
            }.show()
            soundtrack.setVolume(PAUSED_SONG_VOLUME, PAUSED_SONG_VOLUME)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(App.soundActive.value!!) {
                    soundtrack.playbackParams =
                        soundtrack.playbackParams.setSpeed(PAUSED_SONG_SPEED)
                }
            }
        }

        supportActionBar?.hide()
        binding.scoreindicator.text = hangmanViewModel.score.toString()

        binding.recyclerViewGuessedWord.adapter = GuessedWordAdapter(this)
        binding.recyclerViewGuessedWord.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        setupKeyboard()

        val loadDialog = LoadingDialog(this)
        loadDialog.show()

        if(!hangmanViewModel.gameRunning()){
            hangmanViewModel.createNewGame(
                succeedCallback = {
                    runOnUiThread {
                        loadDialog.dismiss()
                        AuthViewModel.registerAnalyticsLevelStart(this)
                    }
                 },
                failCallback = {
                    runOnUiThread{
                        // Errors are not translated!
                        Toast.makeText(this,"INTERNET ERROR", Toast.LENGTH_LONG).show()
                    }
                }

            )
        }

    }

    override fun onPause() {
        super.onPause()
        if(App.soundActive.value!!){
            soundtrack.pause()
            endSound.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if(App.soundActive.value!!) {
            soundtrack.start()
        }
    }

    override fun onDestroy() {
        hangmanViewModel.endGame()
        super.onDestroy()
    }

    // -------------- METHODS --------------------


    /**
     * Initializes the audio resources and game sounds logic.
     * */
    private fun setupAudio(){
        val soundID = resources.getIdentifier("sound_bg","raw", packageName)
        val rewardSoundID = resources.getIdentifier("reward_sound","raw", packageName)
        val failSoundID = resources.getIdentifier("failsound","raw", packageName)
        val endMusicID = resources.getIdentifier("end_song","raw", packageName)

        soundtrack = MediaPlayer.create(this, soundID)
        soundtrack.isLooping = true
        soundtrack.setVolume(SONG_VOLUME, SONG_VOLUME)
        if(App.soundActive.value!!) {
            soundtrack.start()
        }

        rewardSound = MediaPlayer.create(this, rewardSoundID)
        endSound = MediaPlayer.create(this, endMusicID)

        failSound = MediaPlayer.create(this, failSoundID)

    }

    /**
     * Checks if the user won.
     *
     * @return True: the user already won the match! False: They didn't
     * */
    private fun won():Boolean{
        for (l in HangmanViewModel.guessedWord.value!!){
            if(l !in 'a'..'z')
                return false
        }
        return true
    }


    /**
     * Called when the user clicked a correct letter key.
     *
     * NOT called if the key pressed was already clicked.
     *
     * */
    private fun onGuessSucceed(){
        if(App.soundActive.value!!)
            rewardSound.start()
        hangmanViewModel.updateScore()
        binding.scoreindicator.text = hangmanViewModel.score.toString()

        if(won()){
            onWon()
        }

    }

    /**
     * Called when the user clicked a correct letter key and reached the total of hangman word
     * guessed.
     * */
    private fun onWon() {
        VictoryDialog(this, hangmanViewModel.score).show()
        hangmanViewModel.endGame()
        if(App.soundActive.value!!){
            soundtrack.pause()
            endSound.start()
        }
    }


    /**
     * Called when the user reached the max possible errors (defined by hangmanImages size)
     * */
    private fun onLose(){
        if(hangmanViewModel.adChanceActive){
            hangmanViewModel.adChanceActive = false
            LastChanceAdDialog(this, hangmanViewModel.score) {

            }.show()
        }else {
            hangmanViewModel.endGame()
            GameOverDialog(this, hangmanViewModel.score).show()

        }
    }

    /**
     * Called when the user clicked an incorrect letter key.
     *
     * NOT called if the key pressed was already clicked.
     *
     * */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun onGuessFailed(){
        if(App.soundActive.value!!) {
            failSound.start()
        }

        hangmanViewModel.failsCount++

        if(hangmanImages.size <= hangmanViewModel.failsCount){
            onLose()
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.hangmanState.setImageDrawable(
                    resources.getDrawable(
                        hangmanImages[hangmanViewModel.failsCount],
                        applicationContext.theme
                    )
                )
            } else {
                binding.hangmanState.setImageDrawable(
                    resources.getDrawable(
                        hangmanImages[hangmanViewModel.failsCount],
                        applicationContext.theme
                    )
                )
            }
        }


    }

    /**
     * Process the game keyboard key pressed (key down) event
     * */
    private fun onKeyPressed(letter:String) {

        // Prevent faster clicks than API requests
        if(letter in hangmanViewModel.triedLetters || processingKeyPressed)
            return

        processingKeyPressed = true
        hangmanViewModel.guessLetter(letter,
            succeedCallback = {
                runOnUiThread{
                    processingKeyPressed = false

                    if(hangmanViewModel.triedLetters.isNotEmpty()){
                        hangmanViewModel.triedLetters += ","
                    }

                    if(it){
                        hangmanViewModel.triedLetters += " $letter"
                        onGuessSucceed()
                    }else {
                        hangmanViewModel.triedLetters += " <font color=#cc0029>$letter</font>"
                        onGuessFailed()
                    }
                    binding.triedletters.text = HtmlCompat.fromHtml(
                        hangmanViewModel.triedLetters,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )


                }

            },
            failCallback = {
                runOnUiThread {
                    processingKeyPressed = false
                    // Errors are not translated!
                    Toast.makeText(this, "INTERNET ERROR", Toast.LENGTH_LONG).show()
                }
            }
        )


    }

    /**
     * Programmatically creates the game keyboard.
     *
     * The letters follow the alphabetic order, not the standard mobile keyboard order.
     * */
    private fun setupKeyboard(){

        for(l in 'A'..'Z'){
            hangmanViewModel.keyboardLetters.add(KeyboardLetter(l+""))
        }

        val adapter = KeyLetterAdapter(hangmanViewModel.keyboardLetters){
            onKeyPressed(it)
        }
        binding.keyboardRV.adapter = adapter
        binding.keyboardRV.layoutManager = FlexboxLayoutManager(this).apply {
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
    }
}