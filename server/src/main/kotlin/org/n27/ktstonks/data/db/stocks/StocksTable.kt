package org.n27.ktstonks.data.db.stocks

import org.jetbrains.exposed.sql.Table

object StocksTable : Table("stocks") {
    val symbol = varchar("symbol", 20)
    val companyName = varchar("company_name", 255)
    val logo = binary("logo", 1024 * 1024).nullable()
    val price = double("price").nullable()
    val dividendYield = double("dividend_yield").nullable()
    val dividendYieldVariation = double("dividend_yield_variation").nullable()
    val eps = double("eps").nullable()
    val epsVariation = double("eps_variation").nullable()
    val pe = double("pe").nullable()
    val peVariation = double("pe_variation").nullable()
    val earningsQuarterlyGrowth = double("earnings_quarterly_growth").nullable()
    val earningsQuarterlyGrowthVariation = double("earnings_quarterly_growth_variation").nullable()
    val earningsEstimateGrowthHigh = double("earnings_estimate_growth_high").nullable()
    val earningsEstimateGrowthHighVariation = double("earnings_estimate_growth_high_variation").nullable()
    val roe = double("roe").nullable()
    val roeVariation = double("roe_variation").nullable()
    val profitMargin = double("profit_margin").nullable()
    val profitMarginVariation = double("profit_margin_variation").nullable()
    val valuationFloor = double("valuation_floor").nullable()
    val intrinsicValue = double("intrinsic_value").nullable()
    val totalCashPerShare = double("total_cash_per_share").nullable()
    val totalCashPerShareVariation = double("total_cash_per_share_variation").nullable()
    val de = double("de").nullable()
    val deVariation = double("de_variation").nullable()
    val currency = varchar("currency", 10).nullable()
    val lastUpdated = long("last_updated")
    val isWatchlisted = bool("is_watchlisted").default(false)

    override val primaryKey = PrimaryKey(symbol)
}
