package com.charlieworld.housing.services

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import com.charlieworld.housing.utils.FileRead
import monix.eval.Task

trait MockFileRead extends FileRead {
  def transformEntity(row: Row, instituteIds: Seq[Long]): Task[Seq[HousingFinanceFileEntity]] = ???
  def readFile(fileName: String): Task[Seq[Row]] = ???
}
