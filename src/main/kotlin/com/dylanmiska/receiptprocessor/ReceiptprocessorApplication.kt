package com.dylanmiska.receiptprocessor

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class ReceiptprocessorApplication {
    @Bean
    fun dsl(dataSource: DataSource): DSLContext {
        return DSL.using(dataSource, org.jooq.SQLDialect.H2)
    }
}

fun main(args: Array<String>) {
    runApplication<ReceiptprocessorApplication>(*args)
}
