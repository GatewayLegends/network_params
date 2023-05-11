package com.gateway.networkparam.framework.util

import android.Manifest
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telephony.*
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.gateway.networkparam.repository.dto.MCellInfoLte
import com.gateway.networkparam.repository.dto.MCellSignalStrengthLte
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.util.isTrue
import com.gateway.networkparam.repository.util.toEntity
import com.gateway.networkparam.repository.util.toLte
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import kotlin.coroutines.resume

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
internal suspend fun TelephonyManager.getUpdatedCellLte(
    executor: Executor,
    updates: Int = 10,
    updateIntervalMillis: Long = 500L
): List<CellLte> = suspendCancellableCoroutine { continuation ->
    var updateCounter = 0
    var isRegistered: Boolean
    var networkOperator: String? = null
    val cellsLte = mutableListOf<CellLte>()
    val delayHandler = Handler(Looper.getMainLooper())
    val cellInfoCallback = object : TelephonyManager.CellInfoCallback() {
        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
            if (updateCounter != updates) return

            cellInfo.map { it.toLte() }.forEach {
                if (it == null) return@forEach
                val cell = MCellInfoLte(it)

                isRegistered =
                    if (cell.isRegistered.isTrue || cell.cellIdentity.ci !in arrayOf(null, 0))
                        true.also { networkOperator = cell.cellIdentity.networkOperator }
                    else false

                if (cell.cellIdentity.networkOperator == null)
                    networkOperator?.run { cellsLte.add(cell.toEntity(networkOperator = this)) }
                else if (isRegistered)
                    cellsLte.add(cell.toEntity(isRegistered = true))
                else
                    cellsLte.add(cell.toEntity())
            }
        }
    }

    fun scheduleNextUpdate() {
        if (updateCounter < updates)
            delayHandler.postDelayed({
                requestCellInfoUpdate(executor, cellInfoCallback)
                updateCounter++
            }, updateIntervalMillis)
        else
            continuation.resume(cellsLte)
    }

    continuation.invokeOnCancellation {
        delayHandler.removeCallbacksAndMessages(null)
    }

    scheduleNextUpdate()
}

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
internal fun TelephonyManager.getCachedCellLte(): List<CellLte> {
    var isRegistered: Boolean
    var networkOperator: String? = null
    val cellsLte = mutableListOf<CellLte>()

    allCellInfo.map { it.toLte() }.forEach {
        if (it == null) return@forEach
        val cell = MCellInfoLte(it)

        isRegistered =
            if (cell.isRegistered.isTrue || cell.cellIdentity.ci !in arrayOf(null, 0))
                true.also { networkOperator = cell.cellIdentity.networkOperator }
            else false

        if (cell.cellIdentity.networkOperator == null)
            networkOperator?.run { cellsLte.add(cell.toEntity(networkOperator = this)) }
        else if (isRegistered)
            cellsLte.add(cell.toEntity(isRegistered = true))
        else
            cellsLte.add(cell.toEntity())
    }

    return cellsLte
}

private val delayHandlers = mutableListOf<Handler>()

@RequiresApi(Build.VERSION_CODES.Q)
@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
internal suspend fun TelephonyManager.requestCellLteUpdates(
    executor: Executor,
    updates: Int = 10,
    updateIntervalMillis: Long = 1000L,
    onUpdate: (List<CellLte>) -> Unit
) = suspendCancellableCoroutine { continuation ->
    var updateCounter = 0
    var isRegistered: Boolean
    var networkOperator: String? = null
    val delayHandler = Handler(Looper.getMainLooper()).also(delayHandlers::add)
    val cellInfoCallback = object : TelephonyManager.CellInfoCallback() {
        override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
            val cellsLte = mutableListOf<CellLte>()
            cellInfo.map { it.toLte() }.forEach {
                if (it == null) return@forEach
                val cell = MCellInfoLte(it)

                isRegistered =
                    if (cell.isRegistered.isTrue || cell.cellIdentity.ci !in arrayOf(null, 0))
                        true.also { networkOperator = cell.cellIdentity.networkOperator }
                    else false

                if (cell.cellIdentity.networkOperator == null)
                    networkOperator?.run { cellsLte.add(cell.toEntity(networkOperator = this)) }
                else if (isRegistered)
                    cellsLte.add(cell.toEntity(isRegistered = true))
                else
                    cellsLte.add(cell.toEntity())
            }

            if (cellsLte.isNotEmpty())
                onUpdate(cellsLte)
        }
    }

    fun scheduleNextUpdate() {
        if (updateCounter < updates)
            delayHandler.postDelayed({
                requestCellInfoUpdate(executor, cellInfoCallback)
                updateCounter++
            }, updateIntervalMillis)
        else
            continuation.resume(Unit)
    }

    continuation.invokeOnCancellation {
        delayHandler.removeCallbacksAndMessages(null)
        delayHandlers.remove(delayHandler)
    }

    scheduleNextUpdate()
}

@RequiresApi(Build.VERSION_CODES.Q)
internal fun TelephonyManager.removeCellLteUpdates() = with(delayHandlers) {
    forEach { it.removeCallbacksAndMessages(null) }
    clear()
}
