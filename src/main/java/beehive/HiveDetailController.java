package beehive;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class HiveDetailController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private HiveListController hiveListController;
    public void setHiveListController(HiveListController hiveListController) {
        this.hiveListController = hiveListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToHiveList() {
        hiveListController.getMainController().changeStateToHiveList();
    }

    @FXML private Label lblTitleHiveNumber;


    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToHiveList();
    }

    @FXML       //[РЕД]
    public void goEdit() {

    }

}
