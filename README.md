# ErgoScriptCompiler

A CLI tool to compile ErgoScript code to ErgoTree and Address

## How To Use

1. If using the precompiled jar, skip to Step 2. 
   1. Clone the repository using `git clone https://github.com/scalahub/ErgoScriptCompiler.git`.
   2. Ensure SBT is installed and set in path.
   3. Compile the jar using `sbt assembly`. There should be a new jar in the folder `target/scala-2.12/`. 

2. Compile your code
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
