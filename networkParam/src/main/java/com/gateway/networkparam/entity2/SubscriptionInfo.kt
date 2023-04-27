package com.gateway.networkparam.entity2

import android.os.ParcelUuid

data class SubscriptionInfo(
    val cardId: Int? = null,
    val carrierId: Int? = null,
    val carrierName: String? = null,
    val countryIso: String? = null,
    val dataRoaming: String? = null,
    val displayName: String? = null,
    val groupUuid: ParcelUuid? = null,
    val iccId: String? = null,
    val iconTint: Int? = null,
    val isEmbedded: Boolean? = null,
    val isOpportunistic: Boolean? = null,
    val mcc: String? = null,
    val mnc: String? = null,
    val networkOperator: String? = null,
    val number: String? = null,
    val simSlotIndex: Int? = null,
    val subscriptionId: Int? = null,
    val subscriptionType: String? = null,
    val isDataActive: Boolean? = null
)
