# Vorbereitung und Analyse #
## Problemanalyse ##
Die prominentesten Problemen, die sich mir bei der Entwicklung stellten, waren die Organisation und Assoziation von Entitäten ihrer Daten und den verarbeitenden Prozessen.
Bei der Organisation habe ich mich dafür entschieden, alle für YANECOS relevanten Daten in einem Datenbankobjekt ab zu legen.
Die Vorteile die mich dazu bewogen haben, waren die einfache und übersichtliche Verwaltung der Daten da alles an einer Zentralen Stelle zu finden ist. Des Weiteren bietet sich so die Möglichkeit, Teile des Programms während der Laufzeit aus zu tauschen, in dem man lediglich das Datenbankobjekt austauscht.
Bei der Assoziation von Daten zusammengefasst in Entitäten und Prozessoren welche diese Daten verarbeiten sollen, benutze ich ein sogenanntes 'Interesssen-System'. Die Funktion gestaltet sich im folgenden so, dass bei der Definition eines Prozessors festgelegt wird, welche Daten für ihn interssant sind. Wird nun ein Prozessor dieses Typs, zum DataCenter hinzugefügt, ermittelt das DataCenter anhand der angegebenen Typen von Daten, alle Entitäten welche momentan Daten dieses Typs beinhaltet.
Was aber nun wenn sich während der Laufzeit eine Entität ändert? Nun dafür hat das Interface welches der Benutzer zur Interaktion mit Entitäten benutzt (IEntity) eine Funktion welche aufgerufen wird, nachdem sich eine Entität geändert hat, sprich Daten hinzugefügt oder entfernt worden sind.
Diese Funktion sorgt nun dafür, dass das DataCenter erneut für alle Systeme ermittelt ob die geänderte Entität immer noch interessant ist, bzw. nun nach der Änderung interessant geworden ist.

## Recherche bestehender Problemlösungen ##
###Artemis Framework###
Das Artemis Framework, ist eine umfangreiche Java-Bibliothek welche denen von YANECOS ähnliche Klassen zur Entitäten-Komponenten basierenden Programmierung bereitstellt.
###Unity Engine###
Die UnityEngine ist eine bekannte und viel genuzte 3D-Engine zur Spieleentwicklung, welche es ermöglicht ihre 'GameObjects' mit beliebgen Kompoenent aus zu statten, wie etwa Inforamtionen für das Physik-System bis hin zu  Infromationen über das Verhalten, in Form eines Skripts.

# Entwurf und Implementierung #
## Kurzbeschreibung ##
Im Prinzip stellt sich die Grundstruktur von YANECOS recht einfach dar. Da wäre zum einen eine zentrale Stelle, welche alle notwendigen Information speichert, eine Art Datenbank. Im Falle von YANECOS ist dies eine Klasse DataCore, die aus einer Sammlung von Map und List Objekten besteht, welche Information über Entitäten, Daten und Prozessoren, als auch ihre Assoziationen beinhalten.
Im Weiteren muss jede Klasse die als Daten-Klasse für eine Entität dienen soll von einer gemeinsamen Grundklasse Data erben, um eine vereinheitlichte Organisation von Daten zu gewährleisten.
Da eine Entität im Grunde nur ein Schlüssel in Form einer eindeutigen Zahl darstellt und sich eine Entität nur durch die mit ihr assoziierten Daten definiert, gibt es keine Notwendigkeit einer Entitäten-Klasse. Statt dessen gibt es eine Manager-Klasse welche einer Sammlung aus Funktionen besteht um mit einer Entität zu arbeiten, wie etwa eine neue Entität erstellen, ihr Daten zuweisen, löschen, o.ä.
Um trotzdem die Möglichkeit zu haben, mit dem Konzept eine Entität wie mit ein Objekt zu interagieren, wird ein Interface IEntity bereitgestellt, welches im Sinne einer Fassade, Funktionen zum hinzufügen, ändern oder entfernen von Daten ermöglicht, indem es eine lediglich auf Paketebene sichtbare Klasse gibt,welche dieses Interface implementiert und die aus lediglich einer Referenz auf das DataCenter und der ID der Entität mit welcher gearbeitet werden soll besteht.
Letztendlich müssen die Daten einer Entität auch verarbeitet werden. Dies übernimmt in der Regel ein sogenannter Prozessor.
Jeder Prozessor basiert auf einer Prozessor-Grundklasse, welche zwei Kernpunkte besitzt. Zum einen ermöglicht sie das registrieren von Interesse an einer Menge von Entitäten, die eine gewisse 'Ausprägung' aufweisen, welche sich aus einer bestimmten Zusammensetzung von Daten definiert. Beispielsweise wäre ein Prozessor welcher Entitäten im Raum bewegen soll an Entitäten interessiert, welche Daten zur Position sowie eines Bewegungsvektors besitzen.
Zum anderen bietet die Prozessor-Grundklasse eine abstrakte Methode zum Verarbeiten von Entitäten, welche jeder abgeleitet Prozessor dann je nach Zweck aus implementieren muss.
Schlussendlich soll es dem Benutzer einfach gemacht werden diese Einzelteile zu verwalten in dem er mit einer zusammenfassenden Schnittstelle kommuniziert, welche das DataCenter darstellt.
Die DataCenter-Klasse beinhaltet sowohl den DataCore, als auch die Manager-Klasse für die Arbeit mit Entitäten sowie Prozessoren und bietet im Stile des Facade-Pattern eine vereinfachte Schnittstelle an.

