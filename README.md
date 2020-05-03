# docs-tic-tac-toe

Da wir neu LaTeX als Dokumentensprache verwendet werde ich hier meine Wichtigsten Erkenntnisse zusammenfassen.


## Templates
Da beim compilieren eines LaTeX Dokuments gleich immer mehrere Dokumente erstellt werden, verlinkte Grafiken sowie verlinkte PDF Dateien nicht im Dokument eingebunden sind, empfiehlt es sich einen Ordner als Template zu verwenen.
Zurzeit existieren 4 Templates
* Landscape mit Header und Footer
* Landscape
* Portrait mit Header und Footer
* Portrait

Alle Templates sind im Ordner 00_Templates zu finden un die Ordner müssen immer als ganzes kopiert werden.

Mitjedem Template Ordner werden die folgenden Ordner mitkopiert.
* 01_ Grafiken --> Wird verwendet um im Dokument verlinkte Grafiken zu speichern.
* 02_Dokumente --> Wird verwendet um im Dokument verlinkte PDF Dateien zu sichern

## Grafiken Verlinken
Wie bereits erwähnt müssen Grafik im Ordner 01_Grafiken abgelegt werden damit sie später verlinkt werden können.
Mit dem Befehl \includegraphics{NameDesBildes.png} kann ein Bild eingefügt werden.

Erweitert kann ein figure enviroment mit \begin{figure} erzeugt werden. So können Bilder platziert und Captions hinzugefügt werden.


## PDF verlinken
PDFs können ebenfalls im Dokument hinzugefügt werden.

Mit \includepdf[pagecommand={\pagestyle{fancy}}]{<NameDesPDF>.pdf} wird der Header des aktuellen Dokumentes übernommen
Mit \includepdf{<NameDesPDF>.pdf} wird der Header des verlinkten Dokumentes übernommen


## Tips und Trick
* Mit Strg + Klick auf Package name wird die enstprechende Dokumentation des Packages geöffnet
* Änderungen im Inhaltsverzeichnis (Sections) sind erst sichtbar wenn das Dokument zwei mal kompiliert wurde.


