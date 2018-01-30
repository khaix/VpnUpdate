package org.slack.vpnupdater.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {
    private List<String> fileList;
    private String zipFile;
    private static final String OUTPUT_FOLDER = "tmp";
    private static final int BUFFER_SIZE = 1024;

    public UnZip(String zipFile) {
        this.zipFile = zipFile;
    }

    public boolean Extract() {
        fileList = new ArrayList<>();
        byte[] buffer = new byte[BUFFER_SIZE];
        try{
            File folder = new File(OUTPUT_FOLDER);
            if(folder.exists()) folder.delete();
            folder.mkdir();

            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
                if(ze.isDirectory()) {
                    newFile.mkdirs();
                    ze = zis.getNextEntry();
                    continue;
                }else{
                    //System.out.println("File unzip : "+ newFile.getAbsoluteFile());
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    if(fileName.endsWith(".ovpn"))
                        fileList.add(OUTPUT_FOLDER + File.separator + fileName);
                    fos.close();
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("Done!");
            return true;

        }catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<String> getFileList() {
        return fileList;
    }
}
