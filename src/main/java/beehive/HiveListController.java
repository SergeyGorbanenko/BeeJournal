package beehive;

import app.Main;
import app.MainController;
import hba.BeehiveEntity;
import hba.CountFrameEntity;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HiveListController {

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

    private HiveDetailController hiveDetailController;
    private BorderPane hiveDetailLayout;
    private Scene hiveDetailScene;
    public void changeStateToHiveDetail(BeehiveEntity beehiveEntity) {
        if (hiveDetailScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(hiveDetailScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            hiveDetailController.setHiveListController(this);
            hiveDetailController.setMainApp(mnApp);
            hiveDetailController.initDataInHiveDetail(beehiveEntity);
            hiveDetailController.viewCountFrames(hiveDetailController.initBeehive().getCountFrames());
            hiveDetailController.viewResursHistory(hiveDetailController.initBeehive().getIncomeExpenses());
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/beehive/HiveDetail.fxml"));
                hiveDetailLayout = (BorderPane) loader.load();
                hiveDetailScene = new Scene(hiveDetailLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(hiveDetailScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                hiveDetailController = loader.getController();
                hiveDetailController.setHiveListController(this);
                hiveDetailController.setMainApp(mnApp);
                hiveDetailController.initDataInHiveDetail(beehiveEntity);
                hiveDetailController.viewCountFrames(hiveDetailController.initBeehive().getCountFrames());
                hiveDetailController.viewResursHistory(hiveDetailController.initBeehive().getIncomeExpenses());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HiveCUDController hiveCUDController;
    private BorderPane hiveCUDLayout;
    private Scene hiveCUDScene;
    public void changeStateToHiveCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (hiveCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(hiveCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            hiveCUDController.setHiveListController(this);
            hiveCUDController.setMainApp(mnApp);
            hiveCUDController.initComboboxBeegarden();
            hiveCUDController.initHiveAddState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/beehive/HiveCUD.fxml"));
                hiveCUDLayout = (BorderPane) loader.load();
                hiveCUDScene = new Scene(hiveCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(hiveCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                hiveCUDController = loader.getController();
                hiveCUDController.setHiveListController(this);
                hiveCUDController.setMainApp(mnApp);
                hiveCUDController.initComboboxBeegarden();
                hiveCUDController.initHiveAddState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML       //[СОЗДАТЬ НОВЫЙ УЛЕЙ]
    public void goAddHive() {
        changeStateToHiveCUD();
    }


    @FXML private Label lblTitle;
    @FXML private Label lblHiveNotFound;
    @FXML private ScrollPane scrlPane;

    public void setTextlblTitle(String lblTitleText) {
        this.lblTitle.setText(lblTitleText);
    }

    //Конкретный Улей
    private BeehiveEntity beehiveEntity;
    public BeehiveEntity getBeehiveEntity() {
        return beehiveEntity;
    }

    //Вывести спсок всех Ульев
    public void viewHives(List<BeehiveEntity> beehiveEntityList) {
        if (beehiveEntityList.isEmpty()) {
            this.lblHiveNotFound.setVisible(true);
            scrlPane.setContent(new AnchorPane());
            return;
        }
        this.lblHiveNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (BeehiveEntity bhE : beehiveEntityList) {
            if (bhE.getIdBeehive().equals(1)) continue;
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #ffd6e4;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);     //295 для отсутствия полосы прокрутки снизу
            //
            Label lblvalueIconHive = new Label();
            Label lblvalueType = new Label(bhE.getHiveType());
            Label lblcaptionType = new Label("Тип");
            Label lblvalueCountByLastDate = new Label(getCountFrameByLastDate(bhE.getCountFrames()));
            Label lblcaptionCountByLastDate = new Label("Рамки");
            Label lblvalueNumber = new Label(bhE.getHiveNumber());
            Label lblcaptionNumber = new Label("Номер");
            //
            lblvalueIconHive.setFont(new Font("Arial Bold", 25));
            lblvalueIconHive.setStyle("-fx-background-image: url('/icons/beehive.png')");
            lblvalueIconHive.setPrefHeight(32);
            lblvalueIconHive.setPrefWidth(32);
            lblvalueType.setFont(new Font("Arial Bold", 15));
            lblcaptionType.setFont(new Font("Arial", 12));
            lblvalueCountByLastDate.setFont(new Font("Arial Bold", 16));
            lblcaptionCountByLastDate.setFont(new Font("Arial", 12));
            lblvalueNumber.setFont(new Font("Arial Bold", 16));
            lblvalueNumber.setTextFill(Color.web("#ffae00"));
            lblcaptionNumber.setFont(new Font("Arial", 12));
            //
            gridPane.add(lblvalueIconHive, 0, 0, 1, 2);
            gridPane.add(lblvalueType, 1, 0, 1, 1);
            gridPane.add(lblcaptionType, 1, 1, 1, 1);
            gridPane.add(lblvalueCountByLastDate, 2, 0, 1, 1);
            gridPane.add(lblcaptionCountByLastDate, 2, 1, 1, 1);
            gridPane.add(lblvalueNumber, 3, 0, 1, 1);
            gridPane.add(lblcaptionNumber, 3, 1, 1, 1);
            //
            col1.setPercentWidth(15);
            col2.setPercentWidth(48);
            col3.setPercentWidth(20);
            col4.setPercentWidth(17);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.beehiveEntity = bhE;
                changeStateToHiveDetail(this.beehiveEntity);
            });
            //
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrlPane.setContent(aPane);
        }
    }

    //Получить количество рамок за последюю дату
    public String getCountFrameByLastDate(Collection<CountFrameEntity> countFrameEntityCollection) {
        LocalDate lastDate = LocalDate.MIN;
        String countFramesByLastDate = "---";
        for (CountFrameEntity cfE : countFrameEntityCollection) {
            if (lastDate.isBefore(cfE.getCheckDate())) {
                lastDate = cfE.getCheckDate();
                countFramesByLastDate = cfE.getCountFrame();
            }
        }
        return countFramesByLastDate;
    }

    private Integer smallHive = 5;
    private Integer middleHive = 10;
    private Integer bigHive = 15;

    @FXML   //Вывести МАЛЫЕ ульи
    public void viewHivesByCountFramesSmall() {
        List<BeehiveEntity> beehiveEntityList = mainController.loadHiveList();
        List<BeehiveEntity> resList = new ArrayList<>();
        for (BeehiveEntity bhE : beehiveEntityList) {
            if (bhE.getIdBeehive().equals(1)) continue;
            if (Integer.valueOf(getCountFrameByLastDate(bhE.getCountFrames())) <= smallHive)
                resList.add(bhE);
        }
        this.lblTitle.setText("Малые ульи");
        viewHives(resList);
    }

    @FXML   //Вывести СРЕДНИЕ ульи
    public void viewHivesByCountFramesMiddle() {
        List<BeehiveEntity> beehiveEntityList = mainController.loadHiveList();
        List<BeehiveEntity> resList = new ArrayList<>();
        for (BeehiveEntity bhE : beehiveEntityList) {
            if (bhE.getIdBeehive().equals(1)) continue;
            if ((Integer.valueOf(getCountFrameByLastDate(bhE.getCountFrames())) > smallHive) && (Integer.valueOf(getCountFrameByLastDate(bhE.getCountFrames())) <= middleHive))
                resList.add(bhE);
        }
        this.lblTitle.setText("Средние ульи");
        viewHives(resList);
    }

    @FXML   //Вывести БОЛЬШИЕ ульи
    public void viewHivesByCountFramesBig() {
        List<BeehiveEntity> beehiveEntityList = mainController.loadHiveList();
        List<BeehiveEntity> resList = new ArrayList<>();
        for (BeehiveEntity bhE : beehiveEntityList) {
            if (bhE.getIdBeehive().equals(1)) continue;
            if (Integer.valueOf(getCountFrameByLastDate(bhE.getCountFrames())) >= bigHive)
                resList.add(bhE);
        }
        this.lblTitle.setText("Большие ульи");
        viewHives(resList);
    }

}