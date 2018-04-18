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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            workKindCUDController.setWorkKindEntity(this.cmbWorkKind.getValue());
            workKindCUDController.initWorkKindDataInTextFields();
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
                workKindCUDController.setWorkKindEntity(this.cmbWorkKind.getValue());
                workKindCUDController.initWorkKindDataInTextFields();
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


    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDeleteWork;
    //
    @FXML private ComboBox<WorkKindEntity> cmbWorkKind;
    @FXML private ComboBox<BeehiveEntity> cmbBeehive;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private DatePicker dtpckrDateStart;
    @FXML private DatePicker dtpckrDateEnd;
    //

    private ObservableList<String> obserlistWorkStatus;
    private ObservableList<WorkKindEntity> obserlistWorkKind;
    private ObservableList<BeehiveEntity> obserlistBeehive;

    public StringConverter<WorkKindEntity> converterWorkKindName = new StringConverter<WorkKindEntity>() {
        @Override
        public String toString(WorkKindEntity workKindEntity) {
            return workKindEntity.getName();
        }
        @Override
        public WorkKindEntity fromString(String string) {
            return null;
        }
    };

    public StringConverter<BeehiveEntity> converterBeehiveNameType = new StringConverter<BeehiveEntity>() {
        @Override
        public String toString(BeehiveEntity beehiveEntity) {
            return beehiveEntity.getHiveNumber() + " " + beehiveEntity.getHiveType();
        }
        @Override
        public BeehiveEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс Улья (Beehive) в obserList соответствующий редактируемой Работе (Work)
    public int getIndexOfBeehive(WorkEntity workEntityChoose, ObservableList<BeehiveEntity> ObservableList){
        int index = -1;
        for (BeehiveEntity beehiveEntity : ObservableList)
            if (beehiveEntity.getIdBeehive() == workEntityChoose.getIdBeehive())
                index = ObservableList.indexOf(beehiveEntity);
        return index;
    }

    //получить индекс Вида работы (WorkKind) в obserList соответствующий редактируемой Работе (Work)
    public int getIndexOfWorkKind(WorkEntity workEntityChoose, ObservableList<WorkKindEntity> ObservableList){
        int index = -1;
        for (WorkKindEntity workKindEntity : ObservableList)
            if (workKindEntity.getIdWorkKind() == workEntityChoose.getIdWorkKind())
                index = ObservableList.indexOf(workKindEntity);
        return index;
    }

    //получить индекс Статуса в obserList<String> соответствующий редактируемой Работе (Work)
    public int getIndexOfWorkStatus(String statusChoose, ObservableList<String> ObservableList){
        int index = -1;
        for (String statusInObserList : ObservableList)
            if (statusInObserList.equals(statusChoose))
                index = ObservableList.indexOf(statusInObserList);
        return index;
    }

    @FXML
    private void initialize() { }

    public void initWorkDataInCombobox() {
        List<String> lst = new ArrayList<>();
        lst.add("Планируется");
        lst.add("В процессе");
        lst.add("Выполнена");
        this.obserlistWorkStatus = FXCollections.observableArrayList(lst);
        this.cmbStatus.getItems().clear();
        this.cmbStatus.setItems(this.obserlistWorkStatus);
        //
        this.obserlistWorkKind = FXCollections.observableArrayList(loadWorkKindList());
        this.cmbWorkKind.setConverter(converterWorkKindName);
        this.cmbWorkKind.getItems().clear();
        this.cmbWorkKind.setItems(obserlistWorkKind);
        //
        this.obserlistBeehive = FXCollections.observableArrayList(loadBeehiveList());
        this.cmbBeehive.setConverter(converterBeehiveNameType);
        this.cmbBeehive.getItems().clear();
        this.cmbBeehive.setItems(obserlistBeehive);
    }

    public void initWorkAddState() {
        this.lblcaptionTitle.setText("Добавить работу");
        this.hyperlinkAddOrEdit.setText("Добавить");
        this.hyperlinkDeleteWork.setVisible(false);
    }

    public void initWorkEditState() {
        this.lblcaptionTitle.setText("Изменить работу");
        this.hyperlinkAddOrEdit.setText("Изменить");
        this.hyperlinkDeleteWork.setVisible(true);
        //
        this.cmbStatus.getSelectionModel().select(getIndexOfWorkStatus(workEntity.getWorkStatus(), obserlistWorkStatus));
        this.cmbWorkKind.getSelectionModel().select(getIndexOfWorkKind(workEntity, obserlistWorkKind));
        this.cmbBeehive.getSelectionModel().select(getIndexOfBeehive(workEntity, obserlistBeehive));
        //
        this.dtpckrDateStart.setValue(workEntity.getDateStart());
        this.dtpckrDateEnd.setValue(workEntity.getDateEnd());
    }

    //Конкретная работа
    private WorkEntity workEntity = null;
    public void setWorkEntity(WorkEntity workEntity) {
        this.workEntity = workEntity;
    }

    private List<WorkKindEntity> workKindEntityList;
    //Получить список Работ
    public List<WorkKindEntity> loadWorkKindList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<WorkKindEntity> query = builder.createQuery(WorkKindEntity.class);
            Root<WorkKindEntity> root = query.from(WorkKindEntity.class);
            query.select(root);
            Query<WorkKindEntity> q = session.createQuery(query);
            this.workKindEntityList = q.getResultList();
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
        return this.workKindEntityList;
    }

    private List<BeehiveEntity> beehiveEntityList;
    //Получить список Работ
    public List<BeehiveEntity> loadBeehiveList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BeehiveEntity> query = builder.createQuery(BeehiveEntity.class);
            Root<BeehiveEntity> root = query.from(BeehiveEntity.class);
            query.select(root);
            Query<BeehiveEntity> q = session.createQuery(query);
            this.beehiveEntityList = q.getResultList();
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
        return this.beehiveEntityList;
    }

}
