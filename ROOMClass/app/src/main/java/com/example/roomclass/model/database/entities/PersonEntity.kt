package com.example.roomclass.model.database.PersonEntity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomclass.model.PersonModel

@Entity
data class PersonEntity (
    val name: String,
    val age: Int,
    val emailAdress: String,

    @PrimaryKey(autoGenerate = true) val id : Int = 0
    ){

    val model: PersonModel
        get(){
            return PersonModel(name, age, emailAdress)
        }

    fun fromModel(personModel: PersonModel) : PersonEntity{
        return PersonEntity(personModel.name,personModel.age,personModel.emailAdress)
    }
}


