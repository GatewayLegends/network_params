package com.gateway.networkparam.entity

import android.telephony.CellInfo

internal abstract class MCellSignalStrength {

    abstract val asuLevel: Int?

    abstract val dbm: Int?

    abstract val level: Int?

    protected fun Int.orNull() = if (this == CellInfo.UNAVAILABLE) null else this

    protected fun Long.orNull() = if (this == CellInfo.UNAVAILABLE_LONG) null else this
}
