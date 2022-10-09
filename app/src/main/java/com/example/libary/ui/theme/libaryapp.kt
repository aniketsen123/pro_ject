package com.example.libary.ui.theme

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase

class libaryapp:Application() {
    private var db:LibaryDatabase?=null
    init {
        instance=this
    }
    private fun getDb():LibaryDatabase{
        if(db!=null)
            return db!!
        else
        {
            db= Room.databaseBuilder(instance!!.applicationContext,LibaryDatabase::class.java,
                Constant.DATA_BASE_NAME).fallbackToDestructiveMigration().build()
         return db!!
        }
    }
    companion object{
        private var instance:libaryapp?=null
        fun getDao():LibaryDao{
            return instance!!.getDb().LibaryDao()
        }
        fun getUriPermission(uri:Uri)
        {
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }
}