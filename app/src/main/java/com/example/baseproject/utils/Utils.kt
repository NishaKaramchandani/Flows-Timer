package com.example.baseproject.utils

object Utils {

    fun formatTime(secs: Long): String {
        return String.format("%02d:%02d:%02d", secs / 3600, secs % 3600 / 60, secs % 60)
    }
}