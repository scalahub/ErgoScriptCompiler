package util

import kiosk.encoding.ScalaErgoConverters._
import kiosk.ergo._
import kiosk.script.{KioskScriptCreator, KioskScriptEnv}
import org.sh.utils.Util.using
import scorex.crypto.hash.Blake2b256
import sigmastate.Values

import scala.io.Source

object Compiler {

  /**
    * Used to compile ErgoScript code
    *
    * @param ergoScriptFile The file containing the ErgoScript source code
    * @param symbolsFile The file defining the symbols used in the ErgoScript source code
    * @return (1) The compiled ErgoTree, (2) The hash of the ErgoTree, and (3) The address
    */
  def compile(ergoScriptFile: String, symbolsFile: Option[String]): (String, String, String) = {
    val src: String = using(Source.fromFile(ergoScriptFile))(_.mkString)

    val symbols = symbolsFile.map(loadSymbolsFromFile).getOrElse(NoSymbols)
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
