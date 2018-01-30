package org.slack.vpnupdater.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slack.vpnupdater.model.Vpn;


public class VpnEditDialogController {
    @FXML
    private TextField countryField;
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private ChoiceBox protocolField;
    @FXML
    private CheckBox ddosField;

    private Stage dialogStage;
    private Vpn vpn;
    private Boolean isOkClicked = false;

    @FXML
    private void initialize() {
        protocolField.setItems(FXCollections.observableArrayList(
                "TCP", "UDP"));
    }

    @FXML
    private void handleOk() {
        vpn.setCountry(countryField.getText());
        vpn.setIp(ipField.getText());
        vpn.setPort(portField.getText());
        vpn.setProtocol(protocolField.getValue().toString().toLowerCase());
        vpn.setDdos(ddosField.isSelected());

        isOkClicked = true;
        dialogStage.close();
    }
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setVpn(Vpn vpn) {
        this.vpn = vpn;
        countryField.setText(vpn.getCountry());
        ipField.setText(vpn.getIp());
        portField.setText(vpn.getPort());
        protocolField.setValue(vpn.getProtocol());
        ddosField.setSelected(vpn.isDdos());
    }

    public boolean isOkClicked(){
        return isOkClicked;
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
