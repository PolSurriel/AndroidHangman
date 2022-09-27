package com.example.roomclass.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomclass.model.database.PersonEntity.PersonEntity
import com.example.roomclass.model.database.dao.PersonDAO

@Database(
    entities = [PersonEntity::class],
    version = 1
)
abstract class PeopleDB : RoomDatabase(){

    abstract fun personDAO() : PersonDAO

}