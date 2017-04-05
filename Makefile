all: 
	java -jar sablecc.jar parser.grammar
	javac compiler/*/*.java 
	javac compiler/*.java

clean:
	rm -rf compiler/analysis
	rm -rf compiler/lexer
	rm -rf compiler/node
	rm -rf compiler/parser
	rm -rf compiler/*.class