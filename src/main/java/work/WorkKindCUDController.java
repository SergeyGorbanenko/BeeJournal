package work;

import app.HBUtil;
import app.Main;
import hba.WorkKindEntity;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        if (workCUDController.getWorkEntity() == null)
            workCUDController.initWorkAddState();
        else
            workCUDController.initWorkEditState();
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        this.txtfldName.setText(null);
        this.txtareaDescription.setText(null);
        changeStateToWorkCUD();
    }

    @FXML       //[ДОБАВИТЬ НОВЫЙ ВИД]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtfldName.getText() == null || txtfldName.getText().equals("")) throw new Exception();
            if (txtareaDescription.getText() == null || txtareaDescription.getText().equals("")) throw new Exception();
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
            if (workKindEntity == null) throw new Exception();
            if (txtfldName.getText() == null || txtfldName.getText().equals("")) throw new Exception();
            if (txtareaDescription.getText() == null || txtareaDescription.getText().equals("")) throw new Exception();
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
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- нельзя изменить несуществующую запись, сперва выполите \"Добавить новый вид работы\"");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String workKindName = null;
        try {
            workKindName = workKindEntity.getName();
            if (workKindName == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить новый вид работы\"");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление вида работы");
        alertSure.setHeaderText("Удалить вид работы " + "[" + workKindName + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить вид работы " + "[" + workKindName + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!workKindEntity.getWorksByIdWorkKind().isEmpty()) throw  new Exception();
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
                alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить новый вид работы\"\n" +
                        "- нельзя удалить вид работы, по которому есть текущие работы");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
    }

    @FXML private Label lblcaptionWorkKindName;
    @FXML private TextField txtfldName;
    @FXML private TextArea txtareaDescription;


    //Конкретный Вид работы
    private WorkKindEntity workKindEntity;
    public void setWorkKindEntity(WorkKindEntity workKindEntity) {
        this.workKindEntity = workKindEntity;
    }

    public void initWorkKindDataInTextFields() {
        try {
            this.lblcaptionWorkKindName.setText(this.workKindEntity.getName());
            this.txtfldName.setText(this.workKindEntity.getName());
            this.txtareaDescription.setText(this.workKindEntity.getDescription());
        } catch (Exception e) {
            this.lblcaptionWorkKindName.setText("");
        }
    }

}
