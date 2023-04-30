package com.gateway.networkparam.entity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.*
import com.gateway.networkparam.entity2.enums.Domain
import com.gateway.networkparam.entity2.enums.NetworkType
import com.gateway.networkparam.entity2.enums.ServiceType
import com.gateway.networkparam.entity2.enums.TransportType
import com.gateway.networkparam.util.*
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

internal class MNetworkRegistrationInfo(
    private val context: Context,
    private val networkRegistrationInfo: NetworkRegistrationInfo
) {

    private var reflection: NetworkRegistrationInfoReflection? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val gson = GsonBuilder().serializeNulls().create()
            val reflectionJson = networkRegistrationInfo.reflectAsJson()
            reflection = gson.fromJson(
                reflectionJson.toString(),
                NetworkRegistrationInfoReflection::class.java
            )
        }
    }

    val domain: Domain?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.domain.toDomain()
        else
            null

    val accessNetworkTechnology: NetworkType?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.accessNetworkTechnology.toNetworkType()
        else
            null

    val registeredPlmn: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.registeredPlmn
        else
            null

    val availableServices: List<ServiceType?>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.availableServices.map { it.toServiceType() }
        else
            emptyList()

    val cellIdentity: MCellIdentityLte?
        @SuppressLint("MissingPermission")
        get() = if (context.isFineLocationGranted().not())
            null
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.cellIdentity?.getLteOrNull()
        else
            null

    val transportType: TransportType?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.transportType.toTransportType()
        else
            null

    val isRegistered: Boolean?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.isRegistered
        else
            null

    val isSearching: Boolean?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.isSearching
        else
            null

    val isRoaming: Boolean?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            networkRegistrationInfo.isRoaming
        else
            null

    /**
     * Reflection Scope
     */
    val configRadioTechnology: Int?
        get() = reflection?.configRadioTechnology

    val nsaState: Int?
        get() = reflection?.nsaState

    val registrationState: Int?
        get() = reflection?.registrationState

    val rejectCause: Int?
        get() = reflection?.rejectCause

    val roamingType: Int?
        get() = reflection?.roamingType

    val isEmergencyEnabled: Boolean?
        get() = reflection?.isEmergencyEnabled

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mutableMapOf(
        "domain" to domain?.value,
        "accessNetworkTechnology" to accessNetworkTechnology?.value,
        "registeredPlmn" to registeredPlmn,
        "availableServices" to availableServices.map { it?.value.toString() },
        "cellIdentity" to cellIdentity.toString(),
        "transportType" to transportType?.value,
        "isRegistered" to isRegistered,
        "isSearching" to isSearching,
        "isRoaming" to isRoaming,
        "configRadioTechnology" to configRadioTechnology,
        "nsaState" to nsaState,
        "registrationState" to registrationState,
        "rejectCause" to rejectCause,
        "roamingType" to roamingType,
        "isEmergencyEnabled" to isEmergencyEnabled
    )

    private fun CellIdentity.getLteOrNull() =
        if (this is CellIdentityLte) MCellIdentityLte(this) else null

    data class NetworkRegistrationInfoReflection(
        @SerializedName("getConfigRadioTechnology") val configRadioTechnology: Int,
        @SerializedName("getNsaState") val nsaState: Int,
        @SerializedName("getRegistrationState") val registrationState: Int,
        @SerializedName("getRejectCause") val rejectCause: Int,
        @SerializedName("getRoamingType") val roamingType: Int,
        @SerializedName("isEmergencyEnabled") val isEmergencyEnabled: Boolean
    )
}
