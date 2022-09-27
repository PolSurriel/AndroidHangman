package com.example.room

import androidx.room.Entity

@Entity
data class Person (
    val name:String,
    val age:Int,
    val address:String,

    val id:Int
    )

