/* File server interface */
/* by Jia Rao */
import java.rmi.*;

/**
 * Interface defining the operations that can be performed.
 * @author ALI
 */
public interface FileServer extends Remote {
    /**
     * Download specific file from the server.
     * @param filename
     * @return
     * @throws RemoteException
     */
  FileInfo downloadFile(String filename) throws RemoteException;

    /**
     * upload a file from the local system to the server.
     * @param fileif
     * @return
     * @throws RemoteException
     */
  boolean uploadFile(FileInfo fileif) throws RemoteException;

    /**
     * Delete a file specified by the fileinfo
     * @param fileName
     * @return
     */
    boolean DeleteFile(String fileName) throws RemoteException;
    /**
     * Rename file that is existing on the server.
     * @param previousFileName
     * @param currentFileName
     * @return
     */
    boolean RenameFile(String previousFileName,String currentFileName) throws RemoteException;
}

