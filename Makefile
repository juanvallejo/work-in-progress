COMPILER=javac
ECJ_COMPILER=java -jar lib/ecj-4.2.2.jar
ECJ_FLAGS= -time -source 1.7
DIR=-d bin
CLASSPATH= -cp "data/libs/*"
SOURCEPATH= -sourcepath src src/com/rs/*.java src/com/rs/tools/*.java src/com/rs/utils/*.java src/com/rs/game/npc/combat/impl/*.java src/com/rs/game/player/content/*.java src/com/rs/game/minigames/duel/*.java
COMPILER_FLAGS=$(DIR) $(CLASSPATH) $(SOURCEPATH)
RUNTIME=java
RUNTIME_HIGH_MEM_FLAGS=-Xmx2048M -Xms1024M
RUNTIME_LOW_MEM_FLAGS=-Xmx1024M
RUNTIME_CLASSPATH=-classpath "bin:bin/com/mysql/jdbc/*:lib/*:data/libs/*"
RUNTIME_EDITOR_CLASSPATH=-classpath "bin:lib/*"

.PHONY: all all-ecj run run-editor

all:
	$(COMPILER) $(COMPILER_FLAGS)

all-ecj:
	$(ECJ_COMPILER) $(ECJ_FLAGS) $(COMPILER_FLAGS)

run:
	$(RUNTIME) $(RUNTIME_HIGH_MEM_FLAGS) $(RUNTIME_CLASSPATH) com.rs.Launcher true false false

run-lowmem:
	$(RUNTIME) $(RUNTIME_LOW_MEM_FLAGS) $(RUNTIME_CLASSPATH) com.rs.Launcher true false false

run-debug: run-lowmem

run-editor:
	$(RUNTIME) $(RUNTIME_FLAGS) $(RUNTIME_EDITOR_CLASSPATH) com.rs.tools.dropEditor true false false
