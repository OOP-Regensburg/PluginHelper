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
| URL zum Github-Repository des Handouts zur aktuellen Aufgabe| HANDOUT_URL        | HANDOUT_URL=http://github.com/OOP/Handout | https://github.com/esolneman/OOP-Helper-Handout-Template | `String getHandoutURL()` | 

 
