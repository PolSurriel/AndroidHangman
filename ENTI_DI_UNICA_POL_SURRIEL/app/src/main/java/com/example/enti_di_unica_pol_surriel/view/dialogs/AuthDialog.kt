package com.example.enti_di_unica_pol_surriel.view.dialogs

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.R
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel


/**
 * Shown when the users login or register.
 *
 * Can be opened in register mode or in login mode.
 *
 * The options will be different depending on the mode.
 * */
class AuthDialog(
    context: Context,
    authViewModel: AuthViewModel,
    private var mode: AuthMode,
    private val onFinishedCallback: () -> Unit = {}
) : Dialog(context) {

    enum class AuthMode{
        REGISTER,
        LOGIN
    }

    private val userViewModel : AuthViewModel = authViewModel

    companion object{
        private const val LAYOUT_ID = R.layout.auth_dialog
    }

    init {
        setContentView(LAYOUT_ID)
        setUpBindings()
    }

    /**
     * If the dialog was opened in register mode, sets the corresponding button action content
     * and events
     * */
    private fun setupAsRegisterMode(){

        findViewById<Button>(R.id.authactionbtn).setText(R.string.register_action_name)

        findViewById<Button>(R.id.authactionbtn).setOnClickListener {
            userViewModel.register(
                findViewById<EditText>(R.id.email).text.toString(),
                findViewById<EditText>(R.id.pass).text.toString(),
                succeedCallback = {
                    dismiss()
                    App.username.postValue(
                        findViewById<EditText>(R.id.email)
                            .text.toString()
                            .substring(0, findViewById<EditText>(R.id.email)
                                .text.toString().indexOf('@'))
                    )
                    onFinishedCallback()

                },
                failCallback = {
                    Toast.makeText(
                        context,
                        R.string.register_error_message,
                        Toast.LENGTH_LONG).show()
                }
            )
        }
    }


    /**
     * If the dialog was opened in log in mode, sets the corresponding button action content
     * and events
     * */
    private fun setUpAsSigninMode(){

        findViewById<Button>(R.id.authactionbtn).setText(R.string.login_action_name)
        findViewById<Button>(R.id.authactionbtn).setOnClickListener {
            userViewModel.login(
                findViewById<EditText>(R.id.email).text.toString(),
                findViewById<EditText>(R.id.pass).text.toString(),
                succeedCallback = {
                    dismiss()

                    onFinishedCallback()
                },
                failCallback = {
                    Toast.makeText(
                        context,
                        R.string.login_error_message,
                        Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun setUpBindings() {
        when(mode){
            AuthMode.LOGIN -> setUpAsSigninMode()
            AuthMode.REGISTER -> setupAsRegisterMode()
        }

        findViewById<ImageView>(R.id.closedialogbtn).setOnClickListener {
            dismiss()
        }

    }

}