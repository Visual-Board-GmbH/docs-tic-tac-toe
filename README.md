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
* 

## Table
```\begin{table}[h]
					\centering
					\begin{tabularx}{\columnwidth}{l X r}
						\textbf{\thead{Nr}} & \textbf{\thead{Zielbeschreibung}} & \textbf{\thead{Priorität \\ (M, S, K)}}\\ \Xhline{2pt}
						1 & Der Prototyp soll bis zum 27.01.2020 fertig sein. & M \\ \Xhline{1pt}
						2 & Der Prototyp soll am 10.02.2020 den Stakeholdern präsentiert werden. & M \\ \Xhline{1pt}
						3 & Das Projekt wird nach dem Wasserfallmodell umgesetzt. & M \\ \Xhline{1pt}
						4 & Am 11.11.2019 findet das Kick-off-Meeting statt, in welchem die Initialisierungsphase besprochen wird und der Übergang in die Konzeptphase stattfindet.& M \\ \Xhline{1pt}
						5 &Bis zum 09.12.2019 muss die Konzeptionelle Planung abgeschlossen sein, um anschliessend mit der Umsetzung zu beginnen & M \\ \Xhline{1pt}
						6 & Das Projekt wird im Wasserfall-Modell in Kombination mit Agile Projektmethoden umgesetzt. & M \\ \Xhline{1pt}
						7 & Folgende Personen sind zu 100\% in das Projekt involviert:
						-  Oliver Egloff
						-	Leonardo Wiedemeier
						-	Samuel Salomon & M \\ \Xhline{1pt}
						8 & Es muss mindestens jede zweite Woche mit dem Kunden kommuniziert werden. & M  \\ \Xhline{1pt}
						9 &	Die angefallenen Kosten sind zu jedem Zeitpunkt aktuell zu halten und mit dem Soll-Zustand zu vergleichen. & M
						
						
					\end{tabularx}
					\caption{Prozessziele}
				\end{table}


