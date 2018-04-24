package resurs;

import app.HBUtil;
import app.Main;
import beehive.HiveListController;
import hba.IncomeExpenseEntity;
import hba.ResourceTypeEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ResursHistoryController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private ResursListController resursListController;
    public void setResursListController(ResursListController resursListController) {
        this.resursListController = resursListController;
    }

    private HiveListController hiveListController;
    public void setHiveListController(HiveListController hiveListController) {
        this.hiveListController = hiveListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToResursList() {
        if (resursListController.getMainController() != null)
        resursListController.getMainController().changeStateToResursList();
        else    //возврат в подробно об улье
            hiveListController.changeStateToHiveDetail(hiveListController.getBeehiveEntity());
    }

    private ResursCUDController resursCUDController;
    private BorderPane resursCUDLayout;
    private Scene resursCUDScene;
    public void changeStateToResursCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (resursCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(resursCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            resursCUDController.setResursHistoryController(this);
            resursCUDController.setResursListController(resursListController);
            resursCUDController.setMainApp(mnApp);
            resursCUDController.initComboboxMeasure();
            resursCUDController.setResourceTypeEntity(resourceTypeEntity);
            resursCUDController.initResursEditState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/resurs/ResursCUD.fxml"));
                resursCUDLayout = (BorderPane) loader.load();
                resursCUDScene = new Scene(resursCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(resursCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                resursCUDController = loader.getController();
                resursCUDController.setResursHistoryController(this);
                resursCUDController.setResursListController(resursListController);
                resursCUDController.setMainApp(mnApp);
                resursCUDController.initComboboxMeasure();
                resursCUDController.setResourceTypeEntity(resourceTypeEntity);
                resursCUDController.initResursEditState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ResursIncomeExpenseController resursIncomeExpenseController;
    private BorderPane resursIELayout;
    private Scene resursIEScene;
    public void changeStateToResursIncomeExpense(){
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (resursIEScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(resursIEScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            resursIncomeExpenseController.setResursHistoryController(this);
            resursIncomeExpenseController.setResursListController(resursListController);
            resursIncomeExpenseController.setMainApp(mnApp);
            //
            resursIncomeExpenseController.setIncomeExpenseEntity(this.incomeExpenseEntity);
            resursIncomeExpenseController.setResourceTypeEntity(this.resourceTypeEntity);
            resursIncomeExpenseController.initComboboxOperationType();
            resursIncomeExpenseController.initComboboxBeehive();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/resurs/ResursIncomeExpense.fxml"));
                resursIELayout = (BorderPane) loader.load();
                resursIEScene = new Scene(resursIELayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(resursIEScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                resursIncomeExpenseController = loader.getController();
                resursIncomeExpenseController.setResursHistoryController(this);
                resursIncomeExpenseController.setResursListController(resursListController);
                resursIncomeExpenseController.setMainApp(mnApp);
                //
                resursIncomeExpenseController.setIncomeExpenseEntity(this.incomeExpenseEntity);
                resursIncomeExpenseController.setResourceTypeEntity(this.resourceTypeEntity);
                resursIncomeExpenseController.initComboboxOperationType();
                resursIncomeExpenseController.initComboboxBeehive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToResursList();
    }

    @FXML       //[РЕД]
    public void goEdit() {
        changeStateToResursCUD();
    }

    @FXML       //[ДОБАВИТЬ ПРИХОД/РАСХОД]
    void goIncomeExpense() {
        changeStateToResursIncomeExpense();
        resursIncomeExpenseController.initIncomeExpenseAddState();
    }


    @FXML private Label lblvalueName;
    @FXML private Label lblvalueCategory;
    @FXML private Label lblResursHistoryNotFound;
    @FXML private ScrollPane scrlPane;


    public void fillResursHistory(ResourceTypeEntity resourceTypeEntity) {
        this.resourceTypeEntity = resourceTypeEntity;
        this.lblvalueName.setText(resourceTypeEntity.getName());
        this.lblvalueCategory.setText(resourceTypeEntity.getCategory());
        viewResursHistory(loadIncomeExpenseList());
    }

    //Конкретный тип ресурса
    private ResourceTypeEntity resourceTypeEntity = null;
    //Список истории конкретного ресурса
    private List<IncomeExpenseEntity> incomeExpenseEntityList;
    //Контретный приход/расход
    private IncomeExpenseEntity incomeExpenseEntity = null;



    //Вывести список истории ресурса
    public void viewResursHistory(List<IncomeExpenseEntity> incomeExpenseEntityList) {
        if (incomeExpenseEntityList.isEmpty()) {
            this.lblResursHistoryNotFound.setVisible(true);
            scrlPane.setContent(new AnchorPane());
            return;
        }
        this.lblResursHistoryNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (IncomeExpenseEntity ieE : incomeExpenseEntityList) {
            GridPane gridPane = new GridPane();
            //gridPane.setStyle("-fx-background-color:  #ceffd7;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY); gridPaneLayoutY+=65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);     //295 для отсутствия полосы прокрутки снизу
            //
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM", new Locale("ru", "RU"));
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE");
            Label lblvalueDate = new Label(ieE.getDate().format(formatter1));
            Label lblvalueDayDate = new Label("   " + ieE.getDate().format(formatter2));
            Label lblvalueDescription = new Label(ieE.getDescription());
            Label lblvalueCount = new Label();
            Label lblvalueBeehive = new Label();
            //
            if (ieE.getOperationType()) {
                lblvalueCount.setText("+" + ieE.getCount() + " " + ieE.getResourceTypeByIdResourseType().getMeasure());
                lblvalueCount.setTextFill(Color.GREEN);
            } else {
                lblvalueCount.setText("-" + ieE.getCount() + " " + ieE.getResourceTypeByIdResourseType().getMeasure());
                lblvalueCount.setTextFill(Color.RED);
            }
            //
            lblvalueDate.setFont(new Font("Arial Bold", 13));
            lblvalueDayDate.setFont(new Font("Arial Bold", 12));
            lblvalueDescription.setFont(new Font("Arial Bold", 14));
            lblvalueDescription.setTextFill(Color.web( "#703e00"));
            lblvalueDescription.setWrapText(true);
            lblvalueCount.setFont(new Font("Arial Bold", 14));
            //
            VBox vBox = new VBox();
            vBox.getChildren().add(lblvalueDescription);
            if (ieE.getIdBeehive() != 1) {
                lblvalueBeehive.setText("Улей: " + ieE.getBeehive().getHiveNumber());
                lblvalueBeehive.setFont(new Font("Arial", 12));
                vBox.getChildren().add(lblvalueBeehive);
            }
            //
            gridPane.add(lblvalueDate, 0, 0, 1, 1);
            gridPane.add(lblvalueDayDate, 0, 1, 1, 1);
            gridPane.add(vBox, 1, 0, 1, 2);
            gridPane.add(lblvalueCount, 2, 0, 1, 2);
            //
            col1.setPercentWidth(18);
            col2.setPercentWidth(57);
            col3.setPercentWidth(25);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.incomeExpenseEntity = ieE;
                changeStateToResursIncomeExpense();
                resursIncomeExpenseController.initIncomeExpenseEditState();
            });
            //
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight); aPanePrefHeight+=65;
            scrlPane.setContent(aPane);
        }
    }

    //Получить список истории конкретного ресурса
    public List<IncomeExpenseEntity> loadIncomeExpenseList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<IncomeExpenseEntity> query = builder.createQuery(IncomeExpenseEntity.class);
            Root<IncomeExpenseEntity> root = query.from(IncomeExpenseEntity.class);
            query.select(root).where(builder.equal(root.get("idResourseType"), resourceTypeEntity.getIdResourseType()));
            query.orderBy(builder.desc(root.get("date")));
            Query<IncomeExpenseEntity> q = session.createQuery(query);
            this.incomeExpenseEntityList = q.getResultList();
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
        return this.incomeExpenseEntityList;
    }

    //Получить список истории конкретного ресурса
    public List<IncomeExpenseEntity> loadIncomeExpenseList(Boolean operationType) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<IncomeExpenseEntity> query = builder.createQuery(IncomeExpenseEntity.class);
            Root<IncomeExpenseEntity> root = query.from(IncomeExpenseEntity.class);
            query.select(root).where(builder.equal(root.get("idResourseType"), resourceTypeEntity.getIdResourseType()),
                                    builder.equal(root.get("operationType"), operationType));
            query.orderBy(builder.desc(root.get("date")));
            Query<IncomeExpenseEntity> q = session.createQuery(query);
            this.incomeExpenseEntityList = q.getResultList();
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
        return this.incomeExpenseEntityList;
    }

    @FXML
    public void viewResursHistoryByOperationTypeTrue() {
        viewResursHistory( loadIncomeExpenseList(true));
    }

    @FXML
    public void viewResursHistoryByOperationTypeFalse() {
        viewResursHistory( loadIncomeExpenseList(false));
    }


}
