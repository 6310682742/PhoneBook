package com.example.phonebook.domain.model
const val NEW_PHONE_ID = -1L
data class PhoneModel(
    val id:Long = NEW_PHONE_ID,
    val name:String = "",
    val number:String = "",
    val tag:String = "",
    val isCheckedOff: Boolean? = null, // null represents that the note can't be checked off
    val color: ColorModel = ColorModel.DEFAULT
)