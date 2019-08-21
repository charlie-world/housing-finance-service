package com.charlieworld.housing

import java.util.Locale

import com.charlieworld.housing.entities._
import spray.json._

import scala.util.matching.Regex

package object serialization {

  trait SnakeCaseJsonProtocol extends DefaultJsonProtocol {
    import SnakeCaseJsonProtocol._

    import reflect._

    protected def snakify(name: String): String =
      camelCaseRegex
        .replaceAllIn(pascalCaseRegex.replaceAllIn(name, replacement), replacement)
        .toLowerCase(Locale.KOREAN)

    override protected def extractFieldNames(classTag: ClassTag[_]): Array[String] = {

      super.extractFieldNames(classTag).map(snakify)
    }
  }

  object SnakeCaseJsonProtocol {
    protected val pascalCaseRegex: Regex = """([A-Z]+)([A-Z][a-z])""".r
    protected val camelCaseRegex: Regex = """([a-z\d])([A-Z])""".r
    protected val replacement: String = "$1_$2"
  }

  object JsonProtocol extends SnakeCaseJsonProtocol {

    private final val DATA_KEY = "data"
    private final val RESULT_MSG = "result_msg"

    implicit val TopOneYearlyAmountResponseJsonFormat = jsonFormat2(
      TopOneYearlyAmountResponse.apply
    )
    implicit val YearlyAmountResponseJsonFormat = jsonFormat2(YearlyAmountResponse)
    implicit val YearlyAvgAmountResponseJsonFormat = jsonFormat2(YearlyAvgAmountResponse)
    implicit val YearlyTotalAmountResponseJsonFormat = jsonFormat3(YearlyTotalAmountResponse)
    implicit val HousingFinanceDataResponseJsonFormat = jsonFormat2(HousingFinanceDataResponse)
    implicit val JWTResponseJsonFormat = jsonFormat1(JWTResponse)
    implicit val UserRequestJsonFormat = jsonFormat2(UserRequest)
  }
}
