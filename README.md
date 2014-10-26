# Documentation #

Coming soon ...

# User manual #

In order to use yanecos, you have to include the following libraries in your project.

* yanecos.jar
* trove.3.X.X.jar


## Example Code ##

Let's start at the top and see how a simple yanecos app could look like.

```java

//First and formost, you have to create a new data center,
//which is the center of operations of yanecos.

DataCenter dataCenter = new DataCenter();

//Now we add a sample data processor, which is concerned with moving around entities.
dataCenter.addProcessor( new MovementProcessor() );

//Another processer is added, which helps us determining the current positions of entities.
dataCenter.addProcessor( new ConsoleProcessor( System.out ) );

//Here we create a new entity in the system and add two components (which are called data in yanecos)
IEntity entity = dataCenter.createEntity( "example" );
try {
		//one representing the current position in a 2d space
		entity.addData( new Position( 42, 42 ) );
		//and one representing the movement in form of a vector
		entity.addData( new Movement( 0.707f, 0.707f, 2.0f ) );
}
catch( YanecosException ex ) {
		ex.printStackTrace();
}

//this is a method stub, representing a possible update method used in most games.
public void update( double someDeltaSeconds ){
	//in which we will update yanecos

	//first lets update the time past, since the last update
	dataCenter.updateDelta( someDeltaSeconds );

	//and then tell the processors to process
	dataCenter.getProcessor( MovementProcessor.class ).process();
	dataCenter.getProcessor( ConsoleProcessor.class ).process();
}
```

### Here's an example of how a data class could look like: ###

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


### Here's an example of how a processor could look like: ###

```java

//create a class and inherite from DataProcessor
public static class MovementProcessor extends DataProcessor {

	//override the initialization method, to tell yanecos in which
	//entities this processor is intreseted in.
	@Override
	protected void onInitialize() {
	
		//this processor is interested in all entities, which hold both
		//a position data class and a movementdata class
		this.registerInterestIn( Position.class, Movement.class );
	}

	//override the processing method
	@Override
	protected void onProcessing() {
	
		//in which we fetch all interesting entities
		List<IEntity> interestedEntity = this.getInterestedEntities();

		//iterate over them
		for( IEntity entity : interestedEntity ){
		
			try {
				//and try to move them
				Position pos = entity.getData( Position.class );
				Movement mov = entity.getData( Movement.class );
			
				//according to the movement vector
				pos.move( mov.calcMovement() );
			}
			catch( YanecosException ex ) {
				ex.printStackTrace();
			}
			
		}
	}
}
```
