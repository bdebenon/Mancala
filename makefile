#
# A simple makefile for compiling three java classes
#

# define a makefile variable for the java compiler
#
JCC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g

# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#
default: AIeasy.class AImedium.class AIhard.class mancalaClickableHouse.class mancalaCacheHouse.class boardJPanel.class GameUI.class Game.class

# this target entry builds the Average class
# the Average.class file is dependent on the Average.java file
# and the rule associated with this entry gives the command to create it
#
AIeasy.class: AIeasy.java
	$(JCC) $(JFLAGS) AIeasy.java

AImedium.class: AImedium.java
	$(JCC) $(JFLAGS) AImedium.java

AIhard.class: AIhard.java
	$(JCC) $(JFLAGS) AIhard.java

mancalaClickableHouse.class: mancalaClickableHouse.java
	$(JCC) $(JFLAGS) mancalaClickableHouse.java

mancalaCacheHouse.class: mancalaCacheHouse.java
	$(JCC) $(JFLAGS) mancalaCacheHouse.java

boardJPanel.class: boardJPanel.java
	$(JCC) $(JFLAGS) boardJPanel.java

GameUI.class: GameUI.java
	$(JCC) $(JFLAGS) GameUI.java

Game.class: Game.java
	$(JCC) $(JFLAGS) Game.java

# To start over from scratch, type 'make clean'.  
# Removes all .class files, so that the next make rebuilds them
#
clean: 
	$(RM) *.class