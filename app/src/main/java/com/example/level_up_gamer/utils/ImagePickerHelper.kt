package com.example.level_up_gamer.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.io.File
import java.io.FileOutputStream

object ImagePickerHelper {

    /**
     * Copia la imagen seleccionada al almacenamiento interno de la app
     * y retorna la URI permanente
     */
    fun saveImageToInternalStorage(context: Context, sourceUri: Uri, userId: Int): String? {
        try {
            // Crear directorio para las fotos de perfil si no existe
            val directory = File(context.filesDir, "profile_pictures")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Crear archivo con nombre único basado en userId
            val fileName = "profile_$userId.jpg"
            val destinationFile = File(directory, fileName)

            // Copiar el contenido de la URI al archivo
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Retornar la ruta del archivo
            return destinationFile.absolutePath

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Elimina la foto de perfil anterior si existe
     */
    fun deleteOldProfilePicture(imagePath: String?) {
        imagePath?.let {
            try {
                val file = File(it)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/**
 * Composable que proporciona un launcher para seleccionar imágenes
 */
@Composable
fun rememberImagePickerLauncher(
    onImageSelected: (Uri) -> Unit
): androidx.activity.result.ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }
}