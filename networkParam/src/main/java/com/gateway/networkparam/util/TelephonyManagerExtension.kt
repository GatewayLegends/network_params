package com.gateway.networkparam.util

import android.Manifest
import android.os.Build
import android.telephony.*
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.gateway.networkparam.entity.MCellInfoLte
import com.gateway.networkparam.entity.MCellSignalStrengthLte
import com.gateway.networkparam.entity2.CellLte
import kotlinx.coroutines.delay
import java.util.concurrent.Executor

fun TelephonyManager.getSubTelephonyManager(subId: Int): TelephonyManager =
    createForSubscriptionId(subId)

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
suspend fun TelephonyManager.getCellLte(executor: Executor) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        getUpdatedCellLte(executor)
    else
        getCachedCellLte()

@RequiresApi(Build.VERSION_CODES.Q)
fun TelephonyManager.getSignalStrengthLte() =
    signalStrength?.cellSignalStrengths?.find {
        it.toLte() != null
    }?.let(CellSignalStrength::toLte)
        ?.let(::MCellSignalStrengthLte)
        ?.toEntity(networkOperator)

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@RequiresApi(Build.VERSION_CODES.Q)
suspend fun TelephonyManager.getUpdatedCellLte(executor: Executor): List<CellLte> {
    var isRegistered: Boolean
    var isCompleted = true
    var networkOperator: String? = null
    val cellLte = mutableListOf<CellLte>()
    val cellInfoCallback = object : TelephonyManager.CellInfoCallback() {
        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
            isCompleted = false
            cellLte.clear()
            cellInfo.forEach {
                it.toLte()?.let { lteCell ->
                    val mLteCell = MCellInfoLte(lteCell)

                    if (mLteCell.isRegistered.isTrue || (mLteCell.cellIdentity.ci != null && mLteCell.cellIdentity.ci != 0)) {
                        networkOperator = mLteCell.cellIdentity.networkOperator
                        isRegistered = true
                    } else {
                        isRegistered = false
                    }

                    if (mLteCell.cellIdentity.networkOperator == null) {
                        val entityWithPinnedMccMnc = networkOperator?.let { networkOperator ->
                            mLteCell.toEntity(networkOperator = networkOperator)
                        }
                        entityWithPinnedMccMnc?.let { report ->
                            cellLte.add(report)
                        }
                    } else if (isRegistered) {
                        cellLte.add(mLteCell.toEntity(isRegistered = true))
                    } else cellLte.add(mLteCell.toEntity())
                }
            }
            isCompleted = true
        }
    }

    var tries = 0
    while (tries < 10) {
        if (isCompleted) {
            requestCellInfoUpdate(executor, cellInfoCallback)

            tries++
        }
        delay(500)
    }

    return cellLte
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun TelephonyManager.getCachedCellLte(): List<CellLte> {
    val list = mutableListOf<CellLte>()

    var networkOperator: String? = null
    var isRegistered: Boolean

    allCellInfo.forEach { cell ->
        cell.toLte()?.let { lteCell ->
            val mLteCell = MCellInfoLte(lteCell)

            if (mLteCell.isRegistered.isTrue || (mLteCell.cellIdentity.ci != null && mLteCell.cellIdentity.ci != 0)) {
                networkOperator = mLteCell.cellIdentity.networkOperator
                isRegistered = true
            } else {
                isRegistered = false
            }

            if (mLteCell.cellIdentity.networkOperator == null) {
                val entityWithCustomMccMnc = networkOperator?.let {
                    mLteCell.toEntity(networkOperator = it)
                }
                entityWithCustomMccMnc?.let {
                    list.add(it)
                }
            } else if (isRegistered) {
                list.add(mLteCell.toEntity(isRegistered = true))
            } else list.add(mLteCell.toEntity())
        }
    }

    return list
}
