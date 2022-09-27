package com.example.roomclass.model

import com.example.roomclass.database.PersonEntity.PersonEntity

class PersonModel{
    lateinit var  name: String;
    var  age: Int = 0;
    lateinit var emailAdress: String

    constructor(name: String, age: Int, emailAdress: String){
        this.age = age
        this.name = name
        this.emailAdress = emailAdress
    }




}