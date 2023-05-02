package com.gateway.networkparam.framework.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Check [Manifest.permission.READ_PHONE_STATE] permission
 * @return the code {[Boolean]}
 */
internal fun Context.isPhoneStateGranted() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

/**
 * Check [Manifest.permission.ACCESS_FINE_LOCATION] permission
 * @return the code {[Boolean]}
 */
internal fun Context.isFineLocationGranted() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

/**
 * Check [Manifest.permission.ACCESS_COARSE_LOCATION] permission
 * @return the code {[Boolean]}
 */
internal fun Context.isCoarseLocationGranted() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
