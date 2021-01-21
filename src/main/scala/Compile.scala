import kiosk.encoding.ScalaErgoConverters._
import kiosk.ergo
import kiosk.ergo._
import kiosk.offchain.compiler.model.DataType
import kiosk.offchain.parser.Parser.checkedReads
import kiosk.script.{KioskScriptCreator, KioskScriptEnv}
import org.sh.utils.Util.using
import play.api.libs.json.{JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import scorex.crypto.hash.Blake2b256
import sigmastate.Values

import scala.io.Source
import scala.util.Try

case class Symbol(name: String, var `type`: DataType.Type, value: String) {
  def getValue: ergo.KioskType[_] = DataType.getValue(value, `type`)
}
case class Symbols(symbols: Seq[Symbol])

object Parser {
  implicit val readsDataType = new Reads[DataType.Type] {
    override def reads(json: JsValue): JsResult[DataType.Type] = JsSuccess(DataType.fromString(json.as[String]))
  }
  implicit val readsSymbol = checkedReads(Json.reads[Symbol])
  implicit val readsSymbols = checkedReads(Json.reads[Symbols])
}

object Compiler {
  import Parser._
  def compile(ergoScriptFile: String, symbolsFile: Option[String]) = {
    val src: String = using(Source.fromFile(ergoScriptFile))(_.mkString)
    val symbols: Symbols = symbolsFile
      .map { fileName =>
        val symbolsSrc = using(Source.fromFile(fileName))(_.mkString)
        Json.parse(symbolsSrc).as[Symbols]
      }
      .getOrElse(Symbols(Nil))
    val env = new KioskScriptEnv()
    symbols.symbols.foreach(symbol => env.$addIfNotExist(symbol.name, symbol.getValue))
    val scriptCreator = new KioskScriptCreator(env)
    val ergoTree: Values.ErgoTree = scriptCreator.$compile(src)
    val ergoTreeEncoded = ergoTreeToString(ergoTree)
    val hashEncoded = Blake2b256(ergoTree.bytes).encodeHex
    val address = getStringFromAddress(getAddressFromErgoTree(ergoTree))
    (ergoTreeEncoded, hashEncoded, address)
  }
}
object Compile {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) println("Usage java -cp <jar> Compiler <ergoScript_file> <optional_symbols_file>")
    else {
      val (ergoTree, hash, address) = Compiler.compile(args(0), Try(args(1)).toOption)
      println("ErgoTree:")
      println(ergoTree)
      println
      println("Blake2b256:")
      println(hash)
      println
      println("Address:")
      println(address)
    }
  }

}
