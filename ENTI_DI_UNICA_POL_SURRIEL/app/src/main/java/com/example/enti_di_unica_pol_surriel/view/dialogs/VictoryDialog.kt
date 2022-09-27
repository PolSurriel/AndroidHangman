package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.example.enti_di_unica_pol_surriel.R
import com.example.enti_di_unica_pol_surriel.viewmodel.HangmanViewModel

/**
 * Shown when the users won.
 * */
class VictoryDialog(context: Context, score: Int) : Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.victory_dialog
    }

    init {
        setContentView(LAYOUT_ID)
        setCancelable(false)
        findViewById<TextView>(R.id.scoreindicatortv).text = score.toString()
        findViewById<TextView>(R.id.backmenubtn).setOnClickListener {
            dismiss()
            (context as Activity).finish()
        }
        findViewById<TextView>(R.id.newgame).setOnClickListener {
            dismiss()
            (context as Activity).finish()
            context.startActivity(context.intent)

        }
        HangmanViewModel.postScore(score)
    }
}