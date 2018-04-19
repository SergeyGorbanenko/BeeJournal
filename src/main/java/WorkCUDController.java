import hba.BeegardenEntity;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkListController workListController;
    public void setWorkListController(WorkListController workListController) {
        this.workListController = workListController;
    }

    private WorkDetailController workDetailController;
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

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDateEnd.getValue().isBefore(dtpckrDateStart.getValue())) throw new Exception();
            if (cmbStatus.getValue().equals("Планируется") && dtpckrDateStart.getValue().isBefore(LocalDate.now())) throw new Exception();
            if (cmbStatus.getValue().equals("Просрочена") && dtpckrDateStart.getValue().isAfter(LocalDate.now())) throw new Exception();
            workEntity = new WorkEntity();
            workEntity.setIdBeegarden(loadBeegarden().getIdBeegarden());
            workEntity.setIdBeehive(this.cmbBeehive.getValue().getIdBeehive());
            workEntity.setBeehive(this.cmbBeehive.getValue());
            workEntity.setIdWorkKind(this.cmbWorkKind.getValue().getIdWorkKind());
            workEntity.setWorkKindByIdWorkKind(this.cmbWorkKind.getValue());
            workEntity.setDateStart(this.dtpckrDateStart.getValue());
            workEntity.setDateEnd(this.dtpckrDateEnd.getValue());
            workEntity.setWorkStatus(this.cmbStatus.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(workEntity);
            transaction.commit();
            //
            workListController.changeStateToWorkDetail(this.workEntity);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- дата начала работы не может быть позже окончания\n" +
                    "- дата начала планируемой работы не может быть раньше сегодняшней даты\n" +
                    "- дата начала просроченной работы не может быть позже сегодняшней даты");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {
        Transaction transaction = null;
        Transaction transaction2 = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDateEnd.getValue().isBefore(dtpckrDateStart.getValue())) throw new Exception();
            if (cmbStatus.getValue().equals("Планируется") && dtpckrDateStart.getValue().isBefore(LocalDate.now())) throw new Exception();
            if (cmbStatus.getValue().equals("Просрочена") && dtpckrDateStart.getValue().isAfter(LocalDate.now())) throw new Exception();
            if (workEntity.getIdWorkKind() != cmbWorkKind.getValue().getIdWorkKind()) {
                transaction2 = session.getTransaction();
                transaction2.begin();
                session.delete(workEntity);
                transaction2.commit();
                //
                workEntity = new WorkEntity();
                workEntity.setIdBeehive(this.cmbBeehive.getValue().getIdBeehive());
                workEntity.setBeehive(this.cmbBeehive.getValue());
                workEntity.setIdWorkKind(this.cmbWorkKind.getValue().getIdWorkKind());
                workEntity.setWorkKindByIdWorkKind(this.cmbWorkKind.getValue());
                workEntity.setDateStart(this.dtpckrDateStart.getValue());
                workEntity.setDateEnd(this.dtpckrDateEnd.getValue());
                workEntity.setWorkStatus(this.cmbStatus.getValue());
                //
                transaction = session.getTransaction();
                transaction.begin();
                session.save(workEntity);
                transaction.commit();
                //
                workListController.changeStateToWorkDetail(this.workEntity);
            } else {
                workEntity.setIdBeehive(this.cmbBeehive.getValue().getIdBeehive());
                workEntity.setBeehive(this.cmbBeehive.getValue());
                workEntity.setDateStart(this.dtpckrDateStart.getValue());
                workEntity.setDateEnd(this.dtpckrDateEnd.getValue());
                workEntity.setWorkStatus(this.cmbStatus.getValue());
                //
                transaction = session.getTransaction();
                transaction.begin();
                session.update(workEntity);
                transaction.commit();
                //
                workListController.changeStateToWorkDetail(this.workEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- дата начала работы не может быть позже окончания\n"+
                    "- дата начала планируемой работы не может быть раньше сегодняшней даты\n"+
                    "- дата начала просроченной работы не может быть позже сегодняшней даты");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String workKindName = workEntity.getWorkKindByIdWorkKind().getName();
        String workStatus = workEntity.getWorkStatus();
        String dateStart = workEntity.getDateStart().toString();
        String dateEnd = workEntity.getDateEnd().toString();
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление работы");
        alertSure.setHeaderText("Удалить работу " + "[" + workKindName + ": " + workStatus + " - c " + dateStart + " по " + dateEnd + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить работу " + "[" + workKindName + ": " + workStatus + " - c " + dateStart + " по " + dateEnd + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                session.delete(this.workEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Работа удалена");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Работа" + "[" + workKindName + ": " + workStatus + " - c " + dateStart + " по " + dateEnd + "]" + " была успешно удалена!");
                alertSuccess.showAndWait();
                //
                workListController.getMainController().changeStateToWorkList();
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

    @FXML       //[Редактированить ВИД РАБОТЫ]
    public void goEditWorkKind() {
/*        try {     не применять: при удалении всех записей о виде работы - не пустит в сцену редактирования/создания вида работы
            if (this.cmbWorkKind.getValue() == null) throw new Exception();
        } catch (Exception e) {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Невозможно изменить пустой вид работы");
            alertError.setContentText("Чтобы изменить вид работы выберите его в списке \"Вид работы\"");
            alertError.showAndWait();
            return;
        }*/
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
        lst.add("Просрочена");
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
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить работу");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDeleteWork.setVisible(false);
    }

    public void initWorkEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить работу");
        this.hyperlinkAddOrEdit.setText("  Изменить");
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
    public WorkEntity getWorkEntity() {
        return workEntity;
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

    private BeegardenEntity beegardenEntity;
    //Получить пасеку
    public BeegardenEntity loadBeegarden() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BeegardenEntity> query = builder.createQuery(BeegardenEntity.class);
            Root<BeegardenEntity> root = query.from(BeegardenEntity.class);
            query.select(root);
            Query<BeegardenEntity> q = session.createQuery(query);
            this.beegardenEntity = null;
            this.beegardenEntity = q.getSingleResult();
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
        return this.beegardenEntity;
    }

}
