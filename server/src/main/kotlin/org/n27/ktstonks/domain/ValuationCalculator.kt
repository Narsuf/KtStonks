package org.n27.ktstonks.domain

fun Double.getIntrinsicValue(
    valuationFloor: Double,
    expectedEpsGrowth: Double = 0.0,
) = expectedEpsGrowth.toMultiplier() * valuationFloor * this

private fun Double.toMultiplier(): Double = 1 + this / 100
