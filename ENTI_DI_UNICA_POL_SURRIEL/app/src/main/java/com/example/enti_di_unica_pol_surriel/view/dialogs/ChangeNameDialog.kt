package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.R

/**
 * Allows the user to change their username
 * */
class ChangeNameDialog(context: Context) : Dialog(context) {

    companion object{
        private const val LAYOUT_ID = R.layout.change_name_dialog
    }

    init {
        setContentView(LAYOUT_ID)

        val editText = findViewById<EditText>(R.id.changeUsernameEditText)
        editText.setText(App.username.value.toString(), TextView.BufferType.EDITABLE)

        findViewById<Button>(R.id.savenamebtn).setOnClickListener {
            App.username.postValue(editText.text.toString())
            dismiss()
        }
    }
}