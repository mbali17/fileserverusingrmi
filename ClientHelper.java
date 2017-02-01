import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Helper class for the client.
 */
public class ClientHelper {

    static FileServer fileServer= null;

    static String folderToSync = null;
    /**
     *Displays menu to interact and returns the choice selected by the user.
     * @return {@code int choiceSelectedByTheUser}
     */

    public static int displayMainMenu(Scanner inpScanner) {
        System.out.println("Enter 1 to upload a file");
        System.out.println("Enter 2 to download a file");
        System.out.println("Enter 3 to Rename a file");
        System.out.println("Enter 4 to delete a file");
        System.out.println("Enter 5 to Quit the application");
        if (inpScanner.hasNextInt()){
            return inpScanner.nextInt();
        }else {
           // http://stackoverflow.com/questions/16040601/why-is-nextline-returning-an-empty-string --
            // reference for adding this extra line inpScanner.next().
            // nextInt() does not read end of line and subsequent next() call.
            //leads to reading an empty string .Leading to an infinite loop or ArrayIndexOutOfBoundException.
            inpScanner.next();
            displayMainMenu(inpScanner);
        }
        return 5;
    }

    /**
     * Based on the previous selection from the user. Display appropriate menu;
     * @param inputScanner -- Scanner to accept user input.
     * @param operationToPerform -- Current operation being performed
     */
    public static String[] displaySubMenu(Scanner inputScanner, int operationToPerform) {
        String inputsFromTheUser[] = new String[2];
        switch (operationToPerform){
            case 1:
                System.out.println("Enter the absolute(full) file path to be uploaded to the server ");
                inputsFromTheUser[0] = inputScanner.next();
                break;
            case 2:
                System.out.println("Enter the file name to be downloaded from the server");
                inputsFromTheUser[0] = inputScanner.next();
                break;
            case 3:
                System.out.println("Enter the file name to be renameed in the server");
                inputsFromTheUser[0] = inputScanner.next();
                System.out.println("Enter the new name of the file");
                inputsFromTheUser[1] = inputScanner.next();
                break;
            case 4:
                System.out.println("Enter the file name to be deleted from the server");
                inputsFromTheUser[0] = inputScanner.next();
                break;
            default:
                System.out.println("No Submenu for the selected operation.");
                break;
        }
        return inputsFromTheUser;
    }

    public static void performOperation(Scanner inputScanner, int operationToPerform) throws IOException {
        String[] inputs = ClientHelper.displaySubMenu(inputScanner, operationToPerform);
        if(inputs != null && inputs.length==2) {
            switch (operationToPerform) {
                case 1:
                    System.out.println("uploading a file :\t " + inputs[0]);
                    uploadFile(inputs[0]);
                    break;
                case 2:
                    System.out.println("Downloading a file");
                    downloadFile(inputs[0]);
                    break;
                case 3:
                    System.out.println("Renaming a file");
                    renameFile(inputs);
                    break;
                case 4:
                    System.out.println("Deleting a file");
                    deleteFile(inputs[0]);
                    break;
                default:
                    System.out.println("No operation  selected");
                    break;
            }
        }
    }

    public static void deleteFile(String input) {
        if(input!=null){
            try{
                if(fileServer.DeleteFile(input)){
                    System.out.println("Sucessfully deleted file "+ input);
                }else {
                    System.out.println("Something went wrong while deleting file");
                }
            } catch (RemoteException e) {
                System.out.println("java.rmi.RemoteException while renaming file");
                e.printStackTrace();
            }
        }
    }

    public static void renameFile(String[] inputs) {
        if(inputs != null){
            try {
                if(fileServer.RenameFile(inputs[0],inputs[1])){
                    System.out.println("renamed file sucessfully"+inputs[1]);
                }else{
                    System.out.println("Something went wrong while renaming file");
                }
            } catch (RemoteException e) {
                System.out.println("java.rmi.RemoteException while renaming file"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param input
     */
    public static void downloadFile(String input) {
        try {
            if (input != null || input != "") {
                FileInfo fileInfo = fileServer.downloadFile(input);
                if(fileInfo != null) {
                    File downloadedFile = new File(fileInfo.getName());
                    downloadedFile.createNewFile();
                    if(downloadedFile.canWrite()){
                        FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile);
                        fileOutputStream.write(fileInfo.getContent());
                        fileOutputStream.close();
                        System.out.println("File downloaded successfull");
                    }
                }
            }else {
                System.out.println("something went wrong while downloading file.Please try after sometime");
            }
        }
        catch (RemoteException e) {
            System.out.println("java.rmi.RemoteException while downloading file."+ e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("java.io.IOException while downloading file");
            e.printStackTrace();
        }
    }

    /**
     * Uploads the file to the server by invoking the remote object
     * @param filePath -- Path of the file to uploaded.
     * @throws IOException
     */
    public static void uploadFile(String filePath) throws IOException {
        Path inputPath = Paths.get(filePath);
        String absoluteFilePath = inputPath.toAbsolutePath().toString();
        File fileTobeUploaded = new File(absoluteFilePath);
        byte[] content = new byte[(int)fileTobeUploaded.length()];
        String fileTobeUploadedName = fileTobeUploaded.getName();
        BufferedInputStream fileContentInStream = new BufferedInputStream(new FileInputStream(absoluteFilePath));
        fileContentInStream.read(content);
        FileInfo fileInfo = new FileOp();
        fileInfo.setInfo(fileTobeUploadedName, content);
        if(fileServer.uploadFile(fileInfo)){
            System.out.println("File Successfully uploaded.");
        }else {
            System.out.println("Something went wrong while uploading please try again later.");
        }
        fileContentInStream.close();
    }

    /**
     * Accepts server details and connects to the server.
     * @param inputScanner
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public static void getServerDetailsAndConnectToServer(Scanner inputScanner) throws RemoteException,
            NotBoundException, MalformedURLException {
        //Get the ip address of the server to connect.
        System.out.println("Enter the Ip address of the server to be connected ");
        String ipAddress = inputScanner.next();
        System.out.println("Enter the remote object name that is regsitered\n(Enter the object name entered at  the server side)");
        String remoteObjectName = inputScanner.next();
        String url = ProjectConstants.FORWARD_SLASH+ProjectConstants.FORWARD_SLASH+ipAddress+
						 ProjectConstants.FORWARD_SLASH+remoteObjectName;
        System.out.println("Connecting to the server with url: "+url);
        fileServer = (FileServer) Naming.lookup(url);
        System.out.println("Remote server found and connected successfully");
        System.out.println("let's do some operations");
    }

    /**
     * Initiate the watch service and handler threads.
     * @param inputScanner
     * @throws IOException
     * @throws InterruptedException
     */
    public static void watchFolder(Scanner inputScanner) throws IOException, InterruptedException {
        System.out.println("Enter the path of the directory to sync to folder");
        folderToSync = inputScanner.next();
        System.out.println("Syncing folder \t" + folderToSync);
        //folder to watch.
        Path folderPathToWatchForUpdate = Paths.get(folderToSync);

        if(folderPathToWatchForUpdate != null){
            //create watchservice object
            WatchService watchService = folderPathToWatchForUpdate.getFileSystem().newWatchService();
            //start thread by passing the watchservice.
            WatchServiceHandlerThread watchServiceHandlerThread = new WatchServiceHandlerThread(watchService);
            Thread watcherThread =  new Thread(watchServiceHandlerThread);
            watcherThread.start();

            //register a folder to watch and the events associated.
            folderPathToWatchForUpdate.register(watchService,ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
            watcherThread.join();
        }
    }
}
