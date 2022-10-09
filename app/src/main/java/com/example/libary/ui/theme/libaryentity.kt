package com.example.libary.ui.theme

import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = Constant.TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class libaryentity(
    @PrimaryKey(autoGenerate = true) val id:Int?=null,
    @ColumnInfo(name = "name_candidate")val name_candi:String,
    @ColumnInfo(name="name_book")val name_book:String,
    @ColumnInfo(name="Book_no")val book_no:String,
    @ColumnInfo(name="dateUpdated")val dateUpdated:String=getDate(),
    @ColumnInfo(name="Image_Uri")val image_uri:String?=null
)
fun getDate():String
{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH::mm::ss"))
    } else {
        return ""
    }
}
fun libaryentity.getDay(): String{
    if (this.dateUpdated.isEmpty()) return ""
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    return LocalDateTime.parse(this.dateUpdated,formatter ).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}


var placeHolder= listOf(libaryentity(name_candi = "Cannot find", name_book = "Cannot Find", book_no = "Cannot Find",id=0))

fun List<libaryentity>?.orPlaceHolderList():List<libaryentity>
{
    return  if(this !=null && this .isNotEmpty())
        this
    else
        placeHolder
}