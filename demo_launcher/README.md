=== What is this?

Launcher that fires up datastax demo script and does the following

1. Checks wifi configuration, if not valid prompts to switch to it
2. Checks to see if clusters are available
3. Launches Cassandra on each
4. Launches appcontrol vm (assumes location is ~/Documents/Virtual Machines.localized/debian-7.0.0-amd64-minimal/debian-7.0.0-amd64-minimal.vmx )
5. Starts tomcat on appcontrol vm

on Shutdown

1. Turns off Tomcat on appcontrol vm
2. suspends VM
3. Reboots cassandra nodes to clear out running processes

Simulator

You can run and stop the simulation process, same button but it will interrupt an existing process

== Pending Issues

1. Configuration should be adjustable
2. Simulator is hanging for some reason, haven't looked into code to see why. Needs better error output.
3. Should have master build that compiles latest version of simulator, today have to add simulation.jar to resources file.
4. Um tests, this was slapped together pretty sloppily.
5. Better error handling of network.

== How to package

mvn package

== How to run package

java -jar target/launcher.jar


