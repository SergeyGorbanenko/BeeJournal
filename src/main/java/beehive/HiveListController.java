package beehive;

import app.Main;
import app.MainController;
import javafx.fxml.FXML;
import javafx.scene.Scene;

public class HiveListController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public MainController getMainController() {
        return mainController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    @FXML
    public void changeStateToHome() {
        mnApp.initRootLayout();
    }


    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML       //[СОЗДАТЬ НОВЫЙ УЛЕЦ]
    public void goAddHive() {

    }

}
