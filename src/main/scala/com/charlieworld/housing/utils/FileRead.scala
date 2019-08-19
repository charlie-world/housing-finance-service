package com.charlieworld.housing.utils

import com.charlieworld.housing.entities.HousingFinanceFileEntity
import monix.eval.Task

trait FileRead {

  type Row = Seq[String]

  def transformEntity(row: Row, instituteIds: Seq[Long]): Task[Seq[HousingFinanceFileEntity]]
  def readFile(fileName: String): Task[Seq[Row]]
}
