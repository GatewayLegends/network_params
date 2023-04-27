package com.gateway.networkparam.entity

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.ParcelUuid
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import com.gateway.networkparam.entity2.enums.DataRooming
import com.gateway.networkparam.entity2.enums.SubscriptionType
import com.gateway.networkparam.util.toJson
import timber.log.Timber

internal class MSubscriptionInfo(
    private val subscriptionInfo: SubscriptionInfo
) {
    val cardId: Int?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.cardId else null

    val carrierId: Int?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.carrierId else null

    val carrierName: CharSequence?
        get() = subscriptionInfo.carrierName

    val countryIso: String?
        get() = subscriptionInfo.countryIso

    val dataRoaming: DataRooming?
        get() = when (subscriptionInfo.dataRoaming) {
            SubscriptionManager.DATA_ROAMING_DISABLE -> DataRooming.DISABLED
            SubscriptionManager.DATA_ROAMING_ENABLE -> DataRooming.ENABLED
            else -> null
        }

    val displayName: CharSequence?
        get() = subscriptionInfo.displayName

    val groupUuid: ParcelUuid?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.groupUuid else null

    val iccId: String?
        get() = subscriptionInfo.iccId.ifEmpty { null }

    val iconTint: Int
        get() = subscriptionInfo.iconTint

    val isEmbedded: Boolean?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.P) subscriptionInfo.isEmbedded else null

    val isOpportunistic: Boolean?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.isOpportunistic else null

    val mcc: String?
        get() = runCatching {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.mccString
            else subscriptionInfo.mcc.toString()
        }.onFailure(Timber::e).getOrNull()

    val mnc: String?
        get() = runCatching {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) subscriptionInfo.mncString
            else subscriptionInfo.mnc.toString()
        }.onFailure(Timber::e).getOrNull()

    val networkOperator: String?
        get() = if (mcc != null && mnc != null) "$mcc$mnc"
        else null

    val number: String?
        get() = subscriptionInfo.number.ifEmpty { null }

    val simSlotIndex: Int
        get() = subscriptionInfo.simSlotIndex

    val subscriptionId: Int
        get() = subscriptionInfo.subscriptionId

    val subscriptionType: SubscriptionType?
        get() = if (Build.VERSION.SDK_INT >= VERSION_CODES.Q) when (subscriptionInfo.subscriptionType) {
            SubscriptionManager.SUBSCRIPTION_TYPE_REMOTE_SIM -> SubscriptionType.REMOTE_SIM
            SubscriptionManager.SUBSCRIPTION_TYPE_LOCAL_SIM -> SubscriptionType.LOCAL_SIM
            else -> null
        }
        else null

    val isDataActive: Boolean
        get() = subscriptionId == SubscriptionManager.getDefaultDataSubscriptionId()

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mapOf(
        "cardId" to cardId,
        "carrierId" to carrierId,
        "carrierName" to carrierName,
        "countryIso" to countryIso,
        "dataRoaming" to dataRoaming,
        "displayName" to displayName,
        "groupUuid" to groupUuid,
        "iccId" to iccId,
        "iconTint" to iconTint,
        "isEmbedded" to isEmbedded,
        "isOpportunistic" to isOpportunistic,
        "mcc" to mcc,
        "mnc" to mnc,
        "networkOperator" to networkOperator,
        "number" to number,
        "simSlotIndex" to simSlotIndex,
        "subscriptionId" to subscriptionId,
        "subscriptionType" to subscriptionType,
        "isDataActive" to isDataActive
    )
}
