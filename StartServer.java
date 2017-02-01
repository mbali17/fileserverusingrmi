import com.fileserver.interfaces.impl.FileServerImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class StartServer {
    public static void main(String args[])
    {
        try
        {
            //accept server ip to set the system property for the server to host with this IP.
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("Enter the server ip address");
            String serverIp = inputScanner.next();
            System.setProperty("java.rmi.server.hostname",serverIp);
            //create a local instance of the object
            FileServerImpl fileServer = new FileServerImpl();
            System.out.println("Enter the key under which the object is registered in rmiregistry\n" +
                    "This is the key is to be entered at the client side to connect to this server.");
            String objectName = inputScanner.next();
            //put the local instance in the registry.
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(objectName, fileServer);
            System.out.println("Server started successfully....");
            FileServerImpl.initialiazeFolderPathOnTheServer();
        }
        catch (AlreadyBoundException abe)
        {
            System.out.println("Malformed URL: " + abe.getMessage());
            abe.printStackTrace();
        }

        catch (RemoteException re)
        {
            System.out.println("Remote exception: " + re.getMessage());
            re.printStackTrace();

        }

    }

}
