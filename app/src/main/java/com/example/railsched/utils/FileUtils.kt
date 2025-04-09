package com.example.railsched.utils

import android.content.Context
import java.io.File

object FileUtils {
    private const val FILE_NAME = "train_log.txt"

    fun saveTrainLog(context: Context, data: String): Boolean {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            file.appendText(data + "\n\n")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun readTrainLog(context: Context): String {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            file.readText()
        } catch (e: Exception) {
            "No logs found."
        }
    }
}
