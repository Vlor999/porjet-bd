# Variables
BIN_DIR = bin
SRC_DIR = src
LIB_DIR = lib
JAR_FILE = $(LIB_DIR)/ojdbc6.jar

CLASSPATH = $(BIN_DIR):$(JAR_FILE)

all: connexion

connexion:
	javac -d $(BIN_DIR) -classpath $(JAR_FILE) -sourcepath $(SRC_DIR) $(SRC_DIR)/lecteur.java

newrun: connexion
	java -classpath $(CLASSPATH) lecteur new

run: connexion
	java -classpath $(CLASSPATH) lecteur

clean:
	rm -rf $(BIN_DIR)/*

.PHONY: all connexion run newrun clean
