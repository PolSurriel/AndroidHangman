package com.example.enti_di_unica_pol_surriel.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.enti_di_unica_pol_surriel.databinding.GuessedWordLetterItemBinding
import com.example.enti_di_unica_pol_surriel.viewmodel.HangmanViewModel

class GuessedWordAdapter (context: LifecycleOwner): RecyclerView.Adapter<GuessedWordAdapter.ViewHolder>() {

    init {
        HangmanViewModel.guessedWord.observe(context) {
            notifyDataSetChanged()
        }
    }


    class ViewHolder(private val binding: GuessedWordLetterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(letter : String){
            if(letter[0] in 'a'..'z')
                binding.letter.text = letter
            else
                binding.letter.text = " "

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GuessedWordLetterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(HangmanViewModel.guessedWord.value!![position].toString())
    }

    override fun getItemCount(): Int {

        if (HangmanViewModel.guessedWord.value == null)
            return 0

        return HangmanViewModel.guessedWord.value!!.length
    }
}