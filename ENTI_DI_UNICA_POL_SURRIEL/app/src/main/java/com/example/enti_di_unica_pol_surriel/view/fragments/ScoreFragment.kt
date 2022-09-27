package com.example.enti_di_unica_pol_surriel.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.enti_di_unica_pol_surriel.databinding.FragmentScoreBinding
import com.example.enti_di_unica_pol_surriel.view.adapters.UserScoreAdapter
import com.example.enti_di_unica_pol_surriel.viewmodel.AuthViewModel
import com.example.enti_di_unica_pol_surriel.viewmodel.ScoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class ScoreFragment : Fragment() {

    lateinit var binding: FragmentScoreBinding
    private val scoreViewModel : ScoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserScoreAdapter(scoreViewModel, this)
        binding.scoreRecyclerView.adapter = adapter
        binding.scoreRecyclerView.layoutManager = LinearLayoutManager(view.context)

        CoroutineScope(Dispatchers.IO).launch {
            while(true){
                requestScoreUpdate()
                Thread.sleep(2000)
            }
        }
    }

    private fun requestScoreUpdate(){
        AuthViewModel.getScoreRanking {
            scoreViewModel.score.postValue(it)
        }

    }

}