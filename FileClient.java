
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Client program that establishes connection to the server and performs necessary action.
 */
public class FileClient {
	public static void main(String[]  args) {
		Scanner inputScanner = new Scanner(System.in);
		try {
			ClientHelper.getServerDetailsAndConnectToServer(inputScanner);
			System.out.println("Enter the string MD to get menu based program\n else enter DB for dropbox ");
			String programType = inputScanner.next();
			if(programType.equalsIgnoreCase(ProjectConstants.MENU_DRIVEN)){
				int operationToPerform =0;
				while (operationToPerform != 5){
					operationToPerform = ClientHelper.displayMainMenu(inputScanner);
					ClientHelper.performOperation(inputScanner,operationToPerform);
				}
			}else if(programType.equalsIgnoreCase(ProjectConstants.DROP_BOX)) {
				ClientHelper.watchFolder(inputScanner);
			}else {
				System.out.println("The value entered does not mean anything to the system.Please enter apropriate " +
						"inputs to continue.");
			}
		}catch(RemoteException re){
			System.out.println("java.rmi.RemoteException while connecting to server"+ re.getMessage());
			re.printStackTrace();
		} catch (NotBoundException nbe) {
			System.out.println("java.rmi.NotBoundException while connecting to server" + nbe.getMessage());
			nbe.printStackTrace();
		} catch (MalformedURLException murle) {
			System.out.println("java.net.MalformedURLException while connecting to the server" + murle.getMessage());
			murle.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("java.io.IOException while uploading file to the server" + ioe.getMessage());
			ioe.printStackTrace();
		}
		catch (Exception e) {
			System.err.println("FileServer exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
