import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class WorkListController {

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

    private WorkDetailController workDetailController;
    private BorderPane workDetailLayout;
    private Scene workDetailScene;
    public void changeStateToWorkDetail() {
        if (workDetailScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workDetailScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workDetailController.setWorkListController(this);
            workDetailController.setMainApp(mnApp);
            workDetailController.initWorkDetailState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkDetail.fxml"));
                workDetailLayout = (BorderPane) loader.load();
                workDetailScene = new Scene(workDetailLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workDetailScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workDetailController = loader.getController();
                workDetailController.setWorkListController(this);
                workDetailController.setMainApp(mnApp);
                workDetailController.initWorkDetailState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private WorkCUDController workCUDController;
    private BorderPane workCUDLayout;
    private Scene workCUDScene;
    public void changeStateToWorkCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (workCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workCUDController.setWorkListController(this);     //чекать!
            workCUDController.setMainApp(mnApp);
            workCUDController.initWorkAddState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkCUD.fxml"));
                workCUDLayout = (BorderPane) loader.load();
                workCUDScene = new Scene(workCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workCUDController = loader.getController();
                //controller.setMainController(mainController);
                workCUDController.setWorkListController(this);     //чекать!
                //controller.setWorkDetailController(null);
                workCUDController.setMainApp(mnApp);
                workCUDController.initWorkAddState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML       //[ЭЛЕМЕНТ РАБОТЫ] - подробнее о работе
    public void goWorkDetail() {
        changeStateToWorkDetail();
    }

    @FXML       //[СОЗДАТЬ НОВУЮ РАБОТУ]
    public void goCreateNewWork() {
        changeStateToWorkCUD();
    }

    @FXML private ScrollPane scrlPane;


}
