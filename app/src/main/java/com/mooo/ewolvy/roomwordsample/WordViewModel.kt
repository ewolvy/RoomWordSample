package com.mooo.ewolvy.roomwordsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import kotlin.coroutines.CoroutineContext

class WordViewModel(application: Application) : AndroidViewModel(application){
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(word: Word) = scope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}