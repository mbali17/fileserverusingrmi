package com.fileserver.utility;

import com.fileserver.client.ClientHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 *Thread to check the events and perform relevant action in the watchservice queue.
 */
public class WatchServiceHandlerThread implements Runnable {

    private WatchService watchService;
    //obtain the watchservice to listen to.
    public WatchServiceHandlerThread(WatchService watchService){
        this.watchService = watchService;
    }

    /**
     *
     */
    @Override
    public void run() {
        while(true){
            WatchKey watchKey = null;
            try{
                watchKey = watchService.take();
            } catch (InterruptedException e) {
                System.out.println("java.lang.InterruptedException while getting the key"+e.getMessage());
                e.printStackTrace();
            }
            // perform relevant action based on the event that is occurred.
            if(watchKey!= null) {
                for (WatchEvent event : watchKey.pollEvents()) {
                    try {
                        handleEvent(event);
                    } catch (IOException e) {
                        System.out.println("java.io.IOException while handling event in the watcher thread.");
                        e.printStackTrace();
                    }
                }
                System.out.println("exited for loop");
            }
            System.out.println("Resetting the key to obtain subsequent events");
            //Very important:Reset the key to obtain the further events
            boolean reset = watchKey.reset();
            //Check if the directory is accessible, if not break the loop.
            if(!reset){
                break;
            }

        }

    }

    private void handleEvent(WatchEvent eventType) throws IOException {
        //get event name.
        String event = eventType.kind().name();
        //downcasting event type to get the context holding the filename.
        WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>)eventType;
        Path filename = pathWatchEvent.context();
        if(event.equals(ENTRY_MODIFY.name())){
            System.out.println("Following file updated"+filename);
            ClientHelper.uploadFile(ClientHelper.folderToSync+File.separatorChar+filename.toString());
        }else if(event.equals(ENTRY_DELETE.name())){
            System.out.println("Following file deleted"+filename);
            ClientHelper.deleteFile(filename.getFileName().toString());
        }else if(event.equals(ENTRY_CREATE.name())){
            System.out.println("Following file created"+filename);
            ClientHelper.uploadFile(ClientHelper.folderToSync+File.separatorChar+filename.toString());
        }
    }
}
