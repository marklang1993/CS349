
# This directory is set up to work with the IntelliJ Java IDE.

tetris:
	javac -d out src/tetris/*.java
	java -classpath out tetris.TetrisMain



# To use JUnit tests, download the junit and hamcrest libraries using the
# "Plain Old Jar" links at https://github.com/junit-team/junit4/wiki/Download-and-Install
# Put them in the same directory as the makefile.
tst:
	javac -d out -cp .:junit-4.12.jar src/tetris/*.java  test/tetris/*.java 
	java -cp .:out:junit-4.12.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore \
			tetris.ProgramArgsTest tetris.TetrisTest


catsEyes:
	javac -d out src/catsEyes/*.java
	java -cp .:out catsEyes.Main