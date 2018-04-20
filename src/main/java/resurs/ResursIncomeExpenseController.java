package resurs;

import app.HBUtil;
import app.Main;
import hba.BeehiveEntity;
import hba.IncomeExpenseEntity;
import hba.WorkEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import work.WorkCUDController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResursIncomeExpenseController {

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


    @FXML
    private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private ComboBox<String> cmbOperationType;
    @FXML private ComboBox<BeehiveEntity> cmbBeehive;
    @FXML private TextField txtfldCount;
    @FXML private TextField txtfldDescription;
    @FXML private DatePicker dtpckrDate;

    private ObservableList<String> obserlistOperationType;
    private ObservableList<BeehiveEntity> obserlistBeehive;


    //Конкретная операция
    private IncomeExpenseEntity incomeExpenseEntity;
    public IncomeExpenseEntity getIncomeExpenseEntity() {
        return incomeExpenseEntity;
    }
    public void setIncomeExpenseEntity(IncomeExpenseEntity incomeExpenseEntity) {
        this.incomeExpenseEntity = incomeExpenseEntity;
    }

    public void initComboboxOperationType() {
        List<String> lst = new ArrayList<>();
        lst.add("Приход");
        lst.add("Расход");
        this.obserlistOperationType = FXCollections.observableArrayList(lst);
        this.cmbOperationType.getItems().clear();
        this.cmbOperationType.setItems(this.obserlistOperationType);
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        this.txtfldCount.setText(null);
        this.txtfldDescription.setText(null);
        this.dtpckrDate.setValue(null);
        //
        Stage mainStage = mnApp.getPrimaryStage();
        mainStage.setScene(resursHistoryController.getOwnerScene());    //TODO может быть пересмотреть переход
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {
/*        Transaction transaction = null;
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
        }*/
    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {
/*        Transaction transaction = null;
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
        }*/
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
/*        String resursName = null;
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
        }*/
    }

    //получить индекс Типа Операции в obserList<String> соответствующий редактируемому записи о приходе/расходе ресурса (IncomeExpense)
    public int getIndexOfOperationType(String typeChoose, ObservableList<String> ObservableList){
        int index = -1;
        for (String typeInObserList : ObservableList)
            if (typeInObserList.equals(typeChoose))
                index = ObservableList.indexOf(typeInObserList);
        return index;
    }

    //получить индекс Улья (Beehive) в obserList соответствующий редактируемой записи о приходе/расходу ресурса (IncomeExpense)
    public int getIndexOfBeehive(IncomeExpenseEntity incomeExpenseEntityChoose, ObservableList<BeehiveEntity> ObservableList){
        int index = -1;
        for (BeehiveEntity beehiveEntity : ObservableList)
            if (beehiveEntity.getIdBeehive() == incomeExpenseEntityChoose.getIdBeehive())
                index = ObservableList.indexOf(beehiveEntity);
        return index;
    }

    public void initIncomeExpenseAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить приход/расход");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
    }

    public void initIncomeExpenseEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить приход/расход");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        this.txtfldCount.setText(incomeExpenseEntity.getCount().toString());
        this.txtfldDescription.setText(incomeExpenseEntity.getDescription());
        this.dtpckrDate.setValue(incomeExpenseEntity.getDate());
        //
        if (incomeExpenseEntity.getOperationType())
            this.cmbOperationType.getSelectionModel().select(getIndexOfOperationType("Приход", obserlistOperationType));
        else
            this.cmbOperationType.getSelectionModel().select(getIndexOfOperationType("Расход", obserlistOperationType));
        //
        this.cmbBeehive.getSelectionModel().select(getIndexOfBeehive(incomeExpenseEntity, obserlistBeehive));
    }

    WorkCUDController workCUDController = new WorkCUDController();      //TODO если так будут баги при переходах, то писать все по ульям прямо тут
    public void initComboboxBeehive() {
        this.obserlistBeehive = FXCollections.observableArrayList(workCUDController.loadBeehiveList());
        this.cmbBeehive.setConverter(workCUDController.converterBeehiveNameType);
        this.cmbBeehive.getItems().clear();
        this.cmbBeehive.setItems(this.obserlistBeehive);
    }


}
