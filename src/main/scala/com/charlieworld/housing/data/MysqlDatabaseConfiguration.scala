package com.charlieworld.housing.data

import slick.jdbc.MySQLProfile.api.Database

trait MysqlDatabaseConfiguration {
  lazy val mysql: Database = Database.forConfig("mysql")
}
