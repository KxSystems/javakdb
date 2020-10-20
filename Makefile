all:
	mkdir -p build/classes
	javac -Xlint:deprecation -d build/classes src/main/java//kx/c.java src/main/java//kx/examples/Feed.java src/main/java//kx/examples/QueryResponse.java src/main/java//kx/examples/Subscriber.java src/main/java//kx/examples/Server.java src/main/java//kx/examples/TypesMapping.java src/main/java//kx/examples/GridViewer.java
#	jar cf build/c.jar build/kx/*.class
doc:
	javadoc -d docs src/main/java//kx/c.java 
Feed:
	java -cp build/classes kx.examples.Feed
Subscriber:
	java -cp build/classes kx.examples.Subscriber
QueryResponse:
	java -cp build/classes kx.examples.QueryResponse
Server:
	java -cp build/classes kx.examples.Server
TypesMapping:
	java -cp build/classes kx.examples.TypesMapping

GridViewer:
	java -cp build/classes kx.examples.GridViewer
	
