package util

import kiosk.ergo._
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar

import scala.collection.immutable

class ParserSpec extends WordSpec with MockitoSugar with Matchers {
  "Parser" should {
    "parse JSON correctly" in {
      val symbols = loadSymbolsFromFile("src/test/resources/epoch_prep_symbols.json").symbols
      val names: Seq[String] = symbols.map(_.name)
      val values: Seq[KioskType[_]] = symbols.map(_.getValue)

      names shouldBe Seq("livePeriod", "minPoolBoxValue", "buffer", "prepPeriod", "oracleTokenId", "epochPeriod", "liveEpochScriptHash")
      (values zip Seq(
        KioskInt(20),
        KioskLong(20000000),
        KioskInt(4),
        KioskInt(10),
        KioskCollByte("749fe0b8c63213be3451af2578eacabd620a9e687f5c55c54f1ec571b17c9c85".decodeHex),
        KioskInt(30),
        KioskCollByte("396b67b43d9436bb1f310050273f6f34b81beeca1ebea1f126486890d00a282c".decodeHex)
      )) foreach {
        case (left, right) => left.value shouldBe right.value
      }
    }
  }

}
