import hba.WorkEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class WorkListController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public MainController getMainController() {
        return mainController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    @FXML
    public void changeStateToHome() {
        mnApp.initRootLayout();
    }

    private WorkDetailController workDetailController;
    private BorderPane workDetailLayout;
    private Scene workDetailScene;
    public void changeStateToWorkDetail(WorkEntity workEntity) {
        if (workDetailScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workDetailScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workDetailController.setWorkListController(this);
            workDetailController.setMainApp(mnApp);
            //workDetailController.initWorkDetailState();
            workDetailController.fillWorkDetailState(workEntity);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkDetail.fxml"));
                workDetailLayout = (BorderPane) loader.load();
                workDetailScene = new Scene(workDetailLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workDetailScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workDetailController = loader.getController();
                workDetailController.setWorkListController(this);
                workDetailController.setMainApp(mnApp);
                //workDetailController.initWorkDetailState();
                workDetailController.fillWorkDetailState(workEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private WorkCUDController workCUDController;
    private BorderPane workCUDLayout;
    private Scene workCUDScene;
    public void changeStateToWorkCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (workCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(workCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            workCUDController.setWorkListController(this);
            workCUDController.setMainApp(mnApp);
            workCUDController.initWorkDataInCombobox();
            workCUDController.initWorkAddState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("WorkCUD.fxml"));
                workCUDLayout = (BorderPane) loader.load();
                workCUDScene = new Scene(workCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(workCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                workCUDController = loader.getController();
                workCUDController.setWorkListController(this);
                workCUDController.setMainApp(mnApp);
                workCUDController.initWorkDataInCombobox();
                workCUDController.initWorkAddState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML       //[ЭЛЕМЕНТ РАБОТЫ] - подробнее о работе
    public void goWorkDetail() {
        changeStateToWorkDetail(this.workEntity);
    }

    @FXML       //[СОЗДАТЬ НОВУЮ РАБОТУ]
    public void goCreateNewWork() {
        changeStateToWorkCUD();
    }

    @FXML private ScrollPane scrlPane;


    //Конкретная работа
    private WorkEntity workEntity;

    public void viewWorks(List<WorkEntity> workEntityList) {
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (WorkEntity wrkE : workEntityList) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #ffefd3;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY); gridPaneLayoutY+=65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM", new Locale("ru", "RU"));
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE");
            Label lblvalueDateStart = new Label(wrkE.getDateStart().format(formatter1));
            Label lblvalueDayDateStart = new Label("   " + wrkE.getDateStart().format(formatter2));
            Label lblvalueName = new Label(wrkE.getWorkKindByIdWorkKind().getName());
            Label lblcaptionWorkStatus = new Label("Статус");
            Label lblvalueWorkStatus = new Label(wrkE.getWorkStatus());
            Label lblvalueBeehive = new Label("улей: нет");
            if (wrkE.getBeehive() != null)
                lblvalueBeehive.setText("улей: " + wrkE.getBeehive().getHiveNumber());
            //
            lblvalueDateStart.setFont(new Font("Arial Bold", 14));
            lblvalueDayDateStart.setFont(new Font("Arial Bold", 13));
            lblvalueName.setFont(new Font("Arial Bold", 15));
            lblvalueName.setTextFill(Color.web( "#7f5c2f"));
            lblvalueWorkStatus.setFont(new Font("Arial Bold", 13));
            lblcaptionWorkStatus.setFont(new Font("Arial", 12));
            lblvalueBeehive.setFont(new Font("Arial", 12));
            //
            if (lblvalueWorkStatus.getText().equals("Планируется")) lblvalueWorkStatus.setTextFill(Color.web("#006ac6"));
            if (lblvalueWorkStatus.getText().equals("В процессе")) lblvalueWorkStatus.setTextFill(Color.web("#730093"));
            if (lblvalueWorkStatus.getText().equals("Выполнена")) lblvalueWorkStatus.setTextFill(Color.web("#0cc100"));
            if (lblvalueWorkStatus.getText().equals("Просрочена")) lblvalueWorkStatus.setTextFill(Color.web("#ff0000"));
            //
            gridPane.add(lblvalueDateStart, 0, 0, 1, 1);
            gridPane.add(lblvalueDayDateStart, 0, 1, 1, 1);
            gridPane.add(lblvalueName, 1, 0, 1, 1);
            gridPane.add(lblvalueBeehive, 1, 1, 1, 1);
            gridPane.add(lblvalueWorkStatus, 2, 0, 1, 1);
            gridPane.add(lblcaptionWorkStatus, 2, 1, 1, 1);
            //
            col1.setPercentWidth(25);
            col2.setPercentWidth(43);
            col3.setPercentWidth(32);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.workEntity = wrkE;
                changeStateToWorkDetail(this.workEntity);
            });
            //
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight); aPanePrefHeight+=65;
            scrlPane.setContent(aPane);
        }
    }

    @FXML
    public void viewWorksByStatusPlanning() {
        viewWorks( mainController.loadWorkList("Планируется"));
    }

    @FXML
    public void viewWorksByStatusInProcess() {
        viewWorks( mainController.loadWorkList("В процессе"));
    }

    @FXML
    public void viewWorksByStatusDone() {
        viewWorks(mainController.loadWorkList("Выполнена"));
    }

    @FXML
    public void viewWorksByStatusProsrochka() {
        viewWorks(mainController.loadWorkList("Просрочена"));
    }

}
