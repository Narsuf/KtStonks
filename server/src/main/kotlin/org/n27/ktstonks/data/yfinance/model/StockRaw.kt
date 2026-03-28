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
    val earningsQuarterlyGrowth: Double?,
    val revenueQuarterlyGrowth: Double?,
    val revenueEstimateGrowthLow: Double?,
    val revenueEstimateGrowthHigh: Double?,
    val earningsEstimateGrowthLow: Double?,
    val earningsEstimateGrowthHigh: Double?,
    val currency: String?,
    val roe: Double?,
    val profitMargin: Double?,
    val totalCashPerShare: Double?,
    val de: Double?,
    val payoutRatio: Double?,
)
