# PluginHelper

Der PluginHelper unterstützt Entwickler und Entwicklerinnen bei der Evaluation von *IntelliJ*-Plugins im Kontext der Softwaretechnikausbildung. Dazu werden Funktionen für die Verwendung von Konfigurationsdateien (*Informationen über Aufgaben*) und dem Erfassen relevanter (Log-) Daten bereitgestellt. Das *Logging* erfolgt persistent im Benutzerverzeichnis der jeweiligen NutzerIn. Die anfallenden Daten können über eine bereitgestellte Schnittstelle mit einem [externen Server](https://github.com/OOP-Regensburg/PluginHelperServer) synchronisiert werden.

## Verwendung

Eine aktuelle Version der Bibliothek als `JAR`-Datei befindet sich im Ordner `build`. Diese kann als *Dependency* im eigenen Plugin-Projekt eingebunden werden. Alternativ kann der Inhalt des `src`-Ordners in den eigenen Quellcode eingebunden werden (Das *Plugin* verwendet ausschließlich Abhängigkeiten, die über das *IntelliJ Plugin SDK* bereitgestellt werden).

## Funktionen

Die Bibliothek unterstützt aktuell die folgenden Funktionen:

### Aufgabenbeschreibung einlesen

Auf Basis einer *Task*-Datei können Informationen zum aktuell geöffneten OOP-Projekt ausgelesen werden. Diese Datei muss im Wurzelverzeichnis des jeweiligen Projekts liegen und zeilenweise nach dem Schema `KEY=VALUE` aufgebaut sein. 

Die Konfigurationsdatei wird über die Methode `TaskConfiguration.loadFrom(String fileName)` geladen. Falls die Methode ohne Parameter aufgerufen wird, wird standardmäßige aus der Datei `.task` im Wurzelverzeichnis des aktuellen Projekts gelesen. Kann keine Datei gefunden werden oder fehlen in der Konfigurationsdatei Werte, wird eine Konfigurationsobjekt mit *Default*-Werten zurückgegeben.

#### Unterstützte Werte

| Beschreibung | Schlüssel in Datei | Beispiel | Default-Wert | Getter-Methode in TaskConfiguration |
|--------------|--------------------|----------|--------------|-------------------------------------|
| URL zum Github-Repository des Handouts zur aktuellen Aufgabe | HANDOUT_URL        | HANDOUT_URL=http://github.com/OOP/Handout | https://github.com/esolneman/OOP-Helper-Handout-Template | `String getHandoutURL()` | 
| Titel der Aufgabe | TASK        | TASK=Demo Task | "Demo Task" | `String getTaskTitle()` | 

### Logging

Mit Hilfe der  Klassen `LogManager`, `User` und `Log` können zur Laufzeit Logdateien erstellt werden, die außerhalb des jeweiligen Projektes, im Benutzerverzeichnis der NutzerInnen im Ordner `.OOP-Plugin` persistiert werden.

#### User-Klasse

Die Klasse `User` repräsentiert den aktuellen Nutzenden, d.h. den Entwickler bzw. die Entwicklerin, die die Entwicklungsumgebung verwendet. Sie beinhaltet ein sitzungsübergreifend eindeutige *User ID* sowie eine sitzungsabhängig *Session ID*. Durch den Aufruf der Methode `User.getLocalUser` wird ein neues Nutzerobjekt erstellt. Beim ersten Aufruf wird im Benutzerverzeichnis eine Datei zur Speicherung der eindeutigen *User ID* angelegt. Bei zukünftigen Aufrufen wird diese ID zur Initialisierung des `User`-Objektes verwendet. Bei **jedem Aufruf** der Methode wird ein zufällige und eindeutige *Session ID* erzeugt und im `User`-Objekt gespeichert. *User ID*  und *Session ID* können ausgelesen aber nicht verändert werden.

#### LogData-Klasse

Die `LogData`-Klasse speichert Informationen eines einzelnen Eintrags und erzeugt bei Bedarf dessen CSV-Repräsentation. Dem Konstruktor des *immutables* können die folgenden Parameter übergeben werden:

- `Timestamp timestamp`: Erstellungszeitpunkt des Eintrags
- `DataType type`: Typ des Eintrags (Kategorie)
- `String label`: Zusätzliche Beschreibung des Eintrags
- `String payload`: Beliebiger String mit weiteren Inhalten

#### Log-Klasse

Die `Log`-Klasse repräsentiert eine konkretes Log, das im Benutzerverzeichnis in Form zweiter CSV-Dateien (Daten und Metadaten werden separiert gespeichert) persistiert wird. Die `Log`-Klasse erlaubt den schreibenden Zugriff auf die Logdateien. 

*Logs* sind über einen beim Erstellen übergebenen Parameter eindeutig einem bestimmten Experiment zugeordnet. Diese Information wird zusammen mit dem Erstellungsdatum (der Logdatei), einer Hardware-ID (falls vorhanden) und einer eindeutigen Log-ID in der Metadaten-Datei gespeichert. 

### LogManager-Klasse

Die `LogManager`-Klasse erlaubt den einfachen Zugriff auf die einzelnen Logs. Über die Methoden der Klasse können (1) neue Logs für Experimente erstellt werden, (2) existierende Logs geöffnet und (3) Logs mit einem entsprechenden [externen Server](https://github.com/OOP-Regensburg/PluginHelperServer) synchronisiert werden.


#### Beispiel: Logging

``` java	
// Nutzer erstellen oder laden
User user = User.getLocalUser();

// Session Log für das Experiment mit dem Titel "Test" erstellen oder öffnen
Log log = LogManager.openLog(user.getSessionID(), "Test");

// Daten im Log speichern
log.log(user.getSessionID(), LogDataType.CUSTOM, "demo", "Something happened");

// Log synchronisieren
LogManager.syncLog(log, user, serverUrl, new SyncProgressListener() {
    @Override
    public void onFinished() {
    	System.out.println("Upload finished");
    }
    @Override
    public void onFailed() {
        System.out.println("Upload failed");
    }
});
```

**Der *Upload* erfolgt unter Verwendung der *Apache Commons*-Bibliothek. Aktuell wird keine SSL-Verbindung unterstützt - dieses Feature wird vor dem Start erster Studien noch ergänzt.** 

### UI-Komponenten

Über die Klasse `UserDialogManager` kann ein *Confirmation Dialog* erzeugt werden um die Zustimmung der NutzerInnen für z.B. den *Upload* der der Log-Daten zu erfragen:

``` java
UserResponse response = UserDialogManager.showConfirmationDialog("Möchten Sie die Logdatei hochladen?");
if (response == UserResponse.ACCEPT) {
    // Upload data
```