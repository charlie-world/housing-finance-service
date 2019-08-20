package com.charlieworld.housing.utils

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class FileReadSpec extends Matchers with FlatSpecLike {

  object impl extends FileReadImpl

  final val timeout: Duration = 5 seconds

  "readFile" should "return sequence of rows" in {
    val fileName = "/test.csv"
    val expectedResult = Seq(
      Seq("2005", "1", "1019", "846", "82", "95", "30", "157", "57", "80", "99"),
    ).map(_.toSeq)

    Await.result(impl.readFile(fileName).runAsync, timeout) shouldBe expectedResult
  }

  "transformEntity" should "return sequence of HousingFinanceFileEntity" in {
    val row = Seq("2005", "1", "1019", "846", "82", "95", "30", "157", "57", "80", "99")
    val instituteIds = Seq(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)

    Await.result(impl.transformEntity(row, instituteIds).runAsync, timeout) shouldBe Seq(
      HousingFinanceFileEntity(1L, 2005, 1, 1019L),
      HousingFinanceFileEntity(2L, 2005, 1, 846L),
      HousingFinanceFileEntity(3L, 2005, 1, 82L),
      HousingFinanceFileEntity(4L, 2005, 1, 95L),
      HousingFinanceFileEntity(5L, 2005, 1, 30L),
      HousingFinanceFileEntity(6L, 2005, 1, 157L),
      HousingFinanceFileEntity(7L, 2005, 1, 57L),
      HousingFinanceFileEntity(8L, 2005, 1, 80L),
      HousingFinanceFileEntity(9L, 2005, 1, 99L),
    )
  }
}
