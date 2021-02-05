# AgeUSD Bootstrap Instructions

These are the instructions for bootstrapping your own instance of the AgeUSD StableCoin

Before initiating bootstrap, perform the following steps:
- Decide how many stable-coins and reserve-coins can be minted in total. Add 1 to these values. Call the final values `maxScTokens`, `maxRcTokens` respectively.
- Decide the maximum number of votes possible for an update. Call this `maxVotes`.
- Decide on the oracle-pool to use and obtain its NFT id, `oraclePoolNFT`.

The bootstrap process consists of the following steps:
1. Issue Tokens
2. Update Symbols Files
3. Compile ErgoScript Code
5. Create Payment Request

# 1. Issue Tokens

#### For Bank Contract
We need to issue a total of three tokens
1. A token issued in quantity 1 with id `bankNFT`. This identifies the bank box.
2. A token issued in quantity `maxScTokens` with id `scToken`. This identifies the stable-coins.
3. A token issued in quantity `maxRcTokens` with id `rcToken`, This identifies the reserve-coins.

#### For Update Contract
We need to issue a total of two tokens
1. A token issued in quantity 1 with id `updateNFT`. This identifies the update box.
2. A token issued in quantity `maxVotes` with id `ballotToken`. This identifies the ballots.

To issue the tokens, perform the following steps:
- Ensure you have a fully synced and running Ergo node with some Erg balance. We will assume it is running on localhost. If not, change the URL accordingly.
- For each token, make the following request to the node at the endpoint [/wallet/transaction/send](http://localhost:9053/swagger#/wallet/walletTransactionGenerateAndSend) (localhost link). Note that
  a transaction can issue at most one token. Hence, the issuance request must be made separately for each token.
   ```json
   {
     "requests": [
       {
         "amount": 123456,
         "name": "Short symbol",
         "description": "Some token description",
         "decimals": 0
       }
     ],
     "fee": 1500000
   }
   ```
  Replace `123456` with the actual quantity of the token to be issued and optionally give a meaningful name and description.
  Once the tokens are issued and the transaction confirms, note down the token ids for each token.

# 2. Update Symbols File

#### For Bank Contract

At the bare minimum, you need to define the following parameters:
1. `coolingOffHeight`, the height after which the hardwired max-reserve ratio is activated.
   Before this height, this value is infinite.
   This implies we can mint as many reserve-coins as we wish during this period.
2. `oraclePoolNFT`, the id of the oracle-pool NFT.
3. `updateNFT`, the id of the update NFT.

These need to be updated in the file [AgeUSD_symbols.json](src/test/resources/AgeUSD_symbols.json)

#### For Update Contract

At the bare minimum, you need to define the following parameters:
1. `minVotes`, the minimum number of votes needed to update the bank contract.
   This implies we can mint as many reserve-coins as we wish during this period.
2. `ballotTokenId`, the token Id for voting, one token will be held by each stakeholder.
3. `bankNFT`, the id of the bank NFT.

These need to be updated in the file [AgeUSD_update_symbols.json](src/test/resources/AgeUSD_update_symbols.json)

*Note*: If needed, you may also edit the other parameters in both files, provided you know what you are doing.

# 3. Compile ErgoScript Code

For this step, ensure that you have Java 8 or higher installed. 

Following the instructions of the compiler, issue the following commands to compile the contracts.

Note that in the following, `<jarFile>` is the file generated from `sbt assembly` command. This is usually the file `target/scala-2.12/ErgoScriptCompiler-assembly-0.1.jar`.

#### For Bank Contract

`java -cp <jarFile> Compile AgeUSD.es AgeUSD_symbols.json`

The files [AgeUSD.es](src/test/resources/AgeUSD.es) and [AgeUSD_symbols.json](src/test/resources/AgeUSD_symbols.json) are the ones we modified in the step [Update Symbols File](#2-update-symbols-file). 

Copy all the files (including `<jarFile>`) into current folder before running the command. 

Note the address returned by the command. Call this the `bankAddress`.

#### For Update Contract

`java -cp <jarFile> Compile AgeUSD_update.es AgeUSD_update_symbols.json`

The files [AgeUSD_update.es](src/test/resources/AgeUSD_update.es) and [AgeUSD_update_symbols.json](src/test/resources/AgeUSD_update_symbols.json) are the ones we modified in the step [Update Symbols File](#2-update-symbols-file).

Copy all the files (including `<jarFile>`) into current folder before running the command. 

Note the address returned by the command. Call this the `updateAddress`.

# 4. Create Payment Request

Edit the file [payment_request_AgeUSD.json](src/test/resources/payment_request_AgeUSD.json) as follows.
The first payment request corresponds to the bank box and second to the update box.
- Replace the addresses with the actual addresses obtained in the previous step [Compile ErgoScript Code](#3-compile-ergoscript-code).
- Replace the token amounts with the correct token amounts.
- Replace the token ids with the correct token ids obtained in the step [Issue Tokens](#1-issue-tokens), ensuring the following ordering for the bank box: (1) `scToken` (2) `rcToken`  (3) `bankNFT`.

Generate the payment request as follows:

`java -cp <jarFile> Payment payment_request_AgeUSD.json payment_request_AgeUSD_symbols.json`

The files [payment_request_AgeUSD.json](src/test/resources/payment_request_AgeUSD.json) and [payment_request_AgeUSD_symbols.json](src/test/resources/payment_request_AgeUSD_symbols.json) are the ones we modified above.

Copy all the files (including `<jarFile>`) into current folder before running the command. 

This will output something like:
```json
[ {
  "address" : "29irJ65SHH5VxgQaXubC1z9eHzutUWV6BB2QGCbA9eQ53msbyzXdh6bSXm64WMwkiBNRgeXZy1ySSvV...",
  "value" : 1000000,
  "assets" : [ {
    "tokenId" : "a908bf2be7e199014b45e421dc4adb846d8de95e37da87c7f97ac6fb8e863fa2",
    "amount" : 10000000000000
  }, {
    "tokenId" : "b240daba6b5f9f9b6d4e6d7fc8b7c0423f1dfa28a883ec626a18b69be6c7590e",
    "amount" : 10000000000000
  }, {
    "tokenId" : "7bd873b8a886daa7a8bfacdad11d36aeee36c248aaf5779bcd8d41a13e4c1604",
    "amount" : 1
  } ],
  "registers" : [ "0500", "0500" ]
}, {
  "address" : "6Vs43fLottAzin3EiEiswbSD31ETscqBLy9i3zTWCwUVuG79fWuP7S3Kko5PEK56UEBWSTE8GuuXq3g...",
  "value" : 1000000,
  "assets" : [ {
    "tokenId" : "77d14a018507949d1a88a631f76663e8e5101f57305dd5ebd319a41028d80456",
    "amount" : 1
  } ],
  "registers" : [ "0e0601a2b3c4d5e6" ]
} ]
```

Copy this output and use it to make a payment request to the Ergo node at the endpoint [/wallet/payment/send](http://localhost:9053/swagger#/wallet/walletPaymentTransactionGenerateAndSend) (localhost link).
This should send the bootstrap transaction with the bank and update boxes being the first and second outputs respectively.
