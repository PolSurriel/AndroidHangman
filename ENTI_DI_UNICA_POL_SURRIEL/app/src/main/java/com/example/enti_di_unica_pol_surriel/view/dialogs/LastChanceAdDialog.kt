package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Button
import com.example.enti_di_unica_pol_surriel.R
import com.example.enti_di_unica_pol_surriel.view.activities.FullScreenAdsActivity
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel

/**
 * Shown when the user loses the match but can view an Ad to get a second chance.
 * */
class LastChanceAdDialog(context: Context, score: Int, giveupCallback: () -> Unit = {}) :
    Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.last_chance_dialog
    }

    init {
        setContentView(LAYOUT_ID)
        setCancelable(false)
        findViewById<Button>(R.id.viewad).setOnClickListener {
            dismiss()
            val intent = Intent(context, FullScreenAdsActivity::class.java)
            context.startActivity(intent)
            AuthViewModel.registerAnalyticsNewChance(context,true)

        }
        findViewById<Button>(R.id.giveup).setOnClickListener {
            dismiss()
            GameOverDialog(context,score).show()
            giveupCallback()
            AuthViewModel.registerAnalyticsNewChance(context,false)

        }
    }


}