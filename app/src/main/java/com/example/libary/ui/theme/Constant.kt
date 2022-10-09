package com.example.libary.ui.theme

object Constant {
    const val NAVIGATION_Libary_LIST = "libaryList"
    const val NAVIGATION_Libary_Detail = "libarydetail/{libaryId}"
    const val NAVIGATION_Libary_edit = "libaryedit/{libaryId}"
    const val TABLE_NAME="libary"
    const val NAVIGATION_Libary_id="libaryid"
    const val DATA_BASE_NAME="libary database"
    val libarydetailPlaceHolder=libaryentity(id=0,name_candi="Not Specified", book_no = "Not Specified", name_book = "Not Specified")
    const val NAVIGATION_LIBARY_CREATE="libaryCreate"

    fun libaryDetailNavigation(libaryId : Int) = "libarydetail/$libaryId"
    fun noteEditNavigation(libaryId : Int) = "libaryedit/$libaryId"



}