## Modularisierung ##
YANECOS besitzt zwei Pakete. Das erste enthält die wesentlichen Kernfunktionalitäten und das zweite die Definitionen der Ausnahmen (Exceptions), welche während der Benutzung von YANECOS auftreten können.

# Modulbeschreibung #
## com.blurryroots.yanecos.core ##
### DataCore ###
Beinhaltet alle für das Funktionieren von YANECOS benötigten Information.
Kann verglichen werden mit einer Datenbank.
### Data ###
Dient als Grundklasse jeder Klasse die im späteren Programmverlauf einer Entität hinzugefügt werden kann.
Grundsätzlich sind für jede Daten-Klasse zwei Dinge gleich. Zum einen kann der Datensatz aktiv oder inaktiv sein und zum anderen muss er eine Funktion zur verfügung stellen, um unabhängig von der Komplexität des Datensatzes eine tiefe Kopie an zu legen, welches mittels des Cloneable Interfaces gelöst wird.
### DataCenter ###
Ist das Herzstück von YANECOS. Über das DataCenter werden alle wichtigen YANECOS Operationen ausgeführt, wie das erstellen, verändern oder löschen von Entitäten und das Hinzufügen oder Entfernen von Prozessoren.
DataProcessor und InterestListDataProcessor
DataProcessor dient als Grundklasse für alle Prozessoren, welche Daten von Entiäten verarbeiten sollen.
InterestListDataProcessor vereinfacht es Prozessoren zu schreiben, welche mit einer gewissen Menge an Entiäten arbeiten, an denen sie interessiert sind.
### DataProcessorManager ###
Enthält alle Funktionalität um mit Prozessoren zu arbeiten, wie diese in die Datenbank hinzu zu fügen, oder das Interesse von Prozessoren zu aktualisieren.

### IEntity und EntityImplementation ###
Das Interface IEntity dient dem intuitiveren Umgang mit dem Konzept einer Entitä und ihren Daten.
EntityImplementation ist die nur auf Paketebenen sichtbare Implementierung von IEntiy.
EntityManager
Haust alle Funktionalität um mit Entitäten um zu gehen, wie diese erzeugen oder zu löschen, als auch Entitäten aus dem DataCore auszusuchen.
com.blurryroots.yanecos.expection

