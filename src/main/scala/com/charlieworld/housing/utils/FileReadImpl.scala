package com.charlieworld.housing.utils

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import monix.eval.Task

import scala.io.Source
import scala.util.Try

trait FileReadImpl extends FileRead {
  override def readFile(fileName: String): Task[Seq[Row]] = {
    val filePath = getClass.getResource(fileName).getPath
    val lines = Source.fromFile(filePath)
    Task.gather(
      (for {
        line <- lines.getLines().slice(1, lines.size)
      } yield Task.eval(line.split(",").toSeq)).toSeq
    )
  }

  override def transformEntity(row: Row): Task[Seq[HousingFinanceFileEntity]] = {
//    Try {
//      val year = row.head
//      val month = row(1)
//      val
//    }
  }
}
