package org.n27.ktstonks.domain.mapping

import org.junit.jupiter.api.Test
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
}
