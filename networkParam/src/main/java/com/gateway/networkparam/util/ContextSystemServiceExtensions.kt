package com.gateway.networkparam.util

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager

/**
 * Gives the telephony manager
 * @return the @code{[TelephonyManager]}
 */
internal fun Context.telephonyManager() =
    getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

/**
 * Gives the subscription manager
 * @return the @code{[SubscriptionManager]}
 */
internal fun Context.subscriptionManager() =
    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
