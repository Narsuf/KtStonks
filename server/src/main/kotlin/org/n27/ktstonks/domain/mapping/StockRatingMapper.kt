package org.n27.ktstonks.domain.mapping

import org.n27.ktstonks.domain.model.Rating

internal object StockRatingMapper {

    fun toPeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 25 -> Rating.CAUTION
        else -> null
    }

    fun toDeRating(value: Double): Rating? = when {
        value < 0.5 -> Rating.POSITIVE
        value >= 2.0 -> Rating.CAUTION
        else -> null
    }

    fun toRoeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value < 5 -> Rating.CAUTION
        value > 15 -> Rating.POSITIVE
        else -> null
    }

    fun toProfitMarginRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value <= 5 -> Rating.CAUTION
        value >= 20 -> Rating.POSITIVE
        else -> null
    }

    fun toForwardEarningsGrowthRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        else -> null
    }

    fun toPayoutRatioRating(value: Double): Rating? = when {
        value in 75.0..90.0 -> Rating.CAUTION
        value > 90 && value <= 100 -> Rating.WARNING
        value > 100 -> Rating.DANGER
        else -> null
    }

    fun toDynamicPaybackRating(value: Double): Rating? = when {
        value > 10 -> Rating.CAUTION
        else -> null
    }
}
