package util

import kiosk.offchain.compiler.model.DataType
import kiosk.offchain.parser.Parser.checkedReads
import play.api.libs.json.{JsResult, JsSuccess, JsValue, Json, Reads}

object Parser {
  implicit val readsDataType = new Reads[DataType.Type] {
    override def reads(json: JsValue): JsResult[DataType.Type] = JsSuccess(DataType.fromString(json.as[String]))
  }
  implicit val readsSymbol = checkedReads(Json.reads[Symbol])
  implicit val readsSymbols = checkedReads(Json.reads[Symbols])

  implicit val readsToken = checkedReads(Json.reads[Token])
  implicit val readsPaymentRequest = checkedReads(Json.reads[PaymentRequest])
}
