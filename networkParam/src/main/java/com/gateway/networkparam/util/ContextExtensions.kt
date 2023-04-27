package com.gateway.networkparam.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getAllSubTelephonyManagers(): List<TelephonyManager> {
    return subscriptionManager().getAllSubscriptionInfo().mapNotNull {
        it.subscriptionId?.let { subscriptionId ->
            telephonyManager().getSubTelephonyManager(subscriptionId)
        }
    }
}

fun Context.getExecutor(): Executor =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        mainExecutor
    else
        ContextCompat.getMainExecutor(this)
