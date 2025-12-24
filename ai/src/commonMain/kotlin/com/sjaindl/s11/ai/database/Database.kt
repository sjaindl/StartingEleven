package com.sjaindl.s11.ai.database

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ChatDatabase {
    val driver = driverFactory.createDriver()
    val database = ChatDatabase(driver)
    return database
}
