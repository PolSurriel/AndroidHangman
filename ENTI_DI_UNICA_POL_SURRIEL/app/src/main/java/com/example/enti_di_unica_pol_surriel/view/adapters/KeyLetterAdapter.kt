package com.example.enti_di_unica_pol_surriel.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.enti_di_unica_pol_surriel.databinding.LetterKeyItemBinding
import com.example.enti_di_unica_pol_surriel.model.KeyboardLetter

class KeyLetterAdapter (private val letters : MutableList<KeyboardLetter>, private val onclickKeyCallback : (String)->Unit) : RecyclerView.Adapter<KeyLetterAdapter.ViewHolder>() {
    class ViewHolder(private val binding: LetterKeyItemBinding) : RecyclerView.ViewHolder(binding.root)  {

        fun bindData(keyboardLetter:KeyboardLetter, onclickKeyCallback: (String)->Unit){
            binding.letterKeyButon.text = keyboardLetter.letter

            if(keyboardLetter.pressed){
               binding.letterKeyButon.background.alpha = 100
                binding.letterKeyButon.setOnClickListener {

                }
            }else {
                binding.letterKeyButon.setOnClickListener {
                    onclickKeyCallback(keyboardLetter.letter)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LetterKeyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(letters[position]){
            letters[position].pressed = true
            notifyItemChanged(position)
            onclickKeyCallback(it)
        }
    }

    override fun getItemCount(): Int {
        return letters.size
    }
}