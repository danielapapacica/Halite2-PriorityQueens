build:
	javac MyBot.java hlt/*.java

run: MyBot.class 
	java MyBot.class

clean: 
	rm -rf MyBot.class hlt/*.class
