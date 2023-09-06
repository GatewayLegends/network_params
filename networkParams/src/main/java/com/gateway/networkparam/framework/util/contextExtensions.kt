package com.gateway.networkparam.framework.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
internal fun Context.getAllSubTelephonyManagers(): List<TelephonyManager> {
    return subscriptionManager().getAllSubscriptionInfo().map {
        telephonyManager().getSubTelephonyManager(it.subscriptionId)
    }
}

internal fun Context.getExecutor(): Executor =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        mainExecutor
    else
        ContextCompat.getMainExecutor(this)

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

/**
 * Gives the telephony manager
 * @return the @code{[TelephonyManager]}
 */
internal fun Context.telephonyManager() =
    getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

/**
 * Gives the subscription manager
 * @return the @code{[SubscriptionManager]}
 */
internal fun Context.subscriptionManager() =
    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
