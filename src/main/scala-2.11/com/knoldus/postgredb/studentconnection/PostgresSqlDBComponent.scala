package com.knoldus.postgredb.studentconnection
import slick.driver.PostgresDriver

trait PostgreSqlDBComponent extends DBComponent {

  val driver = PostgresDriver

  import driver.api._

  val db: Database = MyPostgreDB.connectionPool

}

 object MyPostgreDB {

  import slick.driver.PostgresDriver.api._

  val connectionPool = Database.forConfig("postgresql")

}



