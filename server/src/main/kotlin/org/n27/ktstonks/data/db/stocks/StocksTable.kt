package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.Table

object StocksTable : Table("stocks") {
    val symbol = varchar("symbol", 20)
    val companyName = varchar("company_name", 255)
    val logo = binary("logo", 1024 * 1024).nullable()
    val price = double("price").nullable()
    val dividendYield = double("dividend_yield").nullable()
    val eps = double("eps").nullable()
    val pe = double("pe").nullable()
    val pb = double("pb").nullable()
    val ps = double("ps").nullable()
    val earningsQuarterlyGrowth = double("earnings_quarterly_growth").nullable()
    val revenueQuarterlyGrowth = double("revenue_quarterly_growth").nullable()
    val revenueEstimateGrowthLow = double("revenue_estimate_growth_low").nullable()
    val revenueEstimateGrowthHigh = double("revenue_estimate_growth_high").nullable()
    val earningsEstimateGrowthLow = double("earnings_estimate_growth_low").nullable()
    val earningsEstimateGrowthHigh = double("earnings_estimate_growth_high").nullable()
    val valuationFloor = double("valuation_floor").nullable()
    val intrinsicValue = double("intrinsic_value").nullable()
    val currency = varchar("currency", 10).nullable()
    val lastUpdated = long("last_updated")
    val isWatchlisted = bool("is_watchlisted").default(false)

    override val primaryKey = PrimaryKey(symbol)
}
