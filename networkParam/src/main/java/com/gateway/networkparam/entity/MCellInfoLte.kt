package com.gateway.networkparam.entity

import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoLte
import com.gateway.networkparam.entity2.enums.MCellConnectionStatus

internal data class MCellInfoLte(
    private val cellInfoLte: CellInfoLte,
) : MCellInfo() {

    val cellIdentity: MCellIdentityLte
        get() = MCellIdentityLte(cellInfoLte.cellIdentity)

    val cellSignalStrengthLte: MCellSignalStrengthLte
        get() = MCellSignalStrengthLte(cellInfoLte.cellSignalStrength)

    override val isRegistered: Boolean
        get() = cellInfoLte.isRegistered

    override val timeStampMillis: Long
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            cellInfoLte.timestampMillis
        else
            cellInfoLte.timeStamp

    override val cellConnectionStatus: MCellConnectionStatus?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            when (cellInfoLte.cellConnectionStatus) {
                CellInfo.CONNECTION_NONE -> MCellConnectionStatus.NONE
                CellInfo.CONNECTION_PRIMARY_SERVING -> MCellConnectionStatus.PRIMARY_SERVING
                CellInfo.CONNECTION_SECONDARY_SERVING -> MCellConnectionStatus.SECONDARY_SERVING
                CellInfo.CONNECTION_UNKNOWN -> MCellConnectionStatus.UNKNOWN
                else -> null
            }
        else null
}
