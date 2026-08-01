package org.n27.ktstonks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RatedValue(
    val value: Double,
    val rating: Rating?,
)

@Serializable
enum class Rating {
    POSITIVE,
    CAUTION,
    WARNING,
    DANGER,
}
