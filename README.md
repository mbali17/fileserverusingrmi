# fileserverusingrmi
This project is a command line based file server implementing  client server architecture using the Java RMI protocol
to communicate and transfer objects between the client and server.
The complete code base for both server and client side includes the following java class files:

Client Implementation :FileClient.java,ClientHelper.java,WathchServiceHandlerThread.java.

Server Implementation :FileServer.java, FileServerImpl.java,StartServer.java

Interfaces :FileInfo.java, FileOp.java

Constants:ProjectContants.java,

Move to src directory/folder and compile using the following command.

javac *.java -- this will compile all the classes. Inorder to separate the .java and .class file create a folder
for example classes and use the following command javac -d <path to the class folder(relative/absolute)> *.java.

 At the Server end
 
   Step 1: After the classes are compiled. Start the rmi registry using one of the following commands based on the
   operating system.
   Windows:start rmiregistry
   linux based: rmiregistry &, This will start the registry at the default port.
   
   Step 2: Start the fileserver using the following command java startserver (make sure you are in classes folder or
   the folder containing the compiled class files.). This will ask for the server ip and the object name to bind in
   the rmi registry enter appropriate values and the server is ready
   
   Step 3: Enter the path of the folder on the server to persist files.

 At the Client end.

    Step 1: start the client  using the following command java FileClient .
    
    Step 2: Client would ask for the IP of the server to connect and the object name registered at the server
    
    Step 3: The client can be started in either the menu driven mode or the dropbox mode. For the menudriven enter MD or
    DB for dropbox.While in menu driven mode the application drives user action through menu interface and in the DropBox
    mode the client listens for changes in a particular directory using the WatchHandler service and any change in the
    directory would be updated at the server.
