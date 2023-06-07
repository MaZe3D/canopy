# OS-Check
$runScriptName = "app"
if ($IsWindows) {
    $runScriptName = "app.bat"
}

$runScriptPath = "./canopy/app/build/distributions/"
$runScriptName = $runScriptPath + "app/bin/" + $runScriptName

$testXml = "<ObjectNode><row><name>John</name><age>30</age></row></ObjectNode>"
$testJson = '{"name":"John","age":30}'
$testYaml = "name: John`nage: 30"

Set-Location ./canopy
./gradlew.bat distzip
Set-Location ..
Expand-Archive ($runScriptPath + "app.zip") $runScriptPath -Force

$testJson | &$runScriptName LoadJson StoreJson
$testXml | &$runScriptName LoadXml StoreJson
$testYaml | &$runScriptName LoadYaml StoreJson

&$runScriptName LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreJson
&$runScriptName LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreXml
&$runScriptName LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreYaml

&$runScriptName LoadYaml:"https://tools.learningcontainer.com/sample-json.json" StoreJson:output.json
Get-Content ./output.json

# Cleanup
Remove-Item ./output.json