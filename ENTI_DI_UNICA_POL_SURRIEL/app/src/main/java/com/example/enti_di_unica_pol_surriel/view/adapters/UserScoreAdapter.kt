package com.example.enti_di_unica_pol_surriel.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.enti_di_unica_pol_surriel.databinding.UserscoreItemBinding
import com.example.enti_di_unica_pol_surriel.model.UserScore
import com.example.enti_di_unica_pol_surriel.viewmodel.ScoreViewModel

class UserScoreAdapter (private val scoreViewModel: ScoreViewModel, context: LifecycleOwner) : RecyclerView.Adapter<UserScoreAdapter.ViewHolder>() {

    init {
        scoreViewModel.score.observe(context) {
            notifyDataSetChanged()
        }
    }

    class ViewHolder(private val binding: UserscoreItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(score: UserScore, index:Int){
            binding.scoreusername.text = score.userName
            binding.scorePoints.text = score.score.toString()
            binding.scoreRankingPosition.text = "$index."
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserscoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(scoreViewModel.score.value!![position], position+1)
    }

    override fun getItemCount(): Int {
        if(scoreViewModel.score.value == null){
            return 0
        }
        return scoreViewModel.score.value!!.size
    }
}