package com.gateway.networkparam.util

import android.telephony.*
import com.gateway.networkparam.entity.*
import com.gateway.networkparam.entity2.*
import com.gateway.networkparam.entity2.ECellIdentityLte
import com.gateway.networkparam.entity2.NetworkRegistrationInfo
import com.gateway.networkparam.entity2.ServiceState
import com.gateway.networkparam.entity2.SubscriptionInfo
import com.gateway.networkparam.entity2.enums.*

internal fun MCellInfoLte.toEntity() = CellLte(
    cellConnectionStatus = cellConnectionStatus?.value,
    isRegistered = isRegistered,
    timeStamp = timeStampMillis,
    cellIdentityLte = cellIdentity.toEntity(),
    signalStrengthReport = cellSignalStrengthLte.toEntity()
)

internal fun MCellInfoLte.toEntity(
    networkOperator: String
) = CellLte(
    cellConnectionStatus = cellConnectionStatus?.value,
    isRegistered = isRegistered,
    timeStamp = timeStampMillis,
    cellIdentityLte = cellIdentity.toEntity(networkOperator),
    signalStrengthReport = cellSignalStrengthLte.toEntity(networkOperator)
)

internal fun MCellInfoLte.toEntity(
    isRegistered: Boolean
) = CellLte(
    cellConnectionStatus = cellConnectionStatus?.value,
    isRegistered = isRegistered,
    timeStamp = timeStampMillis,
    cellIdentityLte = cellIdentity.toEntity(),
    signalStrengthReport = cellSignalStrengthLte.toEntity()
)

internal fun MCellSignalStrengthLte.toEntity() = SignalStrengthLte(
    rsrp = rsrp,
    rsrq = rsrq,
    rssi = rssi,
    rssnr = rssnr,
    cqi = cqi,
    cqiTableIndex = cqiTableIndex,
    timingAdvance = timingAdvance,
    dbm = dbm,
    asuLevel = asuLevel,
    level = level,
    miuiLevel = miuiLevel,
    networkOperator = null
)

internal fun MCellSignalStrengthLte.toEntity(
    networkOperator: String
) = SignalStrengthLte(
    rsrp = rsrp,
    rsrq = rsrq,
    rssi = rssi,
    rssnr = rssnr,
    cqi = cqi,
    cqiTableIndex = cqiTableIndex,
    timingAdvance = timingAdvance,
    dbm = dbm,
    asuLevel = asuLevel,
    level = level,
    miuiLevel = miuiLevel,
    networkOperator = networkOperator
)

internal fun MCellIdentityLte.toEntity() = ECellIdentityLte(
    ci = ci,
    earfcn = earfcn,
    pci = pci,
    tac = tac,
    networkOperator = networkOperator,
    additionalPlmns = additionalPlmns,
    bands = bands,
    bandwidth = bandwidth,
    siteId = siteId,
    cellId = cellId
)

internal fun MCellIdentityLte.toEntity(
    networkOperator: String
) = ECellIdentityLte(
    ci = ci,
    earfcn = earfcn,
    pci = pci,
    tac = tac,
    networkOperator = this.networkOperator ?: networkOperator,
    additionalPlmns = additionalPlmns,
    bands = bands,
    bandwidth = bandwidth,
    siteId = siteId,
    cellId = cellId
)

internal fun SubTelephonyManager.toEntity(
    serviceState: ServiceState?,
    signalStrengthLte: SignalStrengthLte?
) = MTelephony(
    networkOperator = networkOperator,
    serviceState = serviceState,
    signalStrength = signalStrengthLte,
    simState = simState?.value,
    callState = callState?.value,
    dataNetworkType = dataNetworkType?.value,
    voiceNetworkType = voiceNetworkType?.value
)

internal fun MServiceState.toEntity() = ServiceState(
    cdmaNetworkId = cdmaNetworkId,
    cdmaSystemId = cdmaSystemId,
    cellBandwidths = cellBandwidths,
    channelNumber = channelNumber,
    duplexMode = duplexMode,
    isManualSelection = isManualSelection,
    operatorAlphaLong = operatorAlphaLong,
    operatorAlphaShort = operatorAlphaShort,
    operatorNumeric = operatorNumeric,
    roaming = roaming,
    voiceRegState = voiceRegState?.value,
    isSearching = isSearching,
    networkRegistrationInfoList = networkRegistrationInfoList.map { it.toEntity() },
    bitmaskHasTech = bitmaskHasTech,
    cdmaDefaultRoamingIndicator = cdmaDefaultRoamingIndicator,
    cdmaEriIconIndex = cdmaEriIconIndex,
    cdmaEriIconMode = cdmaEriIconMode,
    cdmaRoamingIndicator = cdmaRoamingIndicator,
    configRadioTechnology = configRadioTechnology,
    dataRegState = dataRegState?.value,
    hwNetworkType = hwNetworkType,
    networkRegistrationInfo = networkRegistrationInfo?.toEntity(),
    networkRegistrationInfoListForDomain = networkRegistrationInfoListForDomain.map { it.toEntity() },
    networkRegistrationInfoListForTransportType = networkRegistrationInfoListForTransportType.map { it.toEntity() },
    nsaState = nsaState,
    radioTechnology = radioTechnology,
    rilDataRadioTechnology = rilDataRadioTechnology,
    rilVoiceRadioTechnology = rilVoiceRadioTechnology,
    isCdma = isCdma,
    isEmergencyOnly = isEmergencyOnly,
    isGsm = isGsm,
    isHrpd1X = isHrpd1X
)

internal fun MNetworkRegistrationInfo.toEntity() = NetworkRegistrationInfo(
    domain = domain?.value,
    accessNetworkTechnology = accessNetworkTechnology?.value,
    registeredPlmn = registeredPlmn,
    availableServices = availableServices.map { it?.value },
    cellIdentity = cellIdentity?.toEntity(),
    transportType = transportType?.value,
    isRegistered = isRegistered,
    isSearching = isSearching,
    isRoaming = isRoaming,
    configRadioTechnology = configRadioTechnology,
    nsaState = nsaState,
    registrationState = registrationState,
    rejectCause = rejectCause,
    roamingType = roamingType,
    isEmergencyEnabled = isEmergencyEnabled,
)

internal fun MSubscriptionInfo.toEntity() = SubscriptionInfo(
    cardId = cardId,
    carrierId = carrierId,
    carrierName = carrierName.toString(),
    countryIso = countryIso,
    dataRoaming = dataRoaming?.value,
    displayName = displayName.toString(),
    groupUuid = groupUuid,
    iccId = iccId,
    iconTint = iconTint,
    isEmbedded = isEmbedded,
    isOpportunistic = isOpportunistic,
    mcc = mcc,
    mnc = mnc,
    networkOperator = networkOperator,
    number = number,
    simSlotIndex = simSlotIndex,
    subscriptionId = subscriptionId,
    subscriptionType = subscriptionType?.value,
    isDataActive = isDataActive
)

internal fun Int.toCallState() = when (this) {
    TelephonyManager.CALL_STATE_IDLE -> CallState.IDLE
    TelephonyManager.CALL_STATE_RINGING -> CallState.RINGING
    TelephonyManager.CALL_STATE_OFFHOOK -> CallState.OFFHOOK
    else -> null
}

