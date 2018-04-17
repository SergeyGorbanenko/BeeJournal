import hba.WorkEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WorkDetailController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkListController workListController;
    public void setWorkListController(WorkListController workListController) {
        this.workListController = workListController;
    }
    public WorkListController getWorkListController() {
        return workListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToWorkList() {
        workListController.getMainController().changeStateToWorkList();
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
            workCUDController.setWorkDetailController(this);
            workCUDController.setMainApp(mnApp);
            workCUDController.initWorkEditState();
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
                workCUDController.setWorkListController(workListController);
                workCUDController.setWorkDetailController(this);
                //controller.setMainController(null);
                workCUDController.setMainApp(mnApp);
                workCUDController.initWorkEditState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToWorkList();
    }

    @FXML       //[РЕД]
    public void goEdit() {
        changeStateToWorkCUD();
    }


    @FXML private Label lblvalueDateStart;
    @FXML private Label lblvalueDateEnd;
    @FXML private Label lblvalueBeehive;
    @FXML private Label lblvalueDescription;
    @FXML private Label lblvalueName;
    @FXML private Label lblvalueWorkStatus;

    public void initWorkDetailState() {
        this.lblvalueDateStart.setText("пусто");
        this.lblvalueDateEnd.setText("пусто");
        this.lblvalueBeehive.setText("пусто");
        this.lblvalueDescription.setText("пусто");
        this.lblvalueName.setText("пусто");
        this.lblvalueWorkStatus.setText("пусто");
    }

    public void fillWorkDetailState(WorkEntity workEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy", new Locale("ru", "RU"));
        this.lblvalueName.setText(workEntity.getWorkKindByIdWorkKind().getName());
        this.lblvalueDateStart.setText(workEntity.getDateStart().format(formatter));
        this.lblvalueDateEnd.setText(workEntity.getDateEnd().format(formatter));
        if (workEntity.getBeehive() != null)
            lblvalueBeehive.setText(workEntity.getBeehive().getHiveNumber());
        else
            this.lblvalueBeehive.setText("нет");
        this.lblvalueWorkStatus.setText(workEntity.getWorkStatus());
        this.lblvalueDescription.setText(workEntity.getWorkKindByIdWorkKind().getDescription());
    }

}
