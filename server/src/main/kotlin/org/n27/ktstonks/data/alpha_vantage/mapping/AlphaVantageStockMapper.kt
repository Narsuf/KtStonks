package org.n27.ktstonks.data.alpha_vantage.mapping

import org.n27.ktstonks.data.alpha_vantage.model.AlphaVantageStock
import org.n27.ktstonks.domain.model.Stock
import java.net.URI

internal fun AlphaVantageStock.toDomainEntity(price: Double?, expectedEpsGrowth: Double?) = Stock(
    symbol = symbol,
    companyName = name,
    logoUrl = getLogoFromWebsite(officialSite),
    price = price,
    dividendYield = dividendYield?.toDoubleOrNull(),
    eps = eps?.toDoubleOrNull(),
    pe = if (price != null && eps != null) price / eps.toDouble() else null,
    earningsQuarterlyGrowth = earningsGrowthYOY?.let { it.toDouble() * 100 },
    expectedEpsGrowth = expectedEpsGrowth,
    currentIntrinsicValue = eps?.toDouble()?.getIntrinsicValue(),
    forwardIntrinsicValue = expectedEpsGrowth?.let { eps?.toDouble()?.getIntrinsicValue(it) },
    currency = currency,
    lastUpdated = System.currentTimeMillis(),
)

private fun Double.getIntrinsicValue(expectedEpsGrowth: Double = 0.0) = expectedEpsGrowth.toMultiplier() * 12.5 * this

private fun Double.toMultiplier(): Double = 100 + this / 100

private fun getLogoFromWebsite(website: String?): String? {
    if (website.isNullOrBlank()) return null
    return try {
        val domain = URI(website).host?.replaceFirst("www.", "") ?: return null
        "https://logo.clearbit.com/$domain"
    } catch (_: Exception) {
        null
    }
}
