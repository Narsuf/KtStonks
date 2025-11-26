package org.n27.ktstonks.data.alpha_vantage.mapping

import org.n27.ktstonks.data.alpha_vantage.model.GlobalQuoteResponse

internal fun GlobalQuoteResponse.toPrice() = globalQuote.price.toDoubleOrNull()
