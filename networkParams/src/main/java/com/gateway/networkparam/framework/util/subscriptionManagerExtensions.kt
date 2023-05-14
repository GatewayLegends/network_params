package com.gateway.networkparam.framework.util

import android.Manifest
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission
import com.gateway.networkparam.repository.dto.MSubscriptionInfo

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
internal fun SubscriptionManager.getAllSubscriptionInfo() = activeSubscriptionInfoList.map {
    MSubscriptionInfo(it)
}
