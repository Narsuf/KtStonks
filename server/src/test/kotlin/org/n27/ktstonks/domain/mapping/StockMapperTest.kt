package org.n27.ktstonks.domain.mapping

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.n27.ktstonks.domain.model.Rating
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StockMapperTest {

    @ParameterizedTest(name = "pe={0} → peg={1}")
    @MethodSource("pegRatingCases")
    fun `peg rating`(pe: Double, expected: Rating?) {
        assertEquals(expected, computePeg(pe, growth = 10.0)?.rating)
    }

    @ParameterizedTest(name = "price={0}, eps={1}, growthHigh={2} → dynamicPayback={3}")
    @MethodSource("dynamicPaybackRatingCases")
    fun `dynamicPayback rating`(price: Double, eps: Double, growthHigh: Double, expected: Rating?) {
        assertEquals(expected, computeDynamicPayback(price, eps, growthHigh)?.rating)
    }

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        assertNull(computeEarningsYield(0.0))
    }

    @Test
    fun `mapToStock should return null peg when growth is null`() {
        assertNull(computePeg(pe = 34.72, growth = null))
    }

    @Test
    fun `mapToStock should return null peg when growth is negative`() {
        assertNull(computePeg(pe = 34.72, growth = -1.0))
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        assertNull(computeDynamicPayback(price = null, eps = 0.0, growth = 11.43))
    }

    companion object {
        @JvmStatic
        fun pegRatingCases() = listOf(
            Arguments.of(10.0, null),
            Arguments.of(15.0, null),
            Arguments.of(20.0, null),
            Arguments.of(21.0, Rating.CAUTION),
            Arguments.of(40.0, Rating.CAUTION),
        )

        @JvmStatic
        fun dynamicPaybackRatingCases() = listOf(
            Arguments.of(50.0, 10.0, 10.0, Rating.POSITIVE),
            Arguments.of(150.0, 7.47, 8.65, null),
            Arguments.of(259.37, 7.47, 8.65, Rating.CAUTION),
            Arguments.of(500.0, 7.47, 8.65, Rating.DANGER),
        )
    }
}
