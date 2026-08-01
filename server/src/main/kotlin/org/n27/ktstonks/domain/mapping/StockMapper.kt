package org.n27.ktstonks.domain.mapping

import org.n27.ktstonks.domain.model.RatedValue
import org.n27.ktstonks.domain.model.Rating
import org.n27.ktstonks.domain.model.Stocks.*
import kotlin.math.ln

internal fun mapToStock(
    symbol: String,
    companyName: String,
    logo: String?,
    price: Double?,
    currency: String?,
    lastUpdated: Long,
    isWatchlisted: Boolean,
    dividendYield: Double?,
    eps: Double?,
    earningsQuarterlyGrowth: Double?,
    growthHigh: Double?,
    pe: Double?,
    valuationFloor: Double?,
    intrinsicValue: Double?,
    de: Double?,
    totalCashPerShare: Double?,
    roe: Double?,
    profitMargin: Double?,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = Dividends(
        dividendYield = dividendYield,
        payoutRatio = computePayoutRatio(dividendYield, pe).toRatedValue(StockRatingMapper::toPayoutRatioRating),
    ),
    roe = roe.toRatedValue(StockRatingMapper::toRoeRating),
    profitMargin = profitMargin.toRatedValue(StockRatingMapper::toProfitMarginRating),
    incomeStatement = IncomeStatement(
        eps = eps,
        earningsQuarterlyGrowth = earningsQuarterlyGrowth,
    ),
    earningsEstimate = growthHigh.toRatedValue(StockRatingMapper::toForwardEarningsGrowthRating),
    valuationMeasures = ValuationMeasures(
        pe = pe.toRatedValue(StockRatingMapper::toPeRating),
        valuationFloor = valuationFloor,
        intrinsicValue = intrinsicValue,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = totalCashPerShare,
        de = de.toRatedValue(StockRatingMapper::toDeRating),
    ),
    computed = Computed(
        earningsYield = computeEarningsYield(pe),
        peg = computePeg(pe, growthHigh),
        dynamicPayback = computeDynamicPayback(price, eps, growthHigh),
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

private fun Double?.toRatedValue(rating: (Double) -> Rating?) = this?.let { RatedValue(it, rating(it)) }

internal fun computePayoutRatio(dividendYield: Double?, pe: Double?): Double? =
    dividendYield?.let { yield -> pe?.let { yield * it } }

internal fun computeEarningsYield(pe: Double?) = pe
    ?.takeIf { it != 0.0 }
    ?.let { (1.0 / it) * 100 }

internal fun computePeg(pe: Double?, growth: Double?) = pe?.let { p ->
    growth
        ?.takeIf { it > 0 }
        ?.let { (p / it).toRatedValue(StockRatingMapper::toPegRating) }
}

internal fun computeDynamicPayback(price: Double?, eps: Double?, growth: Double?): RatedValue? {
    if (price == null || eps == null || growth == null || eps <= 0 || growth <= 0) return null
    val g = growth / 100
    val numerator = ln(1 + price * g / eps)
    val denominator = ln(1 + g)
    return (numerator / denominator)
        .takeIf { numerator > 0 }
        .toRatedValue(StockRatingMapper::toDynamicPaybackRating)
}
