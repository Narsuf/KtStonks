package org.n27.ktstonks.domain.mapping

import org.n27.ktstonks.domain.model.MetricValue
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
    dividendYieldVariation: Double? = null,
    eps: Double?,
    epsVariation: Double? = null,
    earningsQuarterlyGrowth: Double?,
    earningsQuarterlyGrowthVariation: Double? = null,
    growthHigh: Double?,
    growthHighVariation: Double? = null,
    pe: Double?,
    peVariation: Double? = null,
    valuationFloor: Double?,
    intrinsicValue: Double?,
    de: Double?,
    deVariation: Double? = null,
    totalCashPerShare: Double?,
    totalCashPerShareVariation: Double? = null,
    roe: Double?,
    roeVariation: Double? = null,
    profitMargin: Double?,
    profitMarginVariation: Double? = null,
) = Stock(
    symbol = symbol,
    companyName = companyName,
    logo = logo,
    price = price,
    dividends = Dividends(
        dividendYield = dividendYield.toMetricValue(dividendYieldVariation),
        payoutRatio = computePayoutRatio(dividendYield, pe).toMetricValue(rating = StockRatingMapper::toPayoutRatioRating),
    ),
    roe = roe.toMetricValue(roeVariation, StockRatingMapper::toRoeRating),
    profitMargin = profitMargin.toMetricValue(profitMarginVariation, StockRatingMapper::toProfitMarginRating),
    incomeStatement = IncomeStatement(
        eps = eps.toMetricValue(epsVariation),
        earningsQuarterlyGrowth = earningsQuarterlyGrowth.toMetricValue(earningsQuarterlyGrowthVariation),
    ),
    earningsEstimate = growthHigh.toMetricValue(growthHighVariation, StockRatingMapper::toForwardEarningsGrowthRating),
    valuationMeasures = ValuationMeasures(
        pe = pe.toMetricValue(peVariation, StockRatingMapper::toPeRating),
        valuationFloor = valuationFloor,
        intrinsicValue = intrinsicValue,
    ),
    balanceSheet = BalanceSheet(
        totalCashPerShare = totalCashPerShare.toMetricValue(totalCashPerShareVariation),
        de = de.toMetricValue(deVariation, StockRatingMapper::toDeRating),
    ),
    computed = Computed(
        earningsYield = computeEarningsYield(pe),
        dynamicPayback = computeDynamicPayback(price, eps, growthHigh),
    ),
    currency = currency,
    lastUpdated = lastUpdated,
    isWatchlisted = isWatchlisted,
)

private fun Double?.toMetricValue(variation: Double? = null, rating: (Double) -> Rating? = { null }) =
    this?.let { MetricValue(it, rating(it), variation) }

internal fun computePayoutRatio(dividendYield: Double?, pe: Double?): Double? =
    dividendYield?.let { yield -> pe?.let { yield * it } }

internal fun computeEarningsYield(pe: Double?) = pe
    ?.takeIf { it != 0.0 }
    ?.let { ((1.0 / it) * 100).toMetricValue(rating = StockRatingMapper::toEarningsYieldRating) }

internal fun computeDynamicPayback(price: Double?, eps: Double?, growth: Double?): MetricValue? {
    if (price == null || eps == null || growth == null || eps <= 0 || price <= 0) return null
    val g = growth / 100
    if (g <= -1) return null
    if (g == 0.0) return (price / eps).toMetricValue()
    val argument = 1 + price * g / eps
    // With negative growth, earnings sum to at most eps / |g|; if that is below the price it never pays back.
    if (argument <= 0) return null
    return (ln(argument) / ln(1 + g)).toMetricValue()
}
