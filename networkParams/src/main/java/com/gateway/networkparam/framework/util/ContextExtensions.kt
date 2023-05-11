package com.gateway.networkparam.framework.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
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
