package finans;

import app.HBUtil;
import app.Main;
import app.ServiseUtil;
import hba.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FinansCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private FinansListController finansListController;
    public void setFinansListController(FinansListController finansListController) {
        this.finansListController = finansListController;
    }

    private FinansDetailController finansDetailController;
    public void setFinansDetailController(FinansDetailController finansDetailController) {
        this.finansDetailController = finansDetailController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }


    @FXML       //[НАЗАД]
    public void goBack() {
        Stage mainStage = mnApp.getPrimaryStage();
        if (finansDetailController != null) {
            if (finansDetailController.getOwnerScene() != null)
                mainStage.setScene(finansDetailController.getOwnerScene());
        } else if (finansListController != null) {
            if (finansListController.getOwnerScene() != null)
                mainStage.setScene(finansListController.getOwnerScene());
        }
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {

    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {

    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {

    }


    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private ComboBox<String> cmbOperationType;
    @FXML private ComboBox<ResourceTypeEntity> cmbResurs;
    @FXML private TextField txtfldCount;
    @FXML private TextField txtfldUnitPrice;
    @FXML private TextField txtfldDescription;
    @FXML private DatePicker dtpckrDate;
    //

    private ObservableList<String> obserlistOperationType;
    private ObservableList<ResourceTypeEntity> obserlistResursType;

    public StringConverter<ResourceTypeEntity> converterResursName = new StringConverter<ResourceTypeEntity>() {
        @Override
        public String toString(ResourceTypeEntity resourceTypeEntity) {
            return resourceTypeEntity.getName();
        }
        @Override
        public ResourceTypeEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс Ресурса (ResourceType) в obserList соответствующий редактируемой Фин.Операции (FinancialOperate)
    public int getIndexOfResurs(ResourceTypeEntity resourceTypeEntityChoose, ObservableList<ResourceTypeEntity> ObservableList){
        int index = -1;
        for (ResourceTypeEntity resourceTypeEntity : ObservableList)
            if (resourceTypeEntity.getIdResourseType().equals(resourceTypeEntityChoose.getIdResourseType()))
                index = ObservableList.indexOf(resourceTypeEntity);
        return index;
    }



    //Конкретная финансовая операция
    private FinancialOperateEntity financialOperateEntity = null;
    public FinancialOperateEntity getFinancialOperateEntity() {
        return financialOperateEntity;
    }
    public void setFinancialOperateEntity(FinancialOperateEntity financialOperateEntity) {
        this.financialOperateEntity = financialOperateEntity;
    }


    //Инициализирует Комбобоксы данными
    public void initFinansDataInCombobox() {
        List<String> lst = new ArrayList<>();
        lst.add("Покупка");
        lst.add("Продажа");
        this.obserlistOperationType = FXCollections.observableArrayList(lst);
        this.cmbOperationType.getItems().clear();
        this.cmbOperationType.setItems(this.obserlistOperationType);
        //
        this.obserlistResursType = FXCollections.observableArrayList(loadResursList());
        this.cmbResurs.setConverter(converterResursName);
        this.cmbResurs.getItems().clear();
        this.cmbResurs.setItems(obserlistResursType);
    }

    //Инициализирует элементы управления на добавление записи
    public void initFinansAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить фин. операцию");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
    }

    //Инициализирует элементы управления на редактирование записи
    public void initFinansEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить фин. операцию");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        if (financialOperateEntity.getOperationType())
            this.cmbOperationType.getSelectionModel().select(1);
        else
            this.cmbOperationType.getSelectionModel().select(0);
        this.cmbResurs.getSelectionModel().select(getIndexOfResurs(financialOperateEntity.getResourceTypeByIdResourseType(), obserlistResursType));
        this.txtfldCount.setText(ServiseUtil.cutZero(financialOperateEntity.getCount()));
        this.txtfldUnitPrice.setText(ServiseUtil.cutZero(financialOperateEntity.getUnitPrice()));
        this.txtfldDescription.setText(financialOperateEntity.getDescription());
        this.dtpckrDate.setValue(financialOperateEntity.getDate());
    }




    private List<ResourceTypeEntity> resourceTypeEntityList;
    //Получить список Ресурсов
    public List<ResourceTypeEntity> loadResursList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ResourceTypeEntity> query = builder.createQuery(ResourceTypeEntity.class);
            Root<ResourceTypeEntity> root = query.from(ResourceTypeEntity.class);
            query.select(root);
            Query<ResourceTypeEntity> q = session.createQuery(query);
            this.resourceTypeEntityList = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return this.resourceTypeEntityList;
    }

}
