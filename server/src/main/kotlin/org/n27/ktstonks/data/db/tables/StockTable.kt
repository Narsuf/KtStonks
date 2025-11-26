package org.n27.ktstonks.data.db.tables

import org.jetbrains.exposed.sql.Table

object StockTable : Table("stocks") {
    val symbol = varchar("symbol", 20)
    val companyName = varchar("company_name", 255)
    val logoUrl = varchar("logo_url", 2048).nullable()
    val price = double("price").nullable()
    val dividendYield = double("dividend_yield").nullable()
    val eps = double("eps").nullable()
    val pe = double("pe").nullable()
    val earningsQuarterlyGrowth = double("earnings_quarterly_growth").nullable()
    val expectedEpsGrowth = double("expected_eps_growth").nullable()
    val currentIntrinsicValue = double("current_intrinsic_value").nullable()
    val forwardIntrinsicValue = double("forward_intrinsic_value").nullable()
    val currency = varchar("currency", 10).nullable()
    val lastUpdated = long("last_updated")

    override val primaryKey = PrimaryKey(symbol)
}
