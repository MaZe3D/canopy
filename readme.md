# canopy
canopy ist ein kompaktes und flexibles Kommandozeilenprogramm, welches JSON, XML und YAML Dateien in einander umwandeln kann.

## Bedienung
### Filter
Die Verwendung des Tools erfolgt durch das verketten sogenannter Filter. Filter sind Module welche die Dateien Laden, Speichern und Manipulieren können.
Ein Filter nimmt bestehenden Inhalt entgegen und führt Operationen an diesem durch. Das Ergebnis kann dann einem nächsten Filter übergeben werden.

Folgende FIlter stehen ab Werk zur Verfügung:
| Modul       | Parameter                          | Beschreibung                                                                                                                                                               |
| ----------- | ---------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `LoadJson`  | Dateipfad (Optional)               | Lädt eine vorliegende JSON-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Eingabe (stdin) übernommen.       |
| `LoadXml`   | Dateipfad (Optional)               | Lädt eine vorliegende XML-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Eingabe (stdin) übernommen.        |
| `LoadYaml`  | Dateipfad (Optional)               | Lädt eine vorliegende YAML-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Eingabe (stdin) übernommen.       |
| `StoreJson` | Dateipfad (Optional)               | Speichert eine vorliegende JSON-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Ausgabe (stdout) übernommen. |
| `StoreXml`  | Dateipfad (Optional)               | Speichert eine vorliegende XML-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Ausgabe (stdout) übernommen.  |
| `StoreYaml` | Dateipfad (Optional)               | Speichert eine vorliegende YAML-Datei in das Programm zur Weiterverarbeitung. Wird kein Dateipfad übergeben, wird der Inhalt aus der Standard-Ausgabe (stdout) übernommen. |
| `Encrypt`   | Schlüssel                          |                                                                                                                                                                            |
| `Decrypt`   | Schlüssel                          |                                                                                                                                                                            |
| `Extract`   | Name des zu extrahierenden Knotens |                                                                                                                                                                            |
### Aufruf des Programms
Beim Aufruf der Programms über die Kommandozeile werden Parameter übergeben welche dem Programm beschreiben wie es die Dateien zu manipulieren und abzuspeichern hat.

Ein Beispiel für einen Programmaufruf lautet wie folgt:
```
canopy LoadJson:"./example.json" StoreXml:"./new.json"
```
Dieses Programm lädt nun die Module `LoadJson` und `StoreJson`. Modulen können Parameter übergeben werden, wie in diesem Beispiel der Dateipfad. Die Module werden nun in der Reinfolge abgearbeitet, in welcher diese an das Programm übergeben wurden. Zunächst wird die Datei `./example.json` von `LoadJson` eingelesen und dann wird diese vom Modul `StoreXml` abgespeichert.

Module lassen sich Beliebig anwenden und verketten. So lässt sich mit dem Aufruf
```
canopy LoadJson:"./example.json" Encrypt:"password123" StoreJSON:"./encrypted.json StoreXML:"./encrypted.xml"
```
1. `LoadJson:"./example.json"` lädt die Datei `./example.json"
2. `Encrypt:"password123"` verschlüsselt alle Values der Baumstruktur. Die Daten liegen dann Veschlüsselt im base64-Format vor.
3. `StoreJson:"./encrypted.json` speichert das Ergebnis in einer JSON-Datei
4. `StoreXml:"./encrypted.xml` speichert das Ergebnis zusätzlich in einer XML-Datei

