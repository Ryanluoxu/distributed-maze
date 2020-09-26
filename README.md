# distributed-maze
A Peer-to-Peer Distributed Maze Game


# Demo
- mkdir ~/demo
- download source code, unzip and put them in ~/demo/src
- cd ~/demo/src
- javac -d ~/demo Tracker.java Game.java
- cd ~/demo
- rmiregistry &
- 

## Compile source
goto .../distributed-maze/src/main/java

```shell script
mkdir ~/demo
javac -d ~/demo Tracker.java Game.java
```

# Launch Game 
0. go to demo dir: `cd ~/demo`
1. run rmi-registry: `rmiregistry &`
2. run Tracker: `java -classpath ./ -Djava.rmi.server.codebase=file:./ Tracker 2001 15 10`
3. each player run Game to join: `java -classpath ~/demo Game 127.0.0.1 2001 xx`
