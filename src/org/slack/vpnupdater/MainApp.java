package org.slack.vpnupdater;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slack.vpnupdater.model.Vpn;
import org.slack.vpnupdater.view.MainAppController;
import org.slack.vpnupdater.view.VpnEditDialogController;
import org.slack.vpnupdater.view.VpnOverviewController;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Vpn> vpnData = FXCollections.observableArrayList();

    public MainApp() {
        //vpnData.add(new Vpn());
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Vpn Updater");
        this.primaryStage.getIcons().add(new Image("file:resources/images/favicon.ico"));

        initRootLayout();
        showVpnOverview();
    }

    public ObservableList<Vpn> getVpnData() {
        return vpnData;
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            MainAppController mainAppController = loader.getController();
            mainAppController.setMainApp(this);
            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showVpnOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/VpnOverview.fxml"));
            AnchorPane vpnOverview = loader.load();
            rootLayout.setCenter(vpnOverview);

            VpnOverviewController vpnController = loader.getController();
            vpnController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean showVpnEditDialog(Vpn vpn) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/VpnEditDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit VPN");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            VpnEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setVpn(vpn);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
