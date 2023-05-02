package com.gateway.networkparam.repository.dto

import android.os.Build
import android.telephony.CellSignalStrengthLte
import com.gateway.networkparam.entity.util.reflectAsJson
import com.gateway.networkparam.entity.util.toJson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.util.*

internal class MCellSignalStrengthLte(
    private val cellSignalStrengthLte: CellSignalStrengthLte
) : MCellSignalStrength() {

    private var reflection: CellSignalStrengthLteReflection

    init {
        val gson = GsonBuilder().serializeNulls().create()
        val reflectionJson = cellSignalStrengthLte.reflectAsJson()
        reflection =
            gson.fromJson(reflectionJson.toString(), CellSignalStrengthLteReflection::class.java)
    }

    val rssi: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            cellSignalStrengthLte.rssi.orNull()
        else
            null

    val rsrp: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            cellSignalStrengthLte.rsrp.orNull()
        else
            null

    val rsrq: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            cellSignalStrengthLte.rsrq.orNull()
        else
            null

    val rssnr: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            cellSignalStrengthLte.rssnr.orNull()
        else
            null

    val cqi: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            cellSignalStrengthLte.cqi.orNull()
        else
            null

    val cqiTableIndex: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            cellSignalStrengthLte.cqiTableIndex.orNull()
        else
            null

    val timingAdvance: Int?
        get() = cellSignalStrengthLte.timingAdvance.orNull()

    override val asuLevel: Int?
        get() = cellSignalStrengthLte.asuLevel.orNull()

    override val dbm: Int?
        get() = cellSignalStrengthLte.dbm.orNull()

    override val level: Int?
        get() = cellSignalStrengthLte.level.orNull()

    val miuiLevel: Int?
        get() = reflection.miuiLevel

    override fun toString() = mapFields().toJson()

    private fun mapFields() = mapOf(
        "rssi" to rssi,
        "rsrp" to rsrp,
        "rsrq" to rsrq,
        "rssnr" to rssnr,
        "cqi" to cqi,
        "cqiTableIndex" to cqiTableIndex,
        "timingAdvance" to timingAdvance,
        "asuLevel" to asuLevel,
        "dbm" to dbm,
        "level" to level,
        "miuiLevel" to miuiLevel
    )

    data class CellSignalStrengthLteReflection(
        @SerializedName("getMiuiLevel") val miuiLevel: Int? = null
    )
}
