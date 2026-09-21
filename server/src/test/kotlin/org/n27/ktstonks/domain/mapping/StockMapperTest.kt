package org.n27.ktstonks.domain.mapping

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StockMapperTest {

    @Test
    fun `mapToStock should return null earningsYield when pe is zero`() {
        assertNull(computeEarningsYield(0.0))
    }

    @Test
    fun `mapToStock should return null dynamicPayback when eps is zero`() {
        assertNull(computeDynamicPayback(price = null, eps = 0.0, growth = 11.43))
    }

    @Test
    fun `computeDynamicPayback should compute with negative growth`() {
        val result = computeDynamicPayback(price = 10.0, eps = 2.0, growth = -10.0)
        assertEquals(6.6, result!!.value, 0.1)
    }

    @Test
    fun `computeDynamicPayback should return null when negative growth never pays back`() {
        assertNull(computeDynamicPayback(price = 30.0, eps = 2.0, growth = -10.0))
    }

    @Test
    fun `computeDynamicPayback should use price over eps when growth is zero`() {
        assertEquals(5.0, computeDynamicPayback(price = 10.0, eps = 2.0, growth = 0.0)!!.value, 0.001)
    }
}
