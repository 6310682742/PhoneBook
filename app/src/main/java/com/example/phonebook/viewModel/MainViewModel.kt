package com.example.phonebook.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonebook.database.AppDatabase
import com.example.phonebook.database.DbMapper
import com.example.phonebook.database.Repository
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.PhoneModel
import com.example.phonebook.routing.MyPhoneRouter
import com.example.phonebook.routing.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : ViewModel() {
    val phonesNotInTrash: LiveData<List<PhoneModel>> by lazy {
        repository.getAllPhonesNotInTrash()
    }

    private var _phoneEntry = MutableLiveData(PhoneModel())

    val phoneEntry: LiveData<PhoneModel> = _phoneEntry

    val colors: LiveData<List<ColorModel>> by lazy {
        repository.getAllColors()
    }

    val phonesInTrash by lazy { repository.getAllPhonesInTrash() }

    private var _selectedNotes = MutableLiveData<List<PhoneModel>>(listOf())

    val selectedNotes: LiveData<List<PhoneModel>> = _selectedNotes

    private val repository: Repository

    init {
        val db = AppDatabase.getInstance(application)
        repository = Repository(db.phoneDao(), db.colorDao(), DbMapper())
    }

    fun onCreateNewNoteClick() {
        _phoneEntry.value = PhoneModel()
        MyPhoneRouter.navigateTo(Screen.SavePhone)
    }

    fun onNoteClick(note: PhoneModel) {
        _phoneEntry.value = note
        MyPhoneRouter.navigateTo(Screen.SavePhone)
    }

    fun onNoteCheckedChange(note: PhoneModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertNote(note)
        }
    }

    fun onNoteSelected(note: PhoneModel) {
        _selectedNotes.value = _selectedNotes.value!!.toMutableList().apply {
            if (contains(note)) {
                remove(note)
            } else {
                add(note)
            }
        }
    }

    fun restoreNotes(notes: List<PhoneModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.restoreNotesFromTrash(notes.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedNotes.value = listOf()
            }
        }
    }

    fun permanentlyDeleteNotes(notes: List<PhoneModel>) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteNotes(notes.map { it.id })
            withContext(Dispatchers.Main) {
                _selectedNotes.value = listOf()
            }
        }
    }

    fun onNoteEntryChange(note: PhoneModel) {
        _phoneEntry.value = note
    }

    fun saveNote(note: PhoneModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertNote(note)

            withContext(Dispatchers.Main) {
                MyPhoneRouter.navigateTo(Screen.Phones)

                _phoneEntry.value = PhoneModel()
            }
        }
    }

    fun moveNoteToTrash(note: PhoneModel) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.moveNoteToTrash(note.id)

            withContext(Dispatchers.Main) {
                MyPhoneRouter.navigateTo(Screen.Phones)
            }
        }
    }
}
