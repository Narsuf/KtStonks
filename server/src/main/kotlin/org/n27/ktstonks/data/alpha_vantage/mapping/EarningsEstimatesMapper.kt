package org.n27.ktstonks.data.alpha_vantage.mapping

import org.n27.ktstonks.data.alpha_vantage.model.EarningsEstimates

internal fun EarningsEstimates.toExpectedEpsGrowth() = annualEarnings
    .getOrNull(0)
    ?.expectedEpsGrowth
    ?.toDoubleOrNull()
