import hba.WorkKindEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class WorkKindCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkCUDController workCUDController;
    public void setWorkCUDController(WorkCUDController workCUDController) {
        this.workCUDController = workCUDController;
    }


    public void changeStateToWorkCUD() {
        Stage mainStage = mnApp.getPrimaryStage();
        mainStage.setScene(workCUDController.getOwnerScene());
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
        workCUDController.initWorkDataInCombobox();
        workCUDController.initWorkEditState();
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToWorkCUD();
    }

    @FXML       //[ДОБАВИТЬ НОВЫЙ ВИД]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            workKindEntity = new WorkKindEntity();
            workKindEntity.setName(txtfldName.getText());
            workKindEntity.setDescription(txtareaDescription.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(workKindEntity);
            transaction.commit();
            //
            changeStateToWorkCUD();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }

    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goSave() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            workKindEntity.setName(txtfldName.getText());
            workKindEntity.setDescription(txtareaDescription.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(workKindEntity);
            transaction.commit();
            //
            changeStateToWorkCUD();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }

    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String workKindName = workKindEntity.getName();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление вида работы");
        alertSure.setHeaderText("Удалить вид работы " + "[" + workKindName + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите вид работы " + "[" + workKindName + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                session.delete(this.workKindEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Вид работы удален");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Вид работы " + "[" + workKindName + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                //
                changeStateToWorkCUD();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("Что-то пошло не так(");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
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
