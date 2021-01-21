# ErgoScript Compiler

A CLI tool to compile ErgoScript code to ErgoTree and Address

## How To Use

1. Compile the ErgoScript compiler. If using the precompiled jar, skip to Step 2. 
   - Clone the repository using `git clone https://github.com/scalahub/ErgoScriptCompiler.git`.
   - Ensure SBT is installed and set in path.
   - Compile the jar using `sbt assembly` in the project root folder. A new jar should be created the folder `target/scala-2.12/`. 

2. Compile your ErgoScript code:
   - Put your ErgoScript code in a text file named, say, `myScript.es` (the extension can be anything)
   - Put any symbols (constants) in another file named, say, `mySymbols.json`. See below on how to write this file.
   - The symbols file is optional and is needed only if your code references any symbols.
   - Compile the file using `java -cp <jarFile> Compile <ergoScriptFile> <optionalSymbolsFile>`. Examples
     - `java -cp ErgoScriptCompiler.jar Compile myScript.es mySymbols.json`
     - `java -cp ErgoScriptCompiler.jar Compile myScript.es`
    
## Symbols File

If your ErgoScript code contains constants, such as reference to a token id or a script hash, then encode it in the file as follows:

```json
{
  "symbols":[
    {
      "name":"poolTokenId",
      "type":"CollByte",
      "value":"0fb1eca4646950743bc5a8c341c16871a0ad9b4077e3b276bf93855d51a042d1"
    },
    {
      "name":"epochPrepScriptHash",
      "type":"CollByte",
      "value":"d998e06e0c093b0990fa3da4f3bea4364546803551ea9cac02623e9675ba4522"
    },
    {
      "name":"buffer",
      "type":"Int",
      "value":"4"
    }
  ]
}
```

The `type` can be any of `CollByte`, `Int`, `Long`, `GroupElement` and `Address`.

- `CollByte` is simply given as a 32-byte long hex-encoded byte-array
- `Int` and `Long` are given as numbers encoded as strings
- `GroupElement` is given as a 33-byte compressed elliptic curve point, with point at infinity represented as all zeros.

## Examples

The folder [src/test/resources](src/test/resources) contains sample ErgoScript and symbol files.

Following is a transcript of an execution

```bash
java -cp target/scala-2.12/ErgoScriptCompiler-assembly-0.1.jar Compile src/test/resources/pool_deposit.es src/test/resources/pool_deposit_symbols.json
 
ErgoTree:
100804000e20d998e06e0c093b0990fa3da4f3bea4364546803551ea9cac02623e9675ba4522040004000500040004000e200fb1eca4646950743bc5a8c341c16871a0ad9b4077e3b276bf93855d51a042d1d802d601b2a4730000d602c27201d1ededed93cb7202730193c2b2a5730200720292c1b2a57303009ac17201b0b5a4d901036393c27203c2a77304d9010341639a8c720301c18c720302938cb2db6308b2a5730500730600017307

Blake2b256:
70ff2ce77ce8098ca1fb1f4f06c3c389eed1efa2dc283fcb94fb5ecafffd4d1f

Address:
zLSQDVBaKPo3b92KyHXfjph18fbHyVCkNEt9WaWzQVc7LnfRKTwoBFzryPE2bEukR4jP8mDiRVgBtKM6peYGb1Rq6Hbi5y9qja5B7mBtRdhEPqJzc66WN74QbQ1yvBMJQzGNjD85xpiPgW3mEJ8NvLuYAZL97vkUYZzMEa38FVesfTPYsMn9bqiTX5ZL5ogimzRL9nFyaMbp3wDTZQNeJDhaJjr5mS7BvsrdjTwmiLsXFZVpcw
```

As we can see, the program outputs three values:
1. The ErgoTree corresponding to the Script, serialized and hex-encoded
2. The Blake2b256 hash of the ErgoTree, hex encoded.
3. The address corresponding to the ErgoTree.
