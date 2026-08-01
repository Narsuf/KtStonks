package org.n27.ktstonks.domain.mapping

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.ktstonks.domain.model.Rating
import kotlin.test.assertEquals

class StockRatingMapperTest {

    @ParameterizedTest(name = "pe={0} → {1}")
    @MethodSource("peRatingCases")
    fun `pe rating`(pe: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toPeRating(pe))
    }

    @ParameterizedTest(name = "de={0} → {1}")
    @MethodSource("deRatingCases")
    fun `de rating`(de: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toDeRating(de))
    }

    @ParameterizedTest(name = "roe={0} → {1}")
    @MethodSource("roeRatingCases")
    fun `roe rating`(roe: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toRoeRating(roe))
    }

    @ParameterizedTest(name = "profitMargin={0} → {1}")
    @MethodSource("profitMarginRatingCases")
    fun `profitMargin rating`(profitMargin: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toProfitMarginRating(profitMargin))
    }

    @ParameterizedTest(name = "growthHigh={0} → {1}")
    @MethodSource("earningsEstimateRatingCases")
    fun `earningsEstimate rating`(growthHigh: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toForwardEarningsGrowthRating(growthHigh))
    }

    @ParameterizedTest(name = "peg={0} → {1}")
    @MethodSource("pegRatingCases")
    fun `peg rating`(peg: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toPegRating(peg))
    }

    @ParameterizedTest(name = "dynamicPayback={0} → {1}")
    @MethodSource("dynamicPaybackRatingCases")
    fun `dynamicPayback rating`(dynamicPayback: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toDynamicPaybackRating(dynamicPayback))
    }

    @ParameterizedTest(name = "payoutRatio={0} → {1}")
    @MethodSource("payoutRatioRatingCases")
    fun `payoutRatio rating`(payoutRatio: Double, expected: Rating?) {
        assertEquals(expected, StockRatingMapper.toPayoutRatioRating(payoutRatio))
    }

    companion object {
        @JvmStatic
        fun peRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(12.0, null),
            Arguments.of(22.0, Rating.CAUTION),
            Arguments.of(27.0, Rating.WARNING),
            Arguments.of(35.0, Rating.WARNING),
        )

        @JvmStatic
        fun deRatingCases() = listOf(
            Arguments.of(0.2, Rating.POSITIVE),
            Arguments.of(0.4, null),
            Arguments.of(0.75, Rating.CAUTION),
            Arguments.of(2.5, Rating.DANGER),
        )

        @JvmStatic
        fun roeRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(5.0, Rating.CAUTION),
            Arguments.of(17.0, null),
            Arguments.of(25.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun profitMarginRatingCases() = listOf(
            Arguments.of(-5.0, Rating.DANGER),
            Arguments.of(3.0, Rating.CAUTION),
            Arguments.of(15.0, null),
            Arguments.of(25.0, Rating.POSITIVE),
        )

        @JvmStatic
        fun earningsEstimateRatingCases() = listOf(
            Arguments.of(-1.0, Rating.DANGER),
            Arguments.of(3.0, Rating.CAUTION),
            Arguments.of(12.0, Rating.POSITIVE),
            Arguments.of(18.0, Rating.CAUTION),
        )

        @JvmStatic
        fun pegRatingCases() = listOf(
            Arguments.of(1.0, null),
            Arguments.of(2.0, null),
            Arguments.of(2.5, Rating.CAUTION),
        )

        @JvmStatic
        fun dynamicPaybackRatingCases() = listOf(
            Arguments.of(5.0, Rating.POSITIVE),
            Arguments.of(12.0, null),
            Arguments.of(18.0, Rating.CAUTION),
            Arguments.of(25.0, Rating.DANGER),
        )

        @JvmStatic
        fun payoutRatioRatingCases() = listOf(
            Arguments.of(50.0, null),
            Arguments.of(80.0, Rating.CAUTION),
            Arguments.of(95.0, Rating.DANGER),
        )
    }
}
