# remote-robot-demo

This project stand for to prove a concept of remote fixtures for Swing testing (based on FEST library). 

## What is implemented now: 
1. Remote lambda transferring and invoking. Start server with `server.FestServerStarter` and pass printed port to the system input of the started in separated JVM process `client.FestClientStarter`. 
