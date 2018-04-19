package resurs;

import app.Main;
import javafx.scene.Scene;

public class ResursCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private ResursHistoryController resursHistoryController;
    public void setResursHistoryController(ResursHistoryController resursHistoryController) {
        this.resursHistoryController = resursHistoryController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

}
