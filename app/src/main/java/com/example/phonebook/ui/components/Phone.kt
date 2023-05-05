package com.example.phonebook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.PhoneModel
import com.example.phonebook.utils.fromHex

@ExperimentalMaterialApi
@Composable
fun Phone(
    modifier: Modifier = Modifier,
    phoneModel: PhoneModel,
    onPhoneClick: (PhoneModel) -> Unit = {},
    onNoteCheckedChange: (PhoneModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = {
            Column() {
                Text(text = "Name: ${phoneModel.name}", maxLines = 1)
                Text(text = "Tag: ${phoneModel.tag}", maxLines = 1)
            }    
                 },
            secondaryText = {
                Text(text = "phone: ${phoneModel.number}", maxLines = 1)
            },
            icon = {
                PhoneColor(
                    color = Color.fromHex(phoneModel.color.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
//            trailing = {
//                if (phoneModel.isCheckedOff != null) {
//                    Checkbox(
//                        checked = phoneModel.isCheckedOff,
//                        onCheckedChange = { isChecked ->
//                            val newNote = phoneModel.copy(isCheckedOff = isChecked)
//                            onNoteCheckedChange.invoke(newNote)
//                        },
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                }
//            },
            modifier = Modifier.clickable {
                onPhoneClick.invoke(phoneModel)
            }
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun PhonePreview() {
//    Phone(phoneModel = PhoneModel(1, "Note 1", "Content 1", "tag1", ColorModel.DEFAULT,), isSelected = true)
}