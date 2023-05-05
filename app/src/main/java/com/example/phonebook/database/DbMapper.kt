package com.example.phonebook.database

import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.PhoneModel
import com.example.phonebook.domain.model.NEW_PHONE_ID
class DbMapper {
    fun mapNotes(
        phoneDbModel: List<PhoneDbModel>,
        colorDbModels: Map<Long, ColorDbModel>
    ): List<PhoneModel> = phoneDbModel.map {
        val colorDbModel = colorDbModels[it.colorId]
            ?: throw RuntimeException("Color for colorId: ${it.colorId} was not found. Make sure that all colors are passed to this method")

        mapNote(it, colorDbModel)
    }
    fun mapNote(phoneDbModel: PhoneDbModel, colorDbModel: ColorDbModel): PhoneModel {
        val color = mapColor(colorDbModel)
        val isCheckedOff = with(phoneDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(phoneDbModel) { PhoneModel(id, name, number, tag,isCheckedOff, color) }
    }
    fun mapColors(colorDbModels: List<ColorDbModel>): List<ColorModel> =
        colorDbModels.map { mapColor(it) }
    fun mapColor(colorDbModel: ColorDbModel): ColorModel =
        with(colorDbModel) { ColorModel(id, name, hex) }
    fun mapDbNote(note: PhoneModel): PhoneDbModel =
        with(note) {
            val canBeCheckedOff = isCheckedOff != null
            val isCheckedOff = isCheckedOff ?: false
            if (id == NEW_PHONE_ID)
                PhoneDbModel(
                    name = name,
                    number = number,
                    tag = tag,
                    colorId = color.id,
                    isInTrash = false,
                            canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                )
            else
                PhoneDbModel(id, name, number, tag, color.id, false,canBeCheckedOff, isCheckedOff)
        }

}