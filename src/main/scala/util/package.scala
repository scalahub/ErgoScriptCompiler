import kiosk.ergo
import kiosk.offchain.compiler.model.DataType
import org.sh.utils.Util.using
import play.api.libs.json.Json

import scala.io.Source

package object util {

  import Parser._

  case class Symbol(name: String, var `type`: DataType.Type, value: String) {
    def getValue: ergo.KioskType[_] = DataType.getValue(value, `type`)
  }

  case class Symbols(symbols: Seq[Symbol])

  val NoSymbols = Symbols(Nil)

  def loadSymbolsFromFile(symbolsFile: String) =
    loadSymbolsFromJSON(using(Source.fromFile(symbolsFile))(_.mkString))

  def loadSymbolsFromJSON(source: String) = Json.parse(source).as[Symbols]

  case class Token(tokenId: String, amount: Long)

  case class PaymentRequest(address: String, value: Long, assets: Seq[Token], registers: Seq[String])
}
