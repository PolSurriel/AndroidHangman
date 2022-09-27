package com.example.enti_di_unica_pol_surriel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.enti_di_unica_pol_surriel.model.UserScore

/**
 * ViewModel used for those views that needs the score ranking to be consulted.
 * */
class ScoreViewModel : ViewModel() {

    /** The score ranking observable list.*/
    val score = MutableLiveData<List<UserScore>>()

}