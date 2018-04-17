import hba.BeehiveEntity;
import hba.WorkEntity;
import hba.WorkKindEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkCUDController {

        private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkListController workListController;
    public WorkListController getWorkListController() {
        return workListController;
    }
    public void setWorkListController(WorkListController workListController) {
        this.workListController = workListController;
    }

    private WorkDetailController workDetailController;
    public WorkDetailController getWorkDetailController() {
        return workDetailController;
    }
    public void setWorkDetailController(WorkDetailController workDetailController) {
        this.workDetailController = workDetailController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    private WorkKindCUDController workKindCUDController;
    private BorderPane workKindCUDLayout;
    private Scene workKindCUDScene;
    public void changeStateToWorkKindCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (workKindCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workKindCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workKindCUDController.setWorkCUDController(this);
            workKindCUDController.setMainApp(mnApp);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkKindCUD.fxml"));
                workKindCUDLayout = (BorderPane) loader.load();
                workKindCUDScene = new Scene(workKindCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workKindCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workKindCUDController = loader.getController();
                workKindCUDController.setWorkCUDController(this);
                workKindCUDController.setMainApp(mnApp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        Stage mainStage = mnApp.getPrimaryStage();
        if (workDetailController != null) {
            if (workDetailController.getOwnerScene() != null)
                mainStage.setScene(workDetailController.getOwnerScene());
        } else if (workListController != null) {
            if (workListController.getOwnerScene() != null)
                mainStage.setScene(workListController.getOwnerScene());
        }
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[СОХРАНИТЬ]
    public void goAddOrEdit() {
        workListController.changeStateToWorkDetail(this.workEntity);
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        workListController.getMainController().changeStateToWorkList();
    }

    @FXML       //[Редактированить ВИД РАБОТЫ]
    public void goEditWorkKind() {
        changeStateToWorkKindCUD();
    }

    private ObservableList<String> obserlistWorkStatus;
    @FXML
    private void initialize() {
        List<String > lst = new ArrayList<>();
        lst.add("Планируется");
        lst.add("В процессе");
        lst.add("Выполнена");
        this.obserlistWorkStatus = FXCollections.observableArrayList(lst);
        this.cmbStatus.getItems().clear();
        this.cmbStatus.setItems(this.obserlistWorkStatus);
        this.cmbStatus.getSelectionModel().select(0);
    }

    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDeleteWork;
    //
    @FXML private ComboBox<WorkKindEntity> cmbWorkKind;
    @FXML private ComboBox<BeehiveEntity> cmbBeehive;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private DatePicker dtpckrDateStart;
    @FXML private DatePicker dtpckrDateEnd;

    public void initWorkAddState() {
        this.lblcaptionTitle.setText("Добавить работу");
        this.hyperlinkAddOrEdit.setText("Добавить");
        this.hyperlinkDeleteWork.setVisible(false);
    }

    public void initWorkEditState() {
        this.lblcaptionTitle.setText("Изменить работу");
        this.hyperlinkAddOrEdit.setText("Изменить");
        this.hyperlinkDeleteWork.setVisible(true);
    }

    private WorkEntity workEntity;


}
