package com.example.baseproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor() : ViewModel() {

    private var _timerFlow = MutableSharedFlow<Long>()
    val timerFlow: Flow<Long> = _timerFlow.asSharedFlow()
    private var ts: Long = 0L

    companion object {
        private const val TAG = "ListViewModel"
    }

    fun initTimer(totalSeconds: Long) {
        ts = totalSeconds
        viewModelScope.launch {
            while (ts > 0) {
                delay(1000)
                ts -= 1
                Log.d(TAG, "initTimer: $ts")
                _timerFlow.emit(ts)
            }
        }
    }

    fun addToTimer() {
        ts += 30
    }

    fun subtractFromTimer() {
        if(ts > 30)
            ts -= 30
    }
}