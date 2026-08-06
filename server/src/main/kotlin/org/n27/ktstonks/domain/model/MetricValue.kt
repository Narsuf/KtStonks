package org.n27.ktstonks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MetricValue(
    val value: Double,
    val rating: Rating?,
    val variation: Double? = null,
)

@Serializable
enum class Rating {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
