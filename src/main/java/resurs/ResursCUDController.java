package resurs;

import app.HBUtil;
import app.Main;
import hba.ResourceTypeEntity;
import hba.WorkEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import jdk.management.resource.ResourceType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResursCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private ResursListController resursListController;
    public void setResursListController(ResursListController resursListController) {
        this.resursListController = resursListController;
    }

    private ResursHistoryController resursHistoryController;
    public void setResursHistoryController(ResursHistoryController resursHistoryController) {
        this.resursHistoryController = resursHistoryController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private TextField txtfldName;
    @FXML private TextField txtfldCategory;
    @FXML private TextField txtfldDescription;
    @FXML private ComboBox<String> cmbMeasure;
    private ObservableList<String> obserlistMeasure;


    //Конкретный тип ресурса
    private ResourceTypeEntity resourceTypeEntity = null;
    public ResourceTypeEntity getResourceTypeEntity() {
        return resourceTypeEntity;
    }
    public void setResourceTypeEntity(ResourceTypeEntity resourceTypeEntity) {
        this.resourceTypeEntity = resourceTypeEntity;
    }


    public void initComboboxMeasure() {
        List<String> lst = new ArrayList<>();
        lst.add("кг");
        lst.add("гр");
        lst.add("литр");
        lst.add("мл");
        lst.add("шт");
        lst.add("метр");
        this.obserlistMeasure = FXCollections.observableArrayList(lst);
        this.cmbMeasure.getItems().clear();
        this.cmbMeasure.setItems(this.obserlistMeasure);
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        this.txtfldName.setText(null);
        this.txtfldCategory.setText(null);
        this.txtfldDescription.setText(null);
        //
        Stage mainStage = mnApp.getPrimaryStage();
        if (resursHistoryController != null) {
            if (resursHistoryController.getOwnerScene() != null)
                mainStage.setScene(resursHistoryController.getOwnerScene());
        } else if (resursListController != null) {
            if (resursListController.getOwnerScene() != null)
                mainStage.setScene(resursListController.getOwnerScene());
        }
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtfldName.getText() == null || txtfldName.getText().equals("")) throw new Exception();
            if (txtfldCategory.getText() == null || txtfldCategory.getText().equals("")) throw new Exception();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new Exception();
            resourceTypeEntity = new ResourceTypeEntity();
            resourceTypeEntity.setName(this.txtfldName.getText());
            resourceTypeEntity.setCategory(this.txtfldCategory.getText());
            resourceTypeEntity.setDescription(this.txtfldDescription.getText());
            resourceTypeEntity.setMeasure(this.cmbMeasure.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(resourceTypeEntity);
            transaction.commit();
            //
            resursListController.changeStateToResursHistory(this.resourceTypeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }

    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (resourceTypeEntity == null) throw new Exception();
            if (txtfldName.getText() == null || txtfldName.getText().equals("")) throw new Exception();
            if (txtfldCategory.getText() == null || txtfldCategory.getText().equals("")) throw new Exception();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new Exception();
            resourceTypeEntity.setName(this.txtfldName.getText());
            resourceTypeEntity.setCategory(this.txtfldCategory.getText());
            resourceTypeEntity.setDescription(this.txtfldDescription.getText());
            resourceTypeEntity.setMeasure(this.cmbMeasure.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(resourceTypeEntity);
            transaction.commit();
            //
            resursListController.changeStateToResursHistory(this.resourceTypeEntity);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String resursName = null;
        String resursCategory = null;
        try {
            resursName = resourceTypeEntity.getName();
            resursCategory = resourceTypeEntity.getCategory();
            if (resursName == null || resursCategory == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Создать новый ресурс\"");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление ресурса");
        alertSure.setHeaderText("Удалить ресурс " + "[" + resursName + " " + resursCategory + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить вид работы " + "[" + resursName + " " + resursCategory + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!resourceTypeEntity.getIncomeExpensesByIdResourseType().isEmpty()) throw  new Exception();
                transaction = session.beginTransaction();
                session.delete(this.resourceTypeEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Ресурс удален");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Ресурс " + "[" + resursName + " " + resursCategory + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                //
                resursListController.getMainController().changeStateToResursList();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Создать новый ресурс\"\n" +
                        "- нельзя удалить ресурс, у которого есть история приходов/расходов");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
    }

    //получить индекс Ед.Измерения в obserList<String> соответствующий редактируемому ресурсу (ResourceType)
    public int getIndexOfMeasure(String measureChoose, ObservableList<String> ObservableList){
        int index = -1;
        for (String measureInObserList : ObservableList)
            if (measureInObserList.equals(measureChoose))
                index = ObservableList.indexOf(measureInObserList);
        return index;
    }


    public void initResursAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить ресурс");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
    }

    public void initResursEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить ресурс");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        this.txtfldName.setText(resourceTypeEntity.getName());
        this.txtfldCategory.setText(resourceTypeEntity.getCategory());
        this.txtfldDescription.setText(resourceTypeEntity.getDescription());
        //
        this.cmbMeasure.getSelectionModel().select(getIndexOfMeasure(resourceTypeEntity.getMeasure(), obserlistMeasure));
    }

}
