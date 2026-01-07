package org.n27.ktstonks.data.yfinance.model

import kotlinx.serialization.Serializable

@Serializable
data class StockRaw(
    val symbol: String,
    val companyName: String,
    val logoUrl: String?,
    val price: Double?,
    val dividendYield: Double?,
    val eps: Double?,
    val pe: Double?,
    val pb: Double?,
    val earningsQuarterlyGrowth: Double?,
    val intrinsicValue: Double?,
    val currency: String?,
)
