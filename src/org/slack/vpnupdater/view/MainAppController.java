package org.slack.vpnupdater.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slack.vpnupdater.MainApp;
import org.slack.vpnupdater.model.Vpn;
import org.slack.vpnupdater.util.Download;
import org.slack.vpnupdater.util.FileUtil;
import org.slack.vpnupdater.util.UnZip;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class MainAppController {
    private List<String> ovpnList;
    private URL url;
    private String zipFileName;
    private static final Pattern SPACE = Pattern.compile(" ");
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    @FXML
    private TextField urlField;
    @FXML
    private Button downloadBtn;
    @FXML
    private Button applyBtn;
    @FXML
    private ProgressBar downloadStatus;

    private MainApp mainApp;

    public MainAppController() {}
    @FXML
    private void initialize(){
        urlField.setText("https://143vpn.com/downloads/OpenVPNFiles.zip");
    }
    @FXML
    private void handleApply(){
        if(zipFileName == null)
            return;
        UnZip unzip = new UnZip(zipFileName);
        unzip.Extract();
        ovpnList = new ArrayList<>();
        ovpnList = unzip.getFileList();
        try {
            updateTableFromDownload();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDownload(){
        if(!urlField.getText().isEmpty()) {
            downloadBtn.setDisable(true);
            applyBtn.setDisable(false);
            try {
                url = new URL(urlField.getText());
                Download download = new Download(url);
                zipFileName = download.getFileName();
                downloadStatus.progressProperty().bind(download.progressProperty());
                executor.submit(download);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                downloadBtn.setDisable(false);
            }
        }
        downloadBtn.setDisable(false);
    }

    private void updateTableFromDownload() throws IOException {
        FileInputStream is;
        BufferedReader br;
        String fileName, strLine, country = null, ip = null, port = null, protocol = null, uuid = null;
        Boolean ddos = true;

        mainApp.getVpnData().clear();
        for (int i = 0; i < ovpnList.size(); i++) {
            fileName = ovpnList.get(i).split("/")[2];
            is = new FileInputStream(ovpnList.get(i));
            br = new BufferedReader(new InputStreamReader(is));
            while ((strLine = br.readLine()) != null) {
                if (strLine.startsWith("remote ")) {
                    ip = SPACE.split(strLine)[1];
                    port = SPACE.split(strLine)[2];
                } else if (strLine.startsWith("proto")) {
                    protocol = SPACE.split(strLine)[1].toUpperCase();
                }
                ddos = fileName.toLowerCase().contains("ddos");
                country = fileName.substring(0, fileName.lastIndexOf('.'));
                uuid = FileUtil.systemCommand("cat /proc/sys/kernel/random/uuid");
            }
            Vpn vpn = new Vpn(country, ip, port, protocol, ddos, uuid);
            mainApp.getVpnData().add(vpn);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void exitOnClose() {
        Platform.exit();
        System.exit(0);
    }

}
