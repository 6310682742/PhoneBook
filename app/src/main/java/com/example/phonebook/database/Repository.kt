package com.example.phonebook.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.PhoneModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
class Repository(
    private val phoneDao: PhoneDao,
    private val colorDao: ColorDao,
    private val dbMapper: DbMapper
) {

    // Working Notes
    private val phonesNotInTrashLiveData: MutableLiveData<List<PhoneModel>> by lazy {
        MutableLiveData<List<PhoneModel>>()
    }

    fun getAllPhonesNotInTrash(): LiveData<List<PhoneModel>> = phonesNotInTrashLiveData

    // Deleted Notes
    private val phonesInTrashLiveData: MutableLiveData<List<PhoneModel>> by lazy {
        MutableLiveData<List<PhoneModel>>()
    }

    fun getAllPhonesInTrash(): LiveData<List<PhoneModel>> = phonesInTrashLiveData

    init {
        initDatabase(this::updatePhonesLiveData)
    }

    /**
     * Populates database with colors if it is empty.
     */
    private fun initDatabase(postInitAction: () -> Unit) {
        GlobalScope.launch {
            // Prepopulate colors
            val colors = ColorDbModel.DEFAULT_COLORS.toTypedArray()
            val dbColors = colorDao.getAllSync()
            if (dbColors.isNullOrEmpty()) {
                colorDao.insertAll(*colors)
            }

            // Prepopulate notes
            val phones = PhoneDbModel.DEFAULT_PHONES.toTypedArray()
            val dbPhones = phoneDao.getAllSync()
            if (dbPhones.isNullOrEmpty()) {
                phoneDao.insertAll(*phones)
            }

            postInitAction.invoke()
        }
    }

    // get list of working notes or deleted notes
    private fun getAllNotesDependingOnTrashStateSync(inTrash: Boolean): List<PhoneModel> {
        val colorDbModels: Map<Long, ColorDbModel> = colorDao.getAllSync().map { it.id to it }.toMap()
        val dbNotes: List<PhoneDbModel> =
            phoneDao.getAllSync().filter { it.isInTrash == inTrash }
        return dbMapper.mapNotes(dbNotes, colorDbModels)
    }

    fun insertNote(note: PhoneModel) {
        phoneDao.insert(dbMapper.mapDbNote(note))
        updatePhonesLiveData()
    }

    fun deleteNotes(noteIds: List<Long>) {
        phoneDao.delete(noteIds)
        updatePhonesLiveData()
    }

    fun moveNoteToTrash(noteId: Long) {
        val dbNote = phoneDao.findByIdSync(noteId)
        val newDbNote = dbNote.copy(isInTrash = true)
        phoneDao.insert(newDbNote)
        updatePhonesLiveData()
    }

    fun restoreNotesFromTrash(noteIds: List<Long>) {
        val dbNotesInTrash = phoneDao.getNotesByIdsSync(noteIds)
        dbNotesInTrash.forEach {
            val newDbNote = it.copy(isInTrash = false)
            phoneDao.insert(newDbNote)
        }
        updatePhonesLiveData()
    }

    fun getAllColors(): LiveData<List<ColorModel>> =
        colorDao.getAll().map() { dbMapper.mapColors(it) }

    private fun updatePhonesLiveData() {
        phonesNotInTrashLiveData.postValue(getAllNotesDependingOnTrashStateSync(false))
        phonesInTrashLiveData.postValue(getAllNotesDependingOnTrashStateSync(true))
    }
}