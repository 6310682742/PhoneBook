package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PhoneDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "color") val colorId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean,
    @ColumnInfo(name = "can_be_checked_off") val canBeCheckedOff: Boolean,
    @ColumnInfo(name = "is_checked_off") val isCheckedOff: Boolean,
) {
    companion object {
        val DEFAULT_PHONES  = listOf(
            PhoneDbModel(1,"name1", "123", "tag1",1,false,false,false),
            PhoneDbModel(2,"name2", "456", "tag2",2,false,false,false),
            PhoneDbModel(3,"name3", "798", "tag3",3,false,false,false),
            PhoneDbModel(4,"name4", "234", "tag3",4,false,false,false),
            PhoneDbModel(5,"name5", "567", "tag2",5,false,false,false),
            PhoneDbModel(6,"name6", "890", "tag1",6,false,false,false),
            PhoneDbModel(7,"name7", "345", "tag1",7,false,false,false),
            PhoneDbModel(8,"name8", "678", "tag2",8,false,false,false),
            PhoneDbModel(8,"aname8", "678", "tag2",8,false,false,false),
        )
    }
}