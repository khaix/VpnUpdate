package org.slack.vpnupdater.util;

import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download extends Task<Void> {
    private static final int BUFFER_SIZE = 1024;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private URL url;
    private int size;
    private int downloaded;

    public Download(URL url) {
        this.url = url;
        size = -1;
        downloaded = 0;
    }

    @Override
    protected Void call() {
        InputStream is;
        OutputStream os;
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
        try {
            URLConnection cnx = url.openConnection();
            cnx.setRequestProperty("User-Agent", USER_AGENT);
            if(size == -1){
                size = cnx.getContentLength();
            }
            is = cnx.getInputStream();
            os = new FileOutputStream(getFileName());
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > -1) {
                os.write(buffer, 0, length);
                downloaded += length;
                updateProgress(downloaded, size);
                updateMessage("Download finished!");
            }
            is.close();
            os.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getFileName(){
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') +1);
    }

}