package org.slack.vpnupdater.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slack.vpnupdater.MainApp;
import org.slack.vpnupdater.model.Vpn;
import org.slack.vpnupdater.util.FileUtil;

import java.io.IOException;
import java.util.List;


public class VpnOverviewController {
    @FXML
    private TableView<Vpn> vpnTable;
    @FXML
    private TableColumn<Vpn, String> countryColumn;
    @FXML
    private TableColumn<Vpn, String> ipColumn;
    @FXML
    private TableColumn<Vpn, String> protocolColumn;
    @FXML
    private Label countryLabel;
    @FXML
    private Label ipLabel;
    @FXML
    private Label portLabel;
    @FXML
    private Label protocolLabel;
    @FXML
    private Label ddosLabel;
    @FXML
    private Label uuid;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        vpnTable.setItems(mainApp.getVpnData());
    }
    public VpnOverviewController() {}
    private void showVpnDetails(Vpn vpn) {
        if (vpn != null) {
            countryLabel.setText(vpn.getCountry());
            ipLabel.setText(vpn.getIp());
            portLabel.setText(vpn.getPort());
            protocolLabel.setText(vpn.getProtocol());
            ddosLabel.setText(Boolean.toString(vpn.isDdos()));
            uuid.setText(vpn.getUuid());
        } else {
            countryLabel.setText("");
            ipLabel.setText("");
            portLabel.setText("");
            protocolLabel.setText("");
            ddosLabel.setText("");
            uuid.setText("");
        }
    }

    @FXML
    private void initialize(){
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        ipColumn.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        protocolColumn.setCellValueFactory(cellData -> cellData.getValue().protocolProperty());

        showVpnDetails(null);
        vpnTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showVpnDetails(newValue));

    }
    @FXML
    private void handleNewVpn(){
        Vpn tempVpn = new Vpn();
        boolean okClicked = mainApp.showVpnEditDialog(tempVpn);
        if (okClicked)
            mainApp.getVpnData().add(tempVpn);


    }
    @FXML
    private void handleEditVpn(){
        Vpn selectedVpn = vpnTable.getSelectionModel().getSelectedItem();
        if (selectedVpn != null){
            boolean okClicked = mainApp.showVpnEditDialog(selectedVpn);
            if (okClicked){
                showVpnDetails(selectedVpn);
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No VPN Selected");
            alert.setContentText("Please select a VPN in the table");
            alert.showAndWait();
        }

    }
    @FXML
    private void handleDeleteVpn() {
        int selectedIndex = vpnTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0) {
            vpnTable.getItems().remove(selectedIndex);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No VPN selected");
            alert.setContentText("Please select a VPN in the table");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleWriteConfig() throws IOException {
        List<Vpn> vpns = mainApp.getVpnData();
        FileUtil.writeConfigFile(vpns, mainApp.getPrimaryStage());
    }

}
