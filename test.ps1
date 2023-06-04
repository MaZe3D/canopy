$testXml = "<ObjectNode><row><name>John</name><age>30</age></row></ObjectNode>"
$testJson = '{"name":"John","age":30}'
$testYaml = "name: John`nage: 30"

Set-Location .\main
.\gradlew.bat distzip
Set-Location ..
Expand-Archive .\main\app\build\distributions\app.zip .\main\app\build\distributions\app -Force

$testJson | .\main\app\build\distributions\app\app\bin\app.bat LoadJson StoreJson
$testXml | .\main\app\build\distributions\app\app\bin\app.bat LoadXml StoreJson
$testYaml | .\main\app\build\distributions\app\app\bin\app.bat LoadYaml StoreJson

.\main\app\build\distributions\app\app\bin\app.bat LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreJson
.\main\app\build\distributions\app\app\bin\app.bat LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreXml
.\main\app\build\distributions\app\app\bin\app.bat LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreYaml

.\main\app\build\distributions\app\app\bin\app.bat LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreJson:output.json
Get-Content .\output.json


Remove-Item .\output.json