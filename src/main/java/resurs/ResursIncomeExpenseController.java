package resurs;

import app.HBUtil;
import app.Main;
import hba.BeehiveEntity;
import hba.IncomeExpenseEntity;
import hba.ResourceTypeEntity;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResursIncomeExpenseController {

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

    //Конкретный тип ресурса
    private ResourceTypeEntity resourceTypeEntity = null;
    public ResourceTypeEntity getResourceTypeEntity() {
        return resourceTypeEntity;
    }
    public void setResourceTypeEntity(ResourceTypeEntity resourceTypeEntity) {
        this.resourceTypeEntity = resourceTypeEntity;
    }

    //Конкретная операция
    private IncomeExpenseEntity incomeExpenseEntity = null;
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
        mainStage.setScene(resursHistoryController.getOwnerScene());    //TODO если не заработает, то, может быть, пересмотреть переход
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDate.getValue() == null || dtpckrDate.getValue().equals("")) throw new Exception();
            if (txtfldCount.getText() == null || txtfldCount.getText().equals("")) throw new Exception();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new Exception();
            //
            if(cmbOperationType.getValue().equals("Приход") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            if(cmbOperationType.getValue().equals("Расход") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            //
            incomeExpenseEntity = new IncomeExpenseEntity();
            incomeExpenseEntity.setDate(dtpckrDate.getValue());
            incomeExpenseEntity.setCount(Integer.valueOf(txtfldCount.getText()));
            incomeExpenseEntity.setDescription(txtfldDescription.getText());
            if (cmbOperationType.getValue().equals("Приход"))
                incomeExpenseEntity.setOperationType(true);
            if (cmbOperationType.getValue().equals("Расход"))
                incomeExpenseEntity.setOperationType(false);
            incomeExpenseEntity.setIdBeehive(cmbBeehive.getValue().getIdBeehive());
            incomeExpenseEntity.setBeehive(cmbBeehive.getValue());
            incomeExpenseEntity.setIdBeegarden(workCUDController.loadBeegarden().getIdBeegarden());
            incomeExpenseEntity.setIdResourseType(resourceTypeEntity.getIdResourseType());
            incomeExpenseEntity.setResourceTypeByIdResourseType(resourceTypeEntity);
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(incomeExpenseEntity);
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
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- в поле \"Количество\" допустимы только числа\n" +
                    "- дата прихода/расхода не может быть позже текущей даты.");
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
            if (dtpckrDate.getValue() == null || dtpckrDate.getValue().equals("")) throw new Exception();
            if (txtfldCount.getText() == null || txtfldCount.getText().equals("")) throw new Exception();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new Exception();
            //
            if(cmbOperationType.getValue().equals("Приход") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            if(cmbOperationType.getValue().equals("Расход") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            //
            incomeExpenseEntity.setDate(dtpckrDate.getValue());
            incomeExpenseEntity.setCount(Integer.valueOf(txtfldCount.getText()));
            incomeExpenseEntity.setDescription(txtfldDescription.getText());
            if (cmbOperationType.getValue().equals("Приход"))
                incomeExpenseEntity.setOperationType(true);
            if (cmbOperationType.getValue().equals("Расход"))
                incomeExpenseEntity.setOperationType(false);
            incomeExpenseEntity.setIdBeehive(cmbBeehive.getValue().getIdBeehive());
            incomeExpenseEntity.setBeehive(cmbBeehive.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(incomeExpenseEntity);
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
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- в поле \"Количество\" допустимы только числа\n" +
                    "- дата прихода/расхода не может быть позже текущей даты.");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String ieType = null;
        String ieCount = null;
        try {
            ieCount = String.valueOf(incomeExpenseEntity.getCount());
            if (incomeExpenseEntity.getOperationType()) ieType = "Приход";
            else                                        ieType = "Расход";
            if (ieCount == null || ieType == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить приход/расход\"");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление записи из истории ресурса");
        alertSure.setHeaderText("Удалить запись " + "[" + ieType + " " + ieCount + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить запись " + "[" + ieType + " " + ieCount + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                session.delete(this.incomeExpenseEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Запись из истории ресурса удалена");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Запись " + "[" + ieType + " " + ieCount + "]" + " была успешна удалена из истории ресурса!");
                alertSuccess.showAndWait();
                //
                resursListController.changeStateToResursHistory(this.resourceTypeEntity);
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить приход/расход\"\n");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
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
        //
        this.txtfldCount.setText(null);
        this.txtfldDescription.setText(null);
        this.dtpckrDate.setValue(null);
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
