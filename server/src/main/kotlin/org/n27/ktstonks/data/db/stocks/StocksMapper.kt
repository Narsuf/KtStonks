package org.n27.ktstonks.data.db.stocks

import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock
import java.util.*

fun StocksEntity.toStocks() = Stocks(
    items = items.map { it.toStock() },
    nextPage = nextPage
)

fun StockEntity.toStock() = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.bytes?.let { Base64.getEncoder().encodeToString(it) },
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = expectedEpsGrowth,
    valuationFloor = valuationFloor,
    currentIntrinsicValue = currentIntrinsicValue,
    forwardIntrinsicValue = forwardIntrinsicValue,
    currency = currency,
    lastUpdated = lastUpdated,
)

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { StockEntity.Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividendYield = dividendYield,
    eps = eps,
    pe = pe,
    pb = pb,
    earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    expectedEpsGrowth = expectedEpsGrowth,
    valuationFloor = valuationFloor,
    currentIntrinsicValue = currentIntrinsicValue,
    forwardIntrinsicValue = forwardIntrinsicValue,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = false,
)
