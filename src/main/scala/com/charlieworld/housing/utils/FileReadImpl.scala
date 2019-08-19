package com.charlieworld.housing.utils

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import com.charlieworld.housing.exceptions.InvalidRow
import monix.eval.Task

import scala.io.Source
import scala.util.{Success, Try}

trait FileReadImpl extends FileRead {

  override def readFile(fileName: String): Task[Seq[Row]] = {
    val filePath = getClass.getResource(fileName).getPath
    val lines = Source.fromFile(filePath)
    (for {
      line ← Task.eval(lines.getLines())
    } yield line.map(_.split(",").toSeq)).map(_.toSeq.drop(1))
  }

  override def transformEntity(
    row: Row,
    instituteIds: Seq[Long]
  ): Task[Seq[HousingFinanceFileEntity]] =
    for {
      year ← Task.eval(Try(row.head.toInt))
      month ← Task.eval(Try(row(1).toInt))
      amounts ← Task.eval(row.slice(2, row.size).map(_.toLong))
      result ← (year, month) match {
        case (Success(y), Success(m)) ⇒
          Task.eval(
            amounts.zip(instituteIds).map {
              case (amount, instituteId) ⇒
                HousingFinanceFileEntity(
                  instituteId,
                  y,
                  m,
                  amount
                )
            }
          )
        case _ ⇒ Task.raiseError(InvalidRow("Invalid row in file"))
      }
    } yield result
}