internal fun Int.toNetworkType() = when (this) {
    TelephonyManager.NETWORK_TYPE_UNKNOWN -> NetworkType.UNKNOWN
    TelephonyManager.NETWORK_TYPE_GPRS -> NetworkType.GPRS
    TelephonyManager.NETWORK_TYPE_EDGE -> NetworkType.EDGE
    TelephonyManager.NETWORK_TYPE_UMTS -> NetworkType.UMTS
    TelephonyManager.NETWORK_TYPE_CDMA -> NetworkType.CDMA
    TelephonyManager.NETWORK_TYPE_EVDO_0 -> NetworkType.EVDO_0
    TelephonyManager.NETWORK_TYPE_EVDO_A -> NetworkType.EVDO_A
    TelephonyManager.NETWORK_TYPE_1xRTT -> NetworkType.RTT
    TelephonyManager.NETWORK_TYPE_HSDPA -> NetworkType.HSDPA
    TelephonyManager.NETWORK_TYPE_HSUPA -> NetworkType.HSUPA
    TelephonyManager.NETWORK_TYPE_HSPA -> NetworkType.HSPA
    TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.IDEN
    TelephonyManager.NETWORK_TYPE_EVDO_B -> NetworkType.EVDO_B
    TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.LTE
    TelephonyManager.NETWORK_TYPE_EHRPD -> NetworkType.EHRPD
    TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.HSPAP
    TelephonyManager.NETWORK_TYPE_GSM -> NetworkType.GSM
    TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkType.TD_SCDMA
    TelephonyManager.NETWORK_TYPE_IWLAN -> NetworkType.IWLAN
    TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NR
    else -> null
}

internal fun Int.toSimState() = when (this) {
    TelephonyManager.SIM_STATE_UNKNOWN -> SimState.SIM_STATE_UNKNOWN
    TelephonyManager.SIM_STATE_ABSENT -> SimState.SIM_STATE_ABSENT
    TelephonyManager.SIM_STATE_PIN_REQUIRED -> SimState.SIM_STATE_PIN_REQUIRED
    TelephonyManager.SIM_STATE_PUK_REQUIRED -> SimState.SIM_STATE_PUK_REQUIRED
    TelephonyManager.SIM_STATE_NETWORK_LOCKED -> SimState.SIM_STATE_NETWORK_LOCKED
    TelephonyManager.SIM_STATE_READY -> SimState.SIM_STATE_READY
    TelephonyManager.SIM_STATE_NOT_READY -> SimState.SIM_STATE_NOT_READY
    TelephonyManager.SIM_STATE_PERM_DISABLED -> SimState.SIM_STATE_PERM_DISABLED
    TelephonyManager.SIM_STATE_CARD_IO_ERROR -> SimState.SIM_STATE_CARD_IO_ERROR
    TelephonyManager.SIM_STATE_CARD_RESTRICTED -> SimState.SIM_STATE_CARD_RESTRICTED
    else -> null
}

internal fun Int.toDomain() = when (this) {
    android.telephony.NetworkRegistrationInfo.DOMAIN_UNKNOWN -> Domain.UNKNOWN
    android.telephony.NetworkRegistrationInfo.DOMAIN_CS -> Domain.CS
    android.telephony.NetworkRegistrationInfo.DOMAIN_PS -> Domain.PS
    android.telephony.NetworkRegistrationInfo.DOMAIN_CS_PS -> Domain.CS_PS
    else -> null
}

internal fun Int.toServiceType() = when (this) {
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_UNKNOWN -> ServiceType.UNKNOWN
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_VOICE -> ServiceType.VOICE
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_DATA -> ServiceType.DATA
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_SMS -> ServiceType.SMS
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_VIDEO -> ServiceType.VIDEO
    android.telephony.NetworkRegistrationInfo.SERVICE_TYPE_EMERGENCY -> ServiceType.EMERGENCY
    else -> null
}

internal fun Int.toTransportType() = when (this) {
    AccessNetworkConstants.TRANSPORT_TYPE_WWAN -> TransportType.WWAN
    AccessNetworkConstants.TRANSPORT_TYPE_WLAN -> TransportType.WLAN
    else -> null
}

fun CellInfo.toLte() = if (this is CellInfoLte) this else null

fun CellSignalStrength.toLte() = if (this is CellSignalStrengthLte) this else null

fun CellIdentity.toLte() = if (this is CellIdentityLte) this else null
