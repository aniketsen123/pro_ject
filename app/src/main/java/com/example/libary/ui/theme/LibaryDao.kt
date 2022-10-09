package com.example.libary.ui.theme

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LibaryDao {
    @Query("SELECT * FROM libary WHERE libary.id=:id")
    suspend fun getlibaryById(id:Int):libaryentity?
    @Query("SELECT * FROM libary ORDER BY dateUpdated DESC ")
    fun getLibary():LiveData<List<libaryentity>>
    @Delete
    fun delete(libaryentity: libaryentity):Int
    @Update
    fun update(libaryentity: libaryentity):Int
    @Insert
    fun insert(libaryentity: libaryentity)
}