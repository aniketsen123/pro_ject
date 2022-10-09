package com.example.libary.ui.theme

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [libaryentity::class])
abstract class LibaryDatabase:RoomDatabase() {
abstract fun LibaryDao():LibaryDao
}
