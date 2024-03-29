package com.charlieworld.housing.services.mock

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import com.charlieworld.housing.utils.FileRead
import monix.eval.Task

trait MockFileRead extends FileRead {

  def transformEntity(row: Row, instituteIds: Seq[Long]): Task[Seq[HousingFinanceFileEntity]] =
    Task.now(Seq.empty)
  def readFile(fileName: String): Task[Seq[Row]] = Task.now(Seq.empty)
}
