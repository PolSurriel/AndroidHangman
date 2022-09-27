package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.example.enti_di_unica_pol_surriel.R
import com.example.enti_di_unica_pol_surriel.viewmodel.HangmanViewModel

/**
 * Shown when the user loses the match and there is no way back.
 * */
class GameOverDialog(context: Context, score: Int) : Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.gameover_dialog

    }

    init {
        setContentView(LAYOUT_ID)
        setCancelable(false)

        findViewById<Button>(R.id.backmenubtn).setOnClickListener {
            dismiss()
            (context as Activity).finish()
        }

        findViewById<Button>(R.id.newgame).setOnClickListener {
            dismiss()
            (context as Activity).finish()
            context.startActivity((context).intent)
        }

        findViewById<TextView>(R.id.scoreindicatortv).text = score.toString()

        HangmanViewModel.postScore(score)
    }


}