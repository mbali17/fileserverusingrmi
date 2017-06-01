# fileserverusingrmi
This project is a command line based file server based on the client server architecture using the Java RMI to communicate and transfer objects between the client and server
The complete code base for both server and client side includes the following 
class files:
FileClient.java, FileServer.java, FileServerImpl.java,
FileInfo.java, FileOp.java,ProjectContants.java,StartServer.java,ClientHelper.java,WathchServiceHandlerThread.java.

Place the classes in a folder and compile them using the following command.

javac *.java -- this will compile all the classes. Inorder to separate the .java and .class file create a folder
for example classes and use the following command javac -d <path to the class folder(relative/absolute)> *.java.

 At the Server end.

   Step 1: After the classes are compiled. Start the rmi registry using one of the following commands based on the
   operating system. Windows:start rmiregistry and linux based: rmiregistry &, This will start the registry at the
   default port.
   
   Step 2: Start the fileserver using the following command java startserver (make sure you are in classes folder or
   the folder containing the compiled class files.). This will ask for the server ip and the object name to bind in
   the rmi registry enter appropriate values and the server is ready
   
   Step 3: Enter the path of the folder on the server to persist files.

 At the Client end.

    Step 1: start the client  using the following command java FileClient .
    Step 2: Client would ask for the IP of the server to connect and the object name registered at the server
    Step 3: The client can be started in either the menudriven mode or the dropbox mode. For the menudriven enter MD or
    DB for dropbox..
