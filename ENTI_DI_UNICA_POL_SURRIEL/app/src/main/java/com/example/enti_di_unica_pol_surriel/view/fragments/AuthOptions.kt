package com.example.enti_di_unica_pol_surriel.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.enti_di_unica_pol_surriel.databinding.FragmentAuthOptionsBinding
import com.example.enti_di_unica_pol_surriel.view.dialogs.AuthDialog
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel

class AuthOptions : Fragment() {

    lateinit var binding: FragmentAuthOptionsBinding

    val authViewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthOptionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openloginbtn.setOnClickListener {
            AuthDialog(view.context, authViewModel, AuthDialog.AuthMode.LOGIN){

            }.show()
        }

        binding.openregisterbtn.setOnClickListener {
            AuthDialog(view.context,authViewModel, AuthDialog.AuthMode.REGISTER){

            }.show()
        }

    }

}