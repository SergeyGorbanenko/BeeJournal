import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkListController workListController;
    private BorderPane workListLayout;
    private Scene workListScene;
    @FXML                               //[РАБОТЫ]
    public void changeStateToWorkList() {
        if (workListScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workListScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workListController.setMainController(this);
            workListController.setMainApp(mnApp);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkList.fxml"));
                workListLayout = (BorderPane) loader.load();
                workListScene = new Scene(workListLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workListScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workListController = loader.getController();
                workListController.setMainController(this);
                workListController.setMainApp(mnApp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}