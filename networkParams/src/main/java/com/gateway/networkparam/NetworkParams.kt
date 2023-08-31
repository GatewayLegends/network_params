package com.gateway.networkparam

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.SignalStrengthLte
import com.gateway.networkparam.entity.util.NetworkOperator
import com.gateway.networkparam.framework.data.TelephonyDataSourceImp
import com.gateway.networkparam.repository.TelephonyDataSource


class NetworkParams(context: Context): TelephonyDataSource {
    private val telephonyDataSource = TelephonyDataSourceImp(context)

    override val lastCellsLte: List<CellLte>
        get() = telephonyDataSource.lastCellsLte

    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun requestCellLteUpdates(
        networkOperator: NetworkOperator,
        updates: Int,
        updateIntervalMillis: Long,
        onUpdate: (List<CellLte>) -> Unit
    )  {
        telephonyDataSource.requestCellLteUpdates(
            networkOperator = networkOperator,
            updates = updates,
            updateIntervalMillis = updateIntervalMillis,
            onUpdate = onUpdate
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun removeCellLteUpdates() = telephonyDataSource.removeCellLteUpdates()

    override suspend fun getAllCellLte(): List<CellLte> = telephonyDataSource.getAllCellLte()

    override fun getAllSignalStrengthLte(): List<SignalStrengthLte> =
        telephonyDataSource.getAllSignalStrengthLte()
}
