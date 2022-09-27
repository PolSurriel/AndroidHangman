package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Dialog
import android.content.Context
import com.example.enti_di_unica_pol_surriel.R

/**
 * Shown wen there is some process loading. Use it when your user interaction may wait
 * until some process finishes!
 * */
class LoadingDialog(context: Context) : Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.loading_dialog
    }

    init {
        setContentView(LAYOUT_ID)
        setCancelable(false)
    }
}