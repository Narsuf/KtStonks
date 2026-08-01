package org.n27.ktstonks.data.yfinance.model

import kotlinx.serialization.Serializable

@Serializable
data class StockRaw(
    val symbol: String,
    val companyName: String = "",
    val logoUrl: String? = null,
    val price: Double? = null,
    val dividendYield: Double? = null,
    val eps: Double? = null,
    val pe: Double? = null,
    val earningsQuarterlyGrowth: Double? = null,
    val earningsEstimateGrowthHigh: Double? = null,
    val currency: String? = null,
    val roe: Double? = null,
    val profitMargin: Double? = null,
    val totalCashPerShare: Double? = null,
    val de: Double? = null,
)
