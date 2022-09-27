package com.example.roomclass.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.roomclass.model.database.PersonEntity.PersonEntity

@Dao
interface PersonDAO {

    @Query("SELECT * FROM PersonEntity")
    suspend fun getAllPeople():List<PersonEntity>

    @Query("SELECT * FROM PersonEntity WHERE id = :personID")
    suspend fun getPersonByID(personID : Int): PersonEntity

    @Query("SELECT * FROM PersonEntity WHERE name LIKE '%' || :personName || '%'")
    suspend fun getPersonBYName(personName : String):List<PersonEntity>

    @Insert
    suspend fun insertPerson(person : PersonEntity)

    @Update
    suspend fun updatePerson(person : PersonEntity)

    @Insert
    suspend fun insertPersons(people : List<PersonEntity>)

    @Query("DELETE FROM PersonEntity")
    suspend fun deleteAllPeople()

}