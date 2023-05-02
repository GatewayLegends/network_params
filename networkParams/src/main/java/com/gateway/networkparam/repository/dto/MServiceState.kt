package com.gateway.networkparam.repository.dto

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.NetworkRegistrationInfo
import android.telephony.ServiceState
import com.gateway.networkparam.entity.enums.DuplexMode
import com.gateway.networkparam.entity.enums.RegState
import com.gateway.networkparam.framework.util.isCoarseLocationGranted
import com.gateway.networkparam.framework.util.isFineLocationGranted
import com.gateway.networkparam.entity.util.reflectAsJson
import com.gateway.networkparam.entity.util.toJson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

internal class MServiceState(
    private val context: Context,
    private val serviceState: ServiceState
) {

    private var reflection: ServiceStateReflection? = null

    init {
        val gson = GsonBuilder().serializeNulls().create()
        val reflectionJson = serviceState.reflectAsJson()
        reflection = gson.fromJson(reflectionJson.toString(), ServiceStateReflection::class.java)
    }

    val cdmaNetworkId: Int?
        @SuppressLint("MissingPermission")
        get() = if (
            context.isFineLocationGranted().not() &&
            context.isCoarseLocationGranted().not()
        )
            null
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            serviceState.cdmaNetworkId.orNull()
        else
            null

    val cdmaSystemId: Int?
        @SuppressLint("MissingPermission")
        get() = if (
            context.isFineLocationGranted().not() &&
            context.isCoarseLocationGranted().not()
        )
            null
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            serviceState.cdmaSystemId.orNull()
        else
            null

    val cellBandwidths: List<Int>?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            serviceState.cellBandwidths?.toList()
        else
            null

    val channelNumber: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            serviceState.channelNumber
        else
            null

    val duplexMode: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            when (serviceState.duplexMode) {
                ServiceState.DUPLEX_MODE_UNKNOWN -> DuplexMode.UNKNOWN.value
                ServiceState.DUPLEX_MODE_FDD -> DuplexMode.FDD.value
                ServiceState.DUPLEX_MODE_TDD -> DuplexMode.TDD.value
                else -> null
            }
        else
            null

    val isManualSelection: Boolean
        get() = serviceState.isManualSelection

    val operatorAlphaLong: String?
        @SuppressLint("MissingPermission")
        get() = if (
            context.isFineLocationGranted().not() &&
            context.isCoarseLocationGranted().not()
        )
            null
        else
            serviceState.operatorAlphaLong

    val operatorAlphaShort: String?
        @SuppressLint("MissingPermission")
        get() = if (
            context.isFineLocationGranted().not() &&
            context.isCoarseLocationGranted().not()
        )
            null
        else
            serviceState.operatorAlphaShort

    val operatorNumeric: String?
        @SuppressLint("MissingPermission")
        get() = if (
            context.isFineLocationGranted().not() &&
            context.isCoarseLocationGranted().not()
        )
            null
        else
            serviceState.operatorNumeric

    val roaming: Boolean
        get() = serviceState.roaming

    val voiceRegState: RegState?
        get() = when (serviceState.state) {
            ServiceState.STATE_IN_SERVICE -> RegState.IN_SERVICE
            ServiceState.STATE_OUT_OF_SERVICE -> RegState.OUT_OF_SERVICE
            ServiceState.STATE_EMERGENCY_ONLY -> RegState.EMERGENCY_ONLY
            ServiceState.STATE_POWER_OFF -> RegState.POWER_OFF
            else -> null
        }

    val isSearching: Boolean?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            serviceState.isSearching
        else
            null

    val networkRegistrationInfoList: List<MNetworkRegistrationInfo>
        get() = getMNetworkRegistrationInfoList()

    /**
     * Reflection Scope
     */
    val bitmaskHasTech: Boolean?
        get() = reflection?.bitmaskHasTech

    val cdmaDefaultRoamingIndicator: Int?
        get() = reflection?.cdmaDefaultRoamingIndicator

    val cdmaEriIconIndex: Int?
        get() = reflection?.cdmaEriIconIndex

    val cdmaEriIconMode: Int?
        get() = reflection?.cdmaEriIconMode

    val cdmaRoamingIndicator: Int?
        get() = reflection?.cdmaRoamingIndicator

    val configRadioTechnology: Int?
        get() = reflection?.configRadioTechnology

    val dataRegState: RegState?
        get() = when (reflection?.dataRegState) {
            ServiceState.STATE_IN_SERVICE -> RegState.IN_SERVICE
            ServiceState.STATE_OUT_OF_SERVICE -> RegState.OUT_OF_SERVICE
            ServiceState.STATE_EMERGENCY_ONLY -> RegState.EMERGENCY_ONLY
            ServiceState.STATE_POWER_OFF -> RegState.POWER_OFF
            else -> null
        }

    val hwNetworkType: Int?
        get() = reflection?.hwNetworkType

    val networkRegistrationInfo: MNetworkRegistrationInfo?
        get() = reflection?.networkRegistrationInfo?.let {
            MNetworkRegistrationInfo(context, it)
        }

    val networkRegistrationInfoListForDomain: List<MNetworkRegistrationInfo>
        get() = reflection?.networkRegistrationInfoListForDomain?.map {
            MNetworkRegistrationInfo(context, it)
        } ?: emptyList()

    val networkRegistrationInfoListForTransportType: List<MNetworkRegistrationInfo>
        get() = reflection?.networkRegistrationInfoListForTransportType?.map {
            MNetworkRegistrationInfo(context, it)
        } ?: emptyList()

    val nsaState: Int?
        get() = reflection?.nsaState

    val radioTechnology: Int?
        get() = reflection?.radioTechnology

    val rilDataRadioTechnology: Int?
        get() = reflection?.rilDataRadioTechnology

    val rilVoiceRadioTechnology: Int?
        get() = reflection?.rilVoiceRadioTechnology

    val isCdma: Boolean?
        get() = reflection?.isCdma

    val isEmergencyOnly: Boolean?
        get() = reflection?.isEmergencyOnly

    val isGsm: Boolean?
        get() = reflection?.isGsm

    val isHrpd1X: Boolean?
        get() = reflection?.isHrpd1X

    private fun getMNetworkRegistrationInfoList(): List<MNetworkRegistrationInfo> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            return emptyList()

        val list = mutableListOf<MNetworkRegistrationInfo>()

        serviceState.networkRegistrationInfoList.forEach { element ->
            element?.let {
                val networkRegistrationInfo = MNetworkRegistrationInfo(context, it)
                list.add(networkRegistrationInfo)
            }
        }

        return list
    }

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mutableMapOf(
        "cdmaNetworkId" to cdmaNetworkId,
        "cdmaSystemId" to cdmaSystemId,
        "cellBandwidths" to cellBandwidths,
        "channelNumber" to channelNumber,
        "duplexMode" to duplexMode,
        "isManualSelection" to isManualSelection,
        "operatorAlphaLong" to operatorAlphaLong,
        "operatorAlphaShort" to operatorAlphaShort,
        "operatorNumeric" to operatorNumeric,
        "roaming" to roaming,
        "voiceRegState" to voiceRegState?.value,
        "isSearching" to isSearching,
        "networkRegistrationInfoList" to networkRegistrationInfoList.map { it.toString() },
        "bitmaskHasTech" to bitmaskHasTech,
        "cdmaDefaultRoamingIndicator" to cdmaDefaultRoamingIndicator,
        "cdmaEriIconIndex" to cdmaEriIconIndex,
        "cdmaEriIconMode" to cdmaEriIconMode,
        "cdmaRoamingIndicator" to cdmaRoamingIndicator,
        "configRadioTechnology" to configRadioTechnology,
        "dataRegState" to dataRegState?.value,
        "hwNetworkType" to hwNetworkType,
        "networkRegistrationInfo" to networkRegistrationInfo.toString(),
        "networkRegistrationInfoListForDomain" to networkRegistrationInfoListForDomain.map { it.toString() },
        "networkRegistrationInfoListForTransportType" to networkRegistrationInfoListForTransportType.map { it.toString() },
        "nsaState" to nsaState,
        "radioTechnology" to radioTechnology,
        "rilDataRadioTechnology" to rilDataRadioTechnology,
        "rilVoiceRadioTechnology" to rilVoiceRadioTechnology,
        "isCdma" to isCdma,
        "isEmergencyOnly" to isEmergencyOnly,
        "isGsm" to isGsm,
        "isHrpd1X" to isHrpd1X
    )

    private fun Int.orNull() = if (this == ServiceState.UNKNOWN_ID) null else this

    data class ServiceStateReflection(
        @SerializedName("bitmaskHasTech") val bitmaskHasTech: Boolean? = null,
        @SerializedName("getCdmaDefaultRoamingIndicator") val cdmaDefaultRoamingIndicator: Int? = null,
        @SerializedName("getCdmaEriIconIndex") val cdmaEriIconIndex: Int? = null,
        @SerializedName("getCdmaEriIconMode") val cdmaEriIconMode: Int? = null,
        @SerializedName("getCdmaRoamingIndicator") val cdmaRoamingIndicator: Int? = null,
        @SerializedName("getConfigRadioTechnology") val configRadioTechnology: Int? = null,
        @SerializedName("getDataRegState") val dataRegState: Int? = null,
        @SerializedName("getHwNetworkType") val hwNetworkType: Int? = null,
        @SerializedName("getNetworkRegistrationInfo") val networkRegistrationInfo: NetworkRegistrationInfo? = null,
        @SerializedName("getNetworkRegistrationInfoList") val networkRegistrationInfoList: String? = null,
        @SerializedName("getNetworkRegistrationInfoListForDomain") val networkRegistrationInfoListForDomain: List<NetworkRegistrationInfo>? = null,
        @SerializedName("getNetworkRegistrationInfoListForTransportType") val networkRegistrationInfoListForTransportType: List<NetworkRegistrationInfo>? = null,
        @SerializedName("getNsaState") val nsaState: Int? = null,
        @SerializedName("getRadioTechnology") val radioTechnology: Int? = null,
        @SerializedName("getRilDataRadioTechnology") val rilDataRadioTechnology: Int? = null,
        @SerializedName("getRilVoiceRadioTechnology") val rilVoiceRadioTechnology: Int? = null,
        @SerializedName("isCdma") val isCdma: Boolean? = null,
        @SerializedName("isEmergencyOnly") val isEmergencyOnly: Boolean? = null,
        @SerializedName("isGsm") val isGsm: Boolean? = null,
        @SerializedName("isHrpd1X") val isHrpd1X: Boolean? = null
    )
}
