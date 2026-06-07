package com.example.gestionrendez_vous.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageHelper {

    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun copyUriToFile(context: Context, uri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = createImageFile(context)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun deleteImage(path: String?) {
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}
