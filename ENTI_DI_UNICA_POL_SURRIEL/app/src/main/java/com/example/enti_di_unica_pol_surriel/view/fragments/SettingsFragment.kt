package com.example.enti_di_unica_pol_surriel.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.databinding.FragmentSettingsBinding
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AuthViewModel.logStatus.observe(viewLifecycleOwner) {
            setupAuthMode(AuthViewModel.logStatus.value!!)
        }


        binding.sound.isChecked = App.soundActive.value!!
        binding.notifications.isChecked = App.notifiactionsActive.value!!

        binding.sound.setOnClickListener {
            App.soundActive.postValue(binding.sound.isChecked)
        }

        binding.notifications.setOnClickListener {
            App.notifiactionsActive.postValue(binding.notifications.isChecked)
        }


        App.soundActive.observe(viewLifecycleOwner) {
            binding.sound.isChecked = App.soundActive.value!!
            AuthViewModel.postUserSettings()

        }

        App.notifiactionsActive.observe(viewLifecycleOwner) {
            binding.notifications.isChecked = App.notifiactionsActive.value!!
            AuthViewModel.postUserSettings()

        }

    }

    private fun setupAuthMode(logStatus: AuthViewModel.LogStatus){

        when (logStatus){
            AuthViewModel.LogStatus.Logged -> {
                binding.fragcontainerAuth.visibility = View.INVISIBLE
                binding.fragContainerAuthOut.visibility = View.VISIBLE
            }
            AuthViewModel.LogStatus.Anon -> {
                binding.fragcontainerAuth.visibility = View.VISIBLE
                binding.fragContainerAuthOut.visibility = View.INVISIBLE
            }
        }
    }

}