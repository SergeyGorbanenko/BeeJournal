import hba.WorkEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;

public class MainController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private WorkListController workListController;
    private BorderPane workListLayout;
    private Scene workListScene;
    @FXML                               //[РАБОТЫ]
    public void changeStateToWorkList() {
        this.workEntityList = loadWorkList();
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
                loader.setLocation(Main.class.getResource("WorkList.fxml"));
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


    private List<WorkEntity> workEntityList;
    //Получить список Работ
    public List<WorkEntity> loadWorkList() {
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

    public List<WorkEntity> loadWorkList(String statusString) {
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

}