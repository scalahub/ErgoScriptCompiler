package util

import kiosk.ergo._
import org.scalatest.{Matchers, WordSpec}

class ParserSpec extends WordSpec with Matchers {
  "Parser" should {
    "parse Symbols correctly" in {
      val symbols = loadSymbols("src/test/resources/epoch_prep_symbols.json").symbols
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

    "parse PaymentRequest correctly" in {
      val paymentRequests = loadPaymentRequest("src/test/resources/payment_request_AgeUSD.json")

      paymentRequests shouldBe Seq(
        PaymentRequest(
          address =
            "29irJ65SHH5VxgQaXubC1z9eHzutUWV6BB2QGCbA9eQ53msbyzXdh6bSXm64WMwkiBNRgeXZy1ySSvVuKtEE4ifUbdXiV4PtdqsdxWxrhmJoqnDHSimsCz2zLsUGFm9j16kivMoS3ek4JPzvxuLddru5DLGaLJaLqN3dSjrY398Puyd2SxaxmUNpLy4PscVv8Gs7x4TYdBffnb9CvTZekitcwgu8hqgzU9yFHo37czxDJSg95gMyevKUivy7wfoxinoCdpKcN8WnHQ8jXA6kVxt2n55EAjJpaP6RkXRdszWAxZGFYHGvaNrRoFzThkpddgtm23P2ayQAYcUyTky8LtAgep1DpPueLWw25tN3hebEkfBy5K95vThXk5ChCBrxKzi6jFtWARdJ7Gm3F61L44jjhBn4qCFizqyJJ62axxhSv8sPnMXu8Je4sSFo9UBCheNoobYDyteatm8RpKLR6vDiKyZSyhKBYgcccNfCfhHHj8N6qr1gYueVvkb6SMxQpFbtHV8pqvdUUKhThNMVRkeBQz6ZeuPdnXEXBU5HZsJbT4oN51T5jUEfF5FfHWHuX9bnC9wfLzx7z7fehiSNdfb1XSG1UDfgs2sDZD6G6TJ5TQev8s5nXtUaJoz3xddnfxLnYDFjJqZnnhQSqmtndBkPZGHgRmt2fY4UxF2AqZ7E6RrK3d6QtaXkYAyLke7GrDTbUgAGXUtizy3tEhAtK3dy46oCVMdkqA2go4iXZ1gFVeuzxGPAvEUW8gRsHHSyP3hAeA7n1tbS9qRajCFB19YLnSd8UPRzkBU4e4NdAneZqZ5TqK9T34eusMLqqriJ1Qp7oAVcG2tbtik82nzX8WhctoXFGQJh1UAeJKFpQ22YCUX7vR17BwehSjpRwc5EK5ZqtCnGpUsfmGY6eyc1txwGRT1AS2m9NYweAmGh7Eq8VZbaXtbJUAhdLrBD8GpVnYjCzZiboZxyokbjFqmifFLYVkRXoZLG7CzqSEBhgtiew",
          value = 1000000L,
          assets = Seq(
            Token(tokenId = "7bd873b8a886daa7a8bfacdad11d36aeee36c248aaf5779bcd8d41a13e4c1604", amount = 1L),
            Token(tokenId = "a908bf2be7e199014b45e421dc4adb846d8de95e37da87c7f97ac6fb8e863fa2", amount = 10000000000000L),
            Token(tokenId = "b240daba6b5f9f9b6d4e6d7fc8b7c0423f1dfa28a883ec626a18b69be6c7590e", amount = 10000000000000L),
          ),
          registers = Seq("zero", "zero")
        )
      )
    }
  }

}
