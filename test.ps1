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

&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreJson
&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreXml
&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreYaml

&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreJson:output.json
&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreXml:output.xml
&$runScriptName LoadJson:"https://tools.learningcontainer.com/sample-json.json" StoreYaml:output.yaml
Get-Content ./output.json
Get-Content ./output.xml
Get-Content ./output.yaml

# Cleanup
Remove-Item ./output.*
