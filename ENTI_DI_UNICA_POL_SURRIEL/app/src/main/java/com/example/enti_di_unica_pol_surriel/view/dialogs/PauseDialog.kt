package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.example.enti_di_unica_pol_surriel.R

/**
 * Pause menu.
 * */
class PauseDialog(context: Context, onResumeCallback: () -> Unit = {}) : Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.pause_dialog
    }

    init {
        setContentView(LAYOUT_ID)
        findViewById<Button>(R.id.resumebtn).setOnClickListener {
            onResumeCallback()
            dismiss()
        }
        findViewById<Button>(R.id.newgamebtn).setOnClickListener {
            dismiss()
            (context as Activity).startActivity((context).intent)
            (context).finish()

        }
        findViewById<Button>(R.id.menubtn).setOnClickListener {
            dismiss()
            (context as Activity).finish()
        }
    }
}