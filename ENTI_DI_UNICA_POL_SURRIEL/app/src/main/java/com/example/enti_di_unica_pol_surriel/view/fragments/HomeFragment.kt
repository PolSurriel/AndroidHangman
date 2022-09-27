package com.example.enti_di_unica_pol_surriel.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.enti_di_unica_pol_surriel.App
import com.example.enti_di_unica_pol_surriel.databinding.FragmentHomeBinding
import com.example.enti_di_unica_pol_surriel.view.activities.MainActivity
import com.example.enti_di_unica_pol_surriel.view.dialogs.ChangeNameDialog
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            (activity as MainActivity).startPlay()
        }

        binding.editnameicon.setOnClickListener {
            ChangeNameDialog(view.context).show()
        }


        binding.username.text = App.username.value

        App.username.observe(viewLifecycleOwner) {
            binding.username.text = App.username.value
            AuthViewModel.postUserSettings()

        }

    }


}