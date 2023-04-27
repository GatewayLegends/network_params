package com.gateway.networkparam.entity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.gateway.networkparam.entity2.enums.CallState
import com.gateway.networkparam.entity2.enums.NetworkType
import com.gateway.networkparam.entity2.enums.SimState
import com.gateway.networkparam.util.*
import com.gateway.networkparam.util.isPhoneStateGranted
import com.gateway.networkparam.util.toCallState
import com.gateway.networkparam.util.toNetworkType
import com.gateway.networkparam.util.toSimState

internal class SubTelephonyManager(
    private val context: Context,
    private val tm: TelephonyManager
) {

    val networkOperator: String?
        get() = tm.networkOperator

    val callState: CallState?
        @SuppressLint("MissingPermission")
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            if (context.isPhoneStateGranted().not())
                null
            else
                tm.callStateForSubscription.toCallState()
        else
            tm.callState.toCallState()

    val dataNetworkType: NetworkType?
        @SuppressLint("MissingPermission")
        get() = if (context.isPhoneStateGranted().not())
            null
        else tm.dataNetworkType.toNetworkType()

    val voiceNetworkType: NetworkType?
        @SuppressLint("MissingPermission")
        get() = if (context.isPhoneStateGranted().not())
            null
        else tm.voiceNetworkType.toNetworkType()

    val simState: SimState?
        get() = tm.simState.toSimState()

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mapOf(
        "networkOperator" to networkOperator,
        "callState" to callState?.value,
        "dataNetworkType" to dataNetworkType?.value,
        "voiceNetworkType" to voiceNetworkType?.value,
        "simState" to simState?.value
    )
}
