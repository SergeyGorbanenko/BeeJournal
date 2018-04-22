package app;

import beehive.HiveListController;
import hba.BeehiveEntity;
import hba.ResourceTypeEntity;
import hba.WorkEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import resurs.ResursListController;
import work.WorkListController;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class MainController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    /////////////////////////////////////////////////////////////
    //////                      РАБОТЫ                     //////
    /////////////////////////////////////////////////////////////
    private WorkListController workListController;
    private BorderPane workListLayout;
    private Scene workListScene;
    @FXML
    public void changeStateToWorkList() {
        //this.workEntityList = loadWorkList();   //TODO пофиксить показ работ по улью!
        this.checkStatusPlaning();
        if (workListScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workListScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workListController.setMainController(this);
            workListController.setMainApp(mnApp);
            workListController.viewWorks(this.workEntityList);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/work/WorkList.fxml"));
                workListLayout = (BorderPane) loader.load();
                workListScene = new Scene(workListLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workListScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workListController = loader.getController();
                workListController.setMainController(this);
                workListController.setMainApp(mnApp);
                workListController.viewWorks(this.workEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private Collection<WorkEntity> workEntityList;
    public void setWorkEntityList(Collection<WorkEntity> workEntityList) {
        this.workEntityList = workEntityList;
    }
    //Получить список Работ
    public Collection<WorkEntity> loadWorkList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<WorkEntity> query = builder.createQuery(WorkEntity.class);
            Root<WorkEntity> root = query.from(WorkEntity.class);
            query.select(root);
            Query<WorkEntity> q = session.createQuery(query);
            this.workEntityList = q.getResultList();
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
        return this.workEntityList;
    }

    //Получить список работ по определенному СТАТУСУ
    public Collection<WorkEntity> loadWorkList(String statusString) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<WorkEntity> query = builder.createQuery(WorkEntity.class);
            Root<WorkEntity> root = query.from(WorkEntity.class);
            query.select(root);
            query.where(builder.equal(root.get("workStatus"), statusString));
            Query<WorkEntity> q = session.createQuery(query);
            this.workEntityList = q.getResultList();
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
        return this.workEntityList;
    }

    //Проверка перед загрузкой списка работ: есть ли статусы несоответствующие своим датам
    public void checkStatusPlaning() {
        Transaction transaction = null;
        Session session = null;
        boolean flag = false;
        for (WorkEntity wrkE : workEntityList) {
            if ((wrkE.getWorkStatus().equals("Планируется") && wrkE.getDateEnd().isBefore(LocalDate.now())) ||
                    (wrkE.getWorkStatus().equals("В процессе") && wrkE.getDateEnd().isBefore(LocalDate.now()))) {
                flag = true;
                wrkE.setWorkStatus("Просрочена");
                try {
                    session = HBUtil.getSessionFactory().openSession();
                    transaction = session.getTransaction();
                    transaction.begin();
                    session.update(wrkE);
                    transaction.commit();
                }  catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null)
                        transaction.rollback();
                } finally {
                    if (session != null)
                        session.close();
                }
            }
        }
        if (flag) {
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Статус планируемых и текущий работ изменен");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Срок некоторых ваших планируемых и текущих работ истек.\n" +
                    "Статус изменен на \"Просрочен\"!");
            alertInfo.showAndWait();
        }
    }

    //кнопка [РАБОТЫ]
    public void goWorks() {
        loadWorkList();
        changeStateToWorkList();
    }
    /////////////////////////////////////////////////////////////
    //////                     РЕСУРСЫ                     //////
    /////////////////////////////////////////////////////////////
    private ResursListController resursListController;
    private BorderPane resursListLayout;
    private Scene resursListScene;
    @FXML
    public void changeStateToResursList() {
        this.resourceTypeEntityList = loadResursList();
        if (resursListScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(resursListScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            resursListController.setMainController(this);
            resursListController.setMainApp(mnApp);
            resursListController.viewResurses(this.resourceTypeEntityList);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/resurs/ResursList.fxml"));
                resursListLayout = (BorderPane) loader.load();
                resursListScene = new Scene(resursListLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(resursListScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                resursListController = loader.getController();
                resursListController.setMainController(this);
                resursListController.setMainApp(mnApp);
                resursListController.viewResurses(this.resourceTypeEntityList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    //Получить список Ресурсов по Единице измерения
    public List<ResourceTypeEntity> loadResursList(String measureString) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ResourceTypeEntity> query = builder.createQuery(ResourceTypeEntity.class);
            Root<ResourceTypeEntity> root = query.from(ResourceTypeEntity.class);
            query.select(root);
            query.where(builder.equal(root.get("measure"), measureString));
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


    /////////////////////////////////////////////////////////////
    //////                      УЛЬИ                       //////
    /////////////////////////////////////////////////////////////
    private HiveListController hiveListController;
    private BorderPane hiveListLayout;
    private Scene hiveListScene;
    @FXML
    public void changeStateToHiveList() {
        this.beehiveEntityList = loadHiveList();
        if (hiveListScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(hiveListScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            hiveListController.setMainController(this);
            hiveListController.setMainApp(mnApp);
            hiveListController.viewHives(this.beehiveEntityList);
            hiveListController.setTextlblTitle("Ульи");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/beehive/HiveList.fxml"));
                hiveListLayout = (BorderPane) loader.load();
                hiveListScene = new Scene(hiveListLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(hiveListScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                hiveListController = loader.getController();
                hiveListController.setMainController(this);
                hiveListController.setMainApp(mnApp);
                hiveListController.viewHives(this.beehiveEntityList);
                hiveListController.setTextlblTitle("Ульи");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<BeehiveEntity> beehiveEntityList;
    //Получить список Ульев
    public List<BeehiveEntity> loadHiveList() {
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


    /////////////////////////////////////////////////////////////
    //////                     ФИНАНСЫ                     //////
    /////////////////////////////////////////////////////////////
}