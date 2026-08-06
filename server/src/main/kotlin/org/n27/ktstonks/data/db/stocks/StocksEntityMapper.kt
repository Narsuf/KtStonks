package org.n27.ktstonks.data.db.stocks

import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity.Logo
import org.n27.ktstonks.domain.mapping.mapToStock
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.domain.model.Stocks.Stock
import java.util.*

fun StocksEntity.toStocks() = Stocks(
    items = items.map { it.toStock() },
    nextPage = nextPage
)

fun StockEntity.toStock() = mapToStock(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.bytes?.let { Base64.getEncoder().encodeToString(it) },
    price = price,
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
    dividendYield = dividends.dividendYield.value,
    dividendYieldVariation = dividends.dividendYield.variation,
    eps = incomeStatement.eps.value,
    epsVariation = incomeStatement.eps.variation,
    earningsQuarterlyGrowth = incomeStatement.earningsQuarterlyGrowth.value,
    earningsQuarterlyGrowthVariation = incomeStatement.earningsQuarterlyGrowth.variation,
    growthHigh = earningsEstimate.growthHigh.value,
    growthHighVariation = earningsEstimate.growthHigh.variation,
    pe = valuationMeasures.pe.value,
    peVariation = valuationMeasures.pe.variation,
    valuationFloor = valuationMeasures.valuationFloor,
    intrinsicValue = valuationMeasures.intrinsicValue,
    de = balanceSheet.de.value,
    deVariation = balanceSheet.de.variation,
    totalCashPerShare = balanceSheet.totalCashPerShare.value,
    totalCashPerShareVariation = balanceSheet.totalCashPerShare.variation,
    roe = roe.value,
    roeVariation = roe.variation,
    profitMargin = profitMargin.value,
    profitMarginVariation = profitMargin.variation,
)

fun Stock.toEntity() = StockEntity(
    symbol = symbol,
    companyName = companyName,
    logo = logo?.let { Logo(Base64.getDecoder().decode(it)) },
    price = price,
    dividends = StockEntity.Dividends(
        dividendYield = StockEntity.Metric(value = dividends.dividendYield?.value, variation = dividends.dividendYield?.variation),
    ),
    roe = StockEntity.Metric(value = roe?.value, variation = roe?.variation),
    profitMargin = StockEntity.Metric(value = profitMargin?.value, variation = profitMargin?.variation),
    incomeStatement = StockEntity.IncomeStatement(
        eps = StockEntity.Metric(value = incomeStatement.eps?.value, variation = incomeStatement.eps?.variation),
        earningsQuarterlyGrowth = StockEntity.Metric(
            value = incomeStatement.earningsQuarterlyGrowth?.value,
            variation = incomeStatement.earningsQuarterlyGrowth?.variation,
        ),
    ),
    earningsEstimate = StockEntity.Estimate(
        growthHigh = StockEntity.Metric(value = earningsEstimate?.value, variation = earningsEstimate?.variation),
    ),
    valuationMeasures = StockEntity.ValuationMeasures(
        pe = StockEntity.Metric(value = valuationMeasures.pe?.value, variation = valuationMeasures.pe?.variation),
        valuationFloor = valuationMeasures.valuationFloor,
        intrinsicValue = valuationMeasures.intrinsicValue,
    ),
    balanceSheet = StockEntity.BalanceSheet(
        totalCashPerShare = StockEntity.Metric(
            value = balanceSheet.totalCashPerShare?.value,
            variation = balanceSheet.totalCashPerShare?.variation,
        ),
        de = StockEntity.Metric(value = balanceSheet.de?.value, variation = balanceSheet.de?.variation),
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)
