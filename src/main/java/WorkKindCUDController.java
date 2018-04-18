import hba.WorkKindEntity;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WorkKindCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkCUDController workCUDController;
    public WorkCUDController getWorkCUDController() {
        return workCUDController;
    }
    public void setWorkCUDController(WorkCUDController workCUDController) {
        this.workCUDController = workCUDController;
    }


    public void changeStateToWorkCUD() {
        Stage mainStage = mnApp.getPrimaryStage();
        mainStage.setScene(workCUDController.getOwnerScene());
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //Назад
    public void goBack() {
        changeStateToWorkCUD();
    }

    @FXML       //Сохранить
    public void goSave() {
        changeStateToWorkCUD();
    }

    @FXML       //Удалить
    public void goDelete() {
        changeStateToWorkCUD();
    }

    @FXML       //Удалить
    public void goAdd() {
        changeStateToWorkCUD();
    }

    @FXML private TextField txtfldName;
    @FXML private TextArea txtareaDescription;



    //Конкретный Вид работы
    private WorkKindEntity workKindEntity;
    public void setWorkKindEntity(WorkKindEntity workKindEntity) {
        this.workKindEntity = workKindEntity;
    }

    public void initWorkKindDataInTextFields() {
        this.txtfldName.setText(this.workKindEntity.getName());
        this.txtareaDescription.setText(this.workKindEntity.getDescription());
    }

}
