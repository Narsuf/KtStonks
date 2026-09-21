package org.n27.ktstonks.domain.mapping

import org.n27.ktstonks.domain.model.Rating

internal object StockRatingMapper {

    fun toPeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 33.3 -> Rating.CAUTION
        else -> null
    }

    fun toDeRating(value: Double): Rating? = when {
        value < 0.3 -> Rating.POSITIVE
        value > 0.5 -> Rating.CAUTION
        else -> null
    }

    fun toRoeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 0 && value < 1 -> Rating.CAUTION
        value > 3 -> Rating.POSITIVE
        else -> null
    }

    fun toEarningsYieldRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 0 && value < 1 -> Rating.CAUTION
        else -> null
    }

    fun toProfitMarginRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 0 && value < 2 -> Rating.CAUTION
        value > 5 -> Rating.POSITIVE
        else -> null
    }

    fun toForwardEarningsGrowthRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value in 10.0..15.0 -> Rating.POSITIVE
        value !in 5.0..15.0 -> Rating.CAUTION
        else -> null
    }

    fun toPayoutRatioRating(value: Double): Rating? = when {
        value in 75.0..90.0 -> Rating.CAUTION
        value > 90 -> Rating.DANGER
        else -> null
    }
}
