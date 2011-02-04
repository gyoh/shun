package org.hamamoto.album.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileMonitor {

    private static final Log log = LogFactory.getLog(FileMonitor.class);

    private static final FileMonitor instance = new FileMonitor();

    private Timer timer;
    private Hashtable timerEntries;

    public static FileMonitor getInstance() {
        return instance;
    }

    protected FileMonitor() {
        // Create timer, run timer thread as daemon.
        timer = new Timer(true);
        timerEntries = new Hashtable();
    }

    /**
     * Add a fileMonitorTask to timerEntries.
     * 
     * @param listener listener to notify when the file changed.
     * @param fileName name of the file to monitor.
     * @param period polling period in milliseconds.
     */
    public void addFileMonitorTask(FileChangeListener listener,
            String fileName, long period) throws FileNotFoundException {
        removeFileMonitorTask(listener, fileName);
        FileMonitorTask task = new FileMonitorTask(listener, fileName);
        timerEntries.put(fileName + listener.hashCode(), task);
        timer.schedule(task, period, period);
        log.debug("added fileMonitorTask for " + fileName);
    }

    /**
     * Remove the fileMonitorTask from timerEntries.
     * 
     * @param listener listener to notify when the file changed.
     * @param fileName name of the file to monitor. 
     */
    public void removeFileMonitorTask(FileChangeListener listener,
            String fileName) {
        FileMonitorTask task = (FileMonitorTask) timerEntries.remove(fileName
                + listener.hashCode());
        if (task != null) {
            log.debug("canceling fileMonitorTask for " + task.fileName);
            task.cancel();
        }
        log.debug("removed fileMonitorTask for " + fileName);
    }

    protected void fireFileChangeEvent(FileChangeListener listener,
            String fileName) {
        log.debug("detected file change of " + fileName);
        listener.fileChanged(fileName);
    }

    class FileMonitorTask extends TimerTask {
        FileChangeListener listener;

        String fileName;
        File monitoredFile;
        long lastModified;

        public FileMonitorTask(FileChangeListener listener, String fileName)
                throws FileNotFoundException {
            this.listener = listener;
            this.fileName = fileName;
            this.lastModified = 0;

            monitoredFile = new File(fileName);
            if (!monitoredFile.exists()) {
                throw new FileNotFoundException("File Not Found: "
                        + fileName);
            }
            // the following line should be commented out
            // because we need to fire FileChangeEvent
            // the first time the task is created.
            //this.lastModified = monitoredFile.lastModified();
        }

        public void run() {
            long lastModified = monitoredFile.lastModified();
            log.debug(fileName + " lastModified: "
                        + this.lastModified + " -> " + lastModified);
            if (lastModified != this.lastModified) {
                this.lastModified = lastModified;
                fireFileChangeEvent(this.listener, this.fileName);
            }
        }
    }
}
