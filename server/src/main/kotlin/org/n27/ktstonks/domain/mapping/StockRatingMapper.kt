package org.n27.ktstonks.domain.mapping

import org.n27.ktstonks.domain.model.Rating

internal object StockRatingMapper {

    fun toPeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 20 && value <= 25 -> Rating.CAUTION
        value > 25 -> Rating.WARNING
        else -> null
    }

    fun toDeRating(value: Double): Rating? = when {
        value < 0.3 -> Rating.POSITIVE
        value > 0.5 && value <= 1 -> Rating.CAUTION
        value > 1 -> Rating.DANGER
        else -> null
    }

    fun toRoeRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 0 && value < 15 -> Rating.CAUTION
        value > 20 -> Rating.POSITIVE
        else -> null
    }

    fun toProfitMarginRating(value: Double): Rating? = when {
        value < 0 -> Rating.DANGER
        value > 0 && value < 10 -> Rating.CAUTION
        value > 20 -> Rating.POSITIVE
        else -> null
    }

    fun toPegRating(value: Double): Rating? = when {
        value > 2 -> Rating.CAUTION
        else -> null
    }

    fun toDynamicPaybackRating(value: Double): Rating? = when {
        value < 10 -> Rating.POSITIVE
        value > 15 && value <= 20 -> Rating.CAUTION
        value > 20 -> Rating.DANGER
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
