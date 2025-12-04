package org.n27.ktstonks.data.db.api_usage

import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.n27.ktstonks.data.db.api_usage.tables.ApiUsages
import java.time.LocalDate

class ApiUsageDao {
    fun getUsage(date: LocalDate): Int = transaction {
        val usage = ApiUsages
            .select { ApiUsages.date eq date.toString() }
            .singleOrNull()
            ?.get(ApiUsages.count)

        if (usage == null) {
            ApiUsages.deleteWhere { ApiUsages.date less date.toString() }
            0
        } else {
            usage
        }
    }

    fun incrementUsage(date: LocalDate) {
        transaction {
            val usage = ApiUsages.select { ApiUsages.date eq date.toString() }.singleOrNull()

            if (usage == null) {
                ApiUsages.insert {
                    it[ApiUsages.date] = date.toString()
                    it[count] = 1
                }
            } else {
                ApiUsages.update({ ApiUsages.date eq date.toString() }) {
                    it[count] = usage[count] + 1
                }
            }
        }
    }
}
