package org.n27.ktstonks.data.db.api_usage

import org.jetbrains.exposed.sql.Table

object ApiUsages : Table("api_usages") {
    val id = integer("id").autoIncrement()
    val date = varchar("date", 10)
    val count = integer("count")

    override val primaryKey = PrimaryKey(id)
}
