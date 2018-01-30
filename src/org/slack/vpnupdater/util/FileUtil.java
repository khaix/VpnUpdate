package org.slack.vpnupdater.util;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slack.vpnupdater.MainApp;
import org.slack.vpnupdater.model.Vpn;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static List<String> model = new ArrayList<>();
    private static final String VPN_DIR = "vpn";
    private MainApp mainApp;


    public static void writeConfigFile(List<Vpn> vpnList, Stage stage) throws IOException {
        PrintWriter writer;
        File file ;
        model = getModel();
        String line;
        String currentLine;
        File folder = new File(VPN_DIR);
        if (folder.exists()) folder.delete();
        folder.mkdirs();
        if(vpnList.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("Empty VPN table");
            alert.setHeaderText("The VPN table are empty");
            alert.setContentText("Please update your VPN table");
            alert.showAndWait();
            return ;
        }

        for (int i =0; i < vpnList.size(); i++) {
            file = new File("vpn/" + vpnList.get(i).getCountry());
            writer = new PrintWriter(file);
            for (int j = 0; j < model.size(); j++){
                currentLine = model.get(j);
                if(currentLine.startsWith("id="))
                    line = currentLine + vpnList.get(i).getCountry();
                else if(currentLine.startsWith("remote="))
                    line = currentLine + vpnList.get(i).getIp() + ":" + vpnList.get(i).getPort();
                else if(currentLine.startsWith("uuid="))
                    line = currentLine + vpnList.get(i).getUuid();
                else if(currentLine.startsWith("proto-tcp=")){
                    if(vpnList.get(i).getProtocol().equals("tcp")) {
                        line = currentLine + "yes";
                    }else {
                        continue;
                    }
                }else
                    line = currentLine;
                writer.println(line);

            }
            FileUtil.systemCommand("chmod 0600 " + file);
            writer.close();
        }
    }

    private static List<String> getModel() throws IOException{
        FileInputStream is = new FileInputStream("resources/files/model");
        FileWriter fw = new FileWriter("test.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String strLine;
        while ((strLine = br.readLine()) != null) {
            model.add(strLine);
        }
        fw.close();
        br.close();
        return model;
    }
    public static String systemCommand(String command){
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            output.append(reader.readLine());
        }catch (Exception e){
            e.printStackTrace();
        }
        return output.toString();
    }

}
