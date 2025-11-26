package org.n27.ktstonks.data.db

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
