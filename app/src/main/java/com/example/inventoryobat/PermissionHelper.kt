package com.example.inventoryobat

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {

    const val PERMISSION_REQUEST_CODE = 100

    private val requiredPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    fun checkPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, requiredPermission) == PackageManager.PERMISSION_GRANTED

    fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(requiredPermission), PERMISSION_REQUEST_CODE)
    }
}