## YanecosException ##
Stellt die Grundklasse für alle weiteren Ausnahmen dar, welche bei der Benutzung von YANECOS auftreten können.
### EntityDataException ###
Bildet die Grundklasse für Ausnahmen die von Daten einer Entität ausgelöst werden.
### EntityException ###
Bildet die Grundklasse für Ausnahmen die von einer Entität ausgelöst werden.
### EntityAlreadHasDataException ###
Tritt auf wenn einer Entität versucht wird, ein Typ von Daten hinzu zu fügen, den sie bereits besitzt.
### NoSuchDataException ###
Tritt auf wenn versucht wird, nach Daten zu fragen, welche nicht im DataCore vorhanden sind.
### NoSuchDataOnEntityException ###
Tritt auf wenn versucht wird, Daten einer Entität ab zu fragen, welche diese gar nicht besitzt.
### NoSuchEntityException ###
Tritt auf wenn versucht wird, mit einer Entität zu arbeiten, welche nicht existiert.

# Spezifikation der Klassen- und Memberfunktionen #
Für die spezifischen Informationen über Klassen und ihren Methoden, siehe JavaDoc im Anhang.	 

# Verwendete Werkzeuge und Frameworks #   
**Trove**   
Version: 3.0.2   
Implementierung von Java Collections mit basis Datentypen   
Internet Seite: http://trove.starlight-systems.com   
Lizenz: LPGL 2.1   

# Anleitung für Benutzer #
Um YANECOS zu benutzen, muss zuerst die Jar-Datei von Trove als auch die von YANECOS in das Projekt eingebunden werden. 

Hier nun ein kurzes Quellcode-Beispiel, wie die Benutzung von YANECOS aussehen könnte.

## Beispiel: ##
```java

//Als erstes wird das DataCenter erstellt,
//welches das Zentrale Organ in YANECOS darstellt.

DataCenter dataCenter = new DataCenter();

//Nun muss dem DataCenter ein DataCore zur Verfügung 
//gestellt werden, um Entitäten, Daten und Prozessoren
//darin zu speichern.

dataCenter.setDataCore( DataCore.createEmptyDataCore() );

//Angenommen, wir möchten nun einen Prozessor
//hinzu fügen, welcher Entitäten in der Welt bewegt.

dataCenter.addProcessor( new MovementProcessor() );

//Und einen welcher uns die Positionen auf einer
//Console ausgibt

dataCenter.addProcessor( new ConsoleProcessor( System.out ) );

//Als nächstes wird eine Entität hinzu gefügt, welche
//von den Prozessoren bearbeitet werden kann

IEntity entity = dataCenter.createEntity( "example" );
try {
		//
		entity.addData( new Position( 42, 42 ) );
		entity.addData( new Movement( 0.707f, 0.707f, 2.0f ) );
}
catch( YanecosException ex ) {
		ex.printStackTrace();
}

//Nun kann in der Update Funktion des Spiels,
//ein Prozessor abgerufen und zur Verarbeitung angewiesen
//werden

public void update( double someDeltaSeconds ){

	//
	dataCenter.updateDelta( someDeltaSeconds );

	//
	dataCenter.getProcessor( MovementProcessor.class ).process();
	dataCenter.getProcessor( ConsoleProcessor.class ).process();
}
```


### Hier ein Beispiel für die Position Daten-Klasse: ###

```java

//
public
static class Position extends Data {
	//
	public float x, y;
	
	//
	public 
	Position( float someX, float someY ){
		//
		x = someX; y = someY;
	}
	
	//
	public
	void move( Vector2 someVector ){
		//
		this.x += someVector.x;
		this.y += someVector.y;
	}	
	
	//
	@Override
	public 
	Object clone() {
		//
		return new Position( this.x, this.y );
	}	
}
```


### Und ein Beispiel für den Bewegenungs-Prozessor: ###

```java

//
public static class MovementProcessor extends DataProcessor {
	//
	@Override
	protected void onInitialize() {
		//
		this.registerInterestIn( Position.class, Movement.class );
	}

	@Override
	protected void onProcessing() {
		//
		List<IEntity> interestedEntity = this.getInterestedEntities();

		//
		for( IEntity entity : interestedEntity ){
			try {
				//
				Position pos = entity.getData( Position.class );
				Movement mov = entity.getData( Movement.class );
			
				//
				pos.move( mov.calcMovement() );
			}
			catch( YanecosException ex ) {
				ex.printStackTrace();
			}
		}
	}
}
```