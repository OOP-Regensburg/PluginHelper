# PluginHelper

Diese Bibliothek unterstützt beim Implementieren von *IntelliJ*-Plugins für den OOP-Kurs der Medieninformatik.

## Verwendung

Eine aktuelle Version der Bibliothek als `JAR`-Datei befindet sich im Ordner `build`. Diese muss als *Dependency* im eigenen Plugin-Projekt eingebunden werden.

## Funktionen

Die Bibliothek unterstützt aktuell die folgenden Funktionen:

### Aufgabenbeschreibung

Auf Basis einer *Task*-Datei können Informationen zum aktuell geöffneten OOP-Projekt ausgelesen werden. Diese Datei muss im Wurzelverzeichnis des Projekts liegen und muss zeilenweise nach dem Schema `KEY=VALUE` aufgebaut sein. 

Die Konfigurationsdatei wird über die Methode `TaskConfiguration.loadFrom(String fileName)` geladen. Falls die Methode ohne Parameter aufgerufen wird, wird standardmäßige aus der Datei `.task` im Wurzelverzeichnis des aktuellen Projekts gelesen. Kann keine Datei gefunden werden oder fehlen in der Konfigurationsdatei Werte, wird eine Konfigurationsobjekt mit *Default*-Werten zurückgegeben.

#### Unterstützte Werte

| Beschreibung | Schlüssel in Datei | Beispiel | Default-Wert | Getter-Methode in TaskConfiguration |
|--------------|--------------------|----------|--------------|-------------------------------------|
| URL zum Github-Repository des Handouts zur aktuellen Aufgabe | HANDOUT_URL        | HANDOUT_URL=http://github.com/OOP/Handout | https://github.com/esolneman/OOP-Helper-Handout-Template | `String getHandoutURL()` | 
| Titel der Aufgabe | TASK        | TASK=Demo Task | "Demo Task" | `String getTaskTitle()` | 

### Logging

Mit Hilfe der  Klassen `LogManager` und `Log` können zur Laufzeit Logdateien erstellt werden, die im Benutzerverzeichnis des Anwenders persistiert werden. Der `LogManager` erlaubt das Erstellen neuer (`LogManager.createNewLog`) sowie das Laden existierende *Logs*, die über über die Klasse `Log` repräsentiert werden. Einzelne Einträge im *Log* können auf Basis der Klasse `LogData` erstellt werden. Zu jeder Logdatei wird automatisch eine zusätzliche Datei mit Metainformationen zur Systemumgebung erstellt.

Alle Logdateien werden im Ordner `.OOP-Plugin-Logdaten` des *Home*-Verzeichnis des aktuellen Nutzers angelegt. 

#### LogData-Klasse

Die `LogData`-Klasse speichert Informationen eines einzelnen Eintrags und erzeugt bei Bedarf dessen CSV-Repräsentation. Dem Konstruktor des *immutables* können die folgenden Parameter übergeben werden:

- `Timestamp timestamp`: Erstellungszeitpunkt des Eintrags
- `DataType type`: Typ des Eintrags (Kategorie)
- `String label`: Zusätzliche Beschreibung des Eintrags
- `String payload`: Beliebiger String mit weiteren Inhalten

#### Beispiel: Erstellen eines neuen Logs und eines Logeintrags

```
Log log = LogManager.createNewLog();
Timestamp ts = new Timestamp(System.currentTimeMillis());
LogData data = new LogData(ts, DataType.CUSTOM,"Test", "{\"origin\": \"demo\"}");
log.log(data);
```

#### Beispiel: Öffnen eines existierenden Logs

```
// Die ID eines existierenden *Logs* kann per `getId()` ausgelesen und so für das spätere Öffnen gespeichert werden
String logID = "f550c651-5399-462f-814e-416271040894";
Log existingLog = LogManager.openLog("f550c651-5399-462f-814e-416271040894");
```