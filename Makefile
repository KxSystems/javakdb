all:
	mkdir -p build/classes
	javac -d build/classes src/kx/c.java src/kx/examples/Feed.java src/kx/examples/QueryResponse.java src/kx/examples/Subscriber.java src/kx/examples/Server.java src/kx/examples/TypesMapping.java src/kx/examples/GridViewer.java
#	jar cf build/c.jar build/kx/*.class
doc:
	javadoc -d docs src/kx/c.java 
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
	
