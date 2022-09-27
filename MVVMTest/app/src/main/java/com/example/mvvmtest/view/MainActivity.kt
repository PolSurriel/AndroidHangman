package com.example.mvvmtest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.mvvmtest.R
import com.example.mvvmtest.databinding.ActivityMainBinding
import com.example.mvvmtest.viewmodel.ReminderViewModel

class MainActivity : AppCompatActivity() {

    private val reminderViewModel : ReminderViewModel by viewModels()

    lateinit var binding : ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reminderViewModel.reminderModel.observe(this, Observer {
            
        })
    }
}