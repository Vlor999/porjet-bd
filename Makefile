# Variables
BIN_DIR = bin
SRC_DIR = src
LIB_DIR = lib
JAR_FILE = $(LIB_DIR)/ojdbc6.jar

CLASSPATH = $(BIN_DIR):$(JAR_FILE)

all: connexion generate

connexion:
	javac -d $(BIN_DIR) -classpath $(JAR_FILE) -sourcepath $(SRC_DIR) $(SRC_DIR)/lecteur.java

run: connexion
	java -classpath $(CLASSPATH) lecteur

generate:
	javac -d $(BIN_DIR) -sourcepath generation generation/recup_cat.java

generate_run: generate
	java -classpath $(BIN_DIR) recup_cat

clean:
	rm -rf $(BIN_DIR)/*

.PHONY: all connexion run generate generate_run clean
