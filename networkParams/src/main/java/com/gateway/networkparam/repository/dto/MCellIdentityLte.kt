package com.gateway.networkparam.repository.dto

import android.os.Build
import android.telephony.CellIdentityLte
import com.gateway.networkparam.entity.util.toJson

internal open class MCellIdentityLte(
    private val cellIdentityLte: CellIdentityLte
) : MCellIdentity() {

    val mcc: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.mccString
        else
            cellIdentityLte.mcc.orNull().toString()

    val mnc: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.mncString
        else
            cellIdentityLte.mnc.orNull().toString()

    val networkOperator: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.mobileNetworkOperator
        else
            null

    val ci: Int?
        get() = cellIdentityLte.ci.orNull()

    val pci: Int?
        get() = cellIdentityLte.pci.orNull()

    val earfcn: Int?
        get() = cellIdentityLte.earfcn.orNull()

    val siteId: Int?
        get() = ci?.let { it / 256 }

    val cellId: Int?
        get() = ci?.let { ci ->
            siteId?.let { siteId ->
                ci - siteId * 256
            }
        }

    val additionalPlmns: List<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            cellIdentityLte.additionalPlmns.toList()
        else
            emptyList()

    val bands: List<Int>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            cellIdentityLte.bands.toList()
        else
            emptyList()

    val bandwidth: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.bandwidth.orNull()
        else
            null

    val tac: Int?
        get() = cellIdentityLte.tac.orNull()

    val operatorAlphaShort: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.operatorAlphaShort.toString()
        else
            null

    val operatorAlphaLong: String?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            cellIdentityLte.operatorAlphaLong.toString()
        else
            null

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mutableMapOf<String, Any?>(
        "mcc" to mcc,
        "mnc" to mnc,
        "networkOperator" to networkOperator,
        "ci" to ci,
        "pci" to pci,
        "earfcn" to earfcn,
        "siteId" to siteId,
        "cellId" to cellId,
        "additionalPlmns" to additionalPlmns.toString(),
        "bands" to bands.toString(),
        "bandwidth" to bandwidth,
        "tac" to tac,
        "operatorAlphaShort" to operatorAlphaShort,
        "operatorAlphaLong" to operatorAlphaLong,
    )
}
