package com.gateway.networkparam.framework.util

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.*
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.repository.dto.MCellSignalStrengthLte
import com.gateway.networkparam.repository.util.toEntity
import com.gateway.networkparam.repository.util.toImprovedLte
import com.gateway.networkparam.repository.util.toLte
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import kotlin.coroutines.resume


internal fun TelephonyManager.getSubTelephonyManager(subId: Int): TelephonyManager =
    createForSubscriptionId(subId)

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE])
internal suspend fun TelephonyManager.getCellLte(executor: Executor) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        getUpdatedCellLte(executor)
    else
        getCachedCellLte()

@RequiresApi(Build.VERSION_CODES.Q)
internal fun TelephonyManager.getSignalStrengthLte() =
    signalStrength?.cellSignalStrengths?.find {
        it.toLte() != null
    }?.let(CellSignalStrength::toLte)
        ?.let(::MCellSignalStrengthLte)
        ?.toEntity(networkOperator)

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE])
internal suspend fun TelephonyManager.getUpdatedCellLte(
    executor: Executor,
    updates: Int = 10,
    updateIntervalMillis: Long = 500L
): List<CellLte> = suspendCancellableCoroutine { continuation ->
    var updateCounter = 0
    var networkOperator: String? = null
    val delayHandler = Handler(Looper.getMainLooper())
    val cellInfoCallback = object : TelephonyManager.CellInfoCallback() {
        var cellsLte = listOf<CellLte>()
        @SuppressLint("MissingPermission")
        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
            cellsLte = cellInfo.toImprovedLte(
                defaultNetworkOperator = { networkOperator },
                onNetworkOperator = { networkOperator = it }
            )

            if (updateCounter < updates)
                delayHandler.postDelayed({
                    requestCellInfoUpdate(executor, this)
                    updateCounter++
                }, updateIntervalMillis)
            else
                continuation.resume(cellsLte)
        }
    }

    continuation.invokeOnCancellation {
        delayHandler.removeCallbacksAndMessages(null)
    }

    requestCellInfoUpdate(executor, cellInfoCallback)
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE])
internal fun TelephonyManager.getCachedCellLte(): List<CellLte> {
    var networkOperator: String? = null
    return allCellInfo.toImprovedLte(
        defaultNetworkOperator = { networkOperator },
        onNetworkOperator = { networkOperator = it }
    )
}

private val delayHandlers = mutableListOf<Handler>()

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE])
internal suspend fun TelephonyManager.requestCellLteUpdates(
    executor: Executor,
    updates: Int = 10,
    updateIntervalMillis: Long = 1000L,
    onUpdate: (List<CellLte>) -> Unit
) = suspendCancellableCoroutine { continuation ->
    var updateCounter = 0
    var networkOperator: String? = null
    val delayHandler = Handler(Looper.getMainLooper()).also(delayHandlers::add)
    val cellInfoCallback = object : TelephonyManager.CellInfoCallback() {
        @SuppressLint("MissingPermission")
        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
            val cellsLte = cellInfo.toImprovedLte(
                defaultNetworkOperator = { networkOperator },
                onNetworkOperator = { networkOperator = it }
            )

            if (cellsLte.isNotEmpty())
                onUpdate(cellsLte)

            if (updateCounter < updates)
                delayHandler.postDelayed({
                    requestCellInfoUpdate(executor, this)
                    updateCounter++
                }, updateIntervalMillis)
            else
                continuation.resume(Unit)
        }
    }

    continuation.invokeOnCancellation {
        delayHandler.removeCallbacksAndMessages(null)
        delayHandlers.remove(delayHandler)
    }

    requestCellInfoUpdate(executor, cellInfoCallback)
}

@RequiresApi(Build.VERSION_CODES.Q)
internal fun TelephonyManager.removeCellLteUpdates() = with(delayHandlers) {
    forEach { it.removeCallbacksAndMessages(null) }
    clear()
}
