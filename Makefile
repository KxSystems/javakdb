all:
	javac -d build/classes src/kx/c.java src/kx/examples/Feed.java src/kx/examples/QueryResponse.java src/kx/examples/Subscriber.java src/kx/examples/Server.java
#	jar cf build/c.jar build/kx/*.class
Feed:
	java -cp build/classes kx.examples.Feed
Subscriber:
	java -cp build/classes kx.examples.Subscriber
QueryResponse:
	java -cp build/classes kx.examples.QueryResponse
Server:
	java -cp build/classes kx.examples.Server
	
