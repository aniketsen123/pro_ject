package com.example.libary.ui.theme

import android.view.KeyEvent
import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class libaryViewModel(private var db:LibaryDao):ViewModel() {

    val libary:LiveData<List<libaryentity>> = db.getLibary()

    fun deleteNode(libaryentity: libaryentity) {
     viewModelScope.launch(Dispatchers.IO) {
         db.delete(libaryentity)
     }
    }
    fun updateNode(libaryentity: libaryentity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.update(libaryentity)
        }
    }
    fun create(name_candi:String,name_book:String,book_no:String,image:String?=null)
    {
        val libary=libaryentity(name_candi =name_candi , name_book =name_book, book_no = book_no, image_uri = image )

            viewModelScope.launch(Dispatchers.IO) {
                db.insert(libary)
            }
        }
       suspend fun getlibary(id:Int):libaryentity?
       {
           return db.getlibaryById(id)
       }



}
class ViewModelFactory(private val db:LibaryDao):ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return libaryViewModel(db=db)as T
    }
}