package com.fileserver.interfaces.impl;
import com.fileserver.interfaces.FileInfo;
import com.fileserver.interfaces.server.FileServer;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;

public class FileServerImpl extends UnicastRemoteObject
implements FileServer {

	static String folderPathOnTheServer;
	public FileServerImpl() throws RemoteException {
		super();
	}

	/**
	 * Accept input from the user and initialize the path on the server.
	 */
	public static void initialiazeFolderPathOnTheServer(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the folder path on the server to persist the files");
		String pathEnteredBytheUser = scanner.nextLine();
		try {
		 	folderPathOnTheServer = Paths.get(pathEnteredBytheUser).toString();
			File chekifValidDirectory = new File(folderPathOnTheServer);
			boolean isValidDirectory = false;
			while (!isValidDirectory){
				if(!chekifValidDirectory.isDirectory()) {
					System.out.println("Please enter a valid directory Path");
					folderPathOnTheServer = scanner.nextLine();
					chekifValidDirectory = new File(folderPathOnTheServer);
				}else {
					isValidDirectory = true;
				}
			}
			System.out.println("Folder Path set successfully let's start performing operations.");
		}catch (InvalidPathException | NullPointerException e){
			System.out.println("java.nio.file.InvalidPathException or " +
					"java.lang.NullPointerException occurred while initiating the directory"+e.getMessage());
			System.out.println("Please enter correct location exiting the application.");
			System.exit(0);
		}
	}
	public synchronized FileInfo downloadFile(String filename) throws RemoteException {
		System.out.println("downloading file");
		BufferedInputStream fileInStream = null;
		try {
			File fileToBeDownloaded = new File(folderPathOnTheServer + File.separatorChar + filename);
			if (fileToBeDownloaded != null && fileToBeDownloaded.exists()) {
				FileInfo downloadFileInfo = new FileOp();
				String fileName = fileToBeDownloaded.getName();
				byte[] fileContent = new byte[(int)fileToBeDownloaded.length()];
				fileInStream = new BufferedInputStream(new FileInputStream(fileToBeDownloaded));
				if(fileInStream != null) {
                    fileInStream.read(fileContent);
					downloadFileInfo.setInfo(fileName,fileContent);
                    return downloadFileInfo;
				}
			}
		}catch (IOException e){
			System.out.println("java.io.IoException occurred while reading" +
					" content of the file for download"+ e.getMessage());
			e.printStackTrace();
		}finally {
			if (fileInStream != null){
				try {
					fileInStream.close();
				} catch (IOException e) {
					System.out.println("java.io.IoException while closing the stream in downloadfile"+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public synchronized boolean uploadFile(FileInfo fileif) throws RemoteException{
		System.out.println("putting file");
		File newFileTobeUpdatedIntheServer = new File(folderPathOnTheServer+File.separatorChar+fileif.getName());
		if (newFileTobeUpdatedIntheServer != null){
			try {
				newFileTobeUpdatedIntheServer.createNewFile();
				if(newFileTobeUpdatedIntheServer.canWrite()){
					FileOutputStream fileOutputStream = new FileOutputStream(newFileTobeUpdatedIntheServer);
					fileOutputStream.write(fileif.getContent());
					fileOutputStream.close();
					System.out.println("File created successfull");
					return true;
				}
			} catch (IOException e) {
				System.out.println("java.io.IOException while uploading file"+e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public synchronized boolean RenameFile(String previousFileName,String updatedFileName) throws RemoteException{
		System.out.println("Renaming file");
		//File to be renamed.
		File fileToBeRenamed = new File(folderPathOnTheServer+File.separatorChar+previousFileName);
		//New file name.
		File newfFileName = new File(folderPathOnTheServer+File.separatorChar+updatedFileName);
		if(fileToBeRenamed != null){
			fileToBeRenamed.renameTo(newfFileName);
		}
		return false;
	}

	public synchronized boolean DeleteFile(String fileName) throws RemoteException{
		System.out.println("DELETING file");
		File fileToBeDeleted = new File(folderPathOnTheServer+File.separatorChar+fileName);
		if(fileToBeDeleted.exists()){
			return fileToBeDeleted.delete();
		}
		return false;
	}

}


