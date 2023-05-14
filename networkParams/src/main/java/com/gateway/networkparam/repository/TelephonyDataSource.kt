package com.gateway.networkparam.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.SignalStrengthLte

interface TelephonyDataSource {
    @get:RequiresApi(Build.VERSION_CODES.Q)
    val lastCellsLte: List<CellLte>

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun requestCellLteUpdates(
        updates: Int = Int.MAX_VALUE,
        updateIntervalMillis: Long = 1000L,
        onUpdate: (List<CellLte>) -> Unit
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    fun removeCellLteUpdates()

    suspend fun getAllCellLte(): List<CellLte>

    fun getAllSignalStrengthLte(): List<SignalStrengthLte>
}
