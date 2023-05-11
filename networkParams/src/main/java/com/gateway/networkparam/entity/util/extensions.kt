package com.gateway.networkparam.entity.util

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
