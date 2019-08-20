package com.charlieworld.housing.data

import slick.jdbc.MySQLProfile.api.Database

trait MysqlDatabaseConfiguration {
  protected val mysql: Database
}
