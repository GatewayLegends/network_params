package com.gateway.networkparam.util

import android.telephony.AccessNetworkConstants
import android.telephony.NetworkRegistrationInfo
import android.telephony.TelephonyManager
import com.gateway.networkparam.entity2.enums.*
import org.json.JSONObject

fun Any.reflectAsJson(): JSONObject {
    val root = JSONObject()

    val mClass = this::class.java

    mClass.methods.forEach {
        it.isAccessible = true

        val name = it.name
        val value = runCatching {
            it(this)
        }.getOrNull()

        root.put(name, value)
    }

    return root
}

internal fun Map<String, Any?>.toJson(): String = JSONObject(this).toString()

val Boolean?.isTrue: Boolean get() = this == true
