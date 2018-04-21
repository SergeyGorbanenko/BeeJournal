package beehive;

import app.Main;
import app.MainController;
import hba.BeehiveEntity;
import hba.CountFrameEntity;
import hba.IncomeExpenseEntity;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import resurs.ResursHistoryController;
import resurs.ResursListController;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class HiveDetailController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private HiveListController hiveListController;
    public void setHiveListController(HiveListController hiveListController) {
        this.hiveListController = hiveListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToHiveList() {
        hiveListController.getMainController().changeStateToHiveList();
    }

    @FXML private Label lblTitleHiveNumber;
    @FXML private Label lblvalueType;
    @FXML private Label lblvalueBeegarden;
    @FXML private Label lblvalueDescription;
    @FXML private ScrollPane scrlPaneCountFrame;
    @FXML private ScrollPane scrlPaneResurs;
    @FXML private Label lblCountFramesNotFound;
    @FXML private Label lblResursNotFound;

    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToHiveList();
    }

    @FXML       //[РЕД]
    public void goEdit() {

    }

    private BeehiveEntity beehiveEntity = null;
    public void initDataInHiveDetail(BeehiveEntity beehiveEntity) {
        this.beehiveEntity = beehiveEntity;
        this.lblTitleHiveNumber.setText("Улей № " + beehiveEntity.getHiveNumber());
        this.lblvalueType.setText(beehiveEntity.getHiveType());
        this.lblvalueBeegarden.setText(beehiveEntity.getBeegardenByIdBeegarden().getName());
        this.lblvalueDescription.setText(beehiveEntity.getDescription());
        //
        viewCountFrames(beehiveEntity.getCountFrames());
        viewResursHistory(beehiveEntity.getIncomeExpenses());
    }

    //Конкретная запись о количестве рамок
    private CountFrameEntity countFrameEntity = null;

    //Вывести спсок Истории рамок по конкретному улью
    public void viewCountFrames(Collection<CountFrameEntity> countFrameEntityList) {
        if (countFrameEntityList.isEmpty()) {
            this.lblCountFramesNotFound.setVisible(true);
            scrlPaneCountFrame.setContent(new AnchorPane());
            return;
        }
        this.lblCountFramesNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPaneCountFrame.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (CountFrameEntity cfE : countFrameEntityList) {
            GridPane gridPane = new GridPane();
            //gridPane.setStyle("-fx-background-color:  #ffd6e4;");
            gridPane.setPadding(new Insets(5, 10, 5, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 30;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru", "RU"));
            Label lblvalueDate = new Label(cfE.getCheckDate().format(formatter));
            Label lblvalueCount = new Label(cfE.getCountFrame() + " шт");
            //
            lblvalueDate.setFont(new Font("Arial Bold", 13));
            lblvalueCount.setFont(new Font("Arial Bold", 13));
            lblvalueCount.setTextFill(Color.web("#ffae00"));
            //
            gridPane.add(lblvalueDate, 0, 0, 1, 1);
            gridPane.add(lblvalueCount, 1, 0, 1, 1);
            //
            col1.setPercentWidth(50);
            col2.setPercentWidth(50);
            gridPane.getColumnConstraints().addAll(col1, col2);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.countFrameEntity = cfE;
                //changeStateToHiveDetail(this.beehiveEntity);
            });
            //
            aPane = (AnchorPane) scrlPaneCountFrame.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 30;
            scrlPaneCountFrame.setContent(aPane);
        }
    }

    //Вывести список Истории ресурса по кокретному улью
    public void viewResursHistory(Collection<IncomeExpenseEntity> incomeExpenseEntityList) {
        if (incomeExpenseEntityList.isEmpty()) {
            this.lblResursNotFound.setVisible(true);
            scrlPaneResurs.setContent(new AnchorPane());
            return;
        }
        this.lblResursNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPaneResurs.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (IncomeExpenseEntity ieE : incomeExpenseEntityList) {
            GridPane gridPane = new GridPane();
            //gridPane.setStyle("-fx-background-color:  #ceffd7;");
            gridPane.setPadding(new Insets(5, 10, 5, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY); gridPaneLayoutY+=50;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM", new Locale("ru", "RU"));
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE");
            Label lblvalueDate = new Label(ieE.getDate().format(formatter1));
            Label lblvalueDayDate = new Label("   " + ieE.getDate().format(formatter2));
            Label lblvalueDescription = new Label(ieE.getDescription());
            Label lblvalueResursName = new Label(ieE.getResourceTypeByIdResourseType().getName());
            Label lblvalueCount = new Label();
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
            lblvalueResursName.setFont(new Font("Arial", 12));
            //
            gridPane.add(lblvalueDate, 0, 0, 1, 1);
            gridPane.add(lblvalueDayDate, 0, 1, 1, 1);
            gridPane.add(lblvalueResursName, 1, 0, 1, 1);
            gridPane.add(lblvalueDescription, 1, 1, 1, 1);
            gridPane.add(lblvalueCount, 2, 0, 1, 2);
            //
            col1.setPercentWidth(18);
            col2.setPercentWidth(64);
            col3.setPercentWidth(18);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                ResursListController resursListController = new ResursListController();
                resursListController.setMainApp(mnApp);
                resursListController.setHiveListController(hiveListController);
                resursListController.changeStateToResursHistory(ieE.getResourceTypeByIdResourseType());
            });
            //
            aPane = (AnchorPane) scrlPaneResurs.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight); aPanePrefHeight+=50;
            scrlPaneResurs.setContent(aPane);
        }
    }


    @FXML//      [ПОКАЗАТЬ РАБОТЫ ПО УЛЬЮ]
    public void showWorksByBeehive() {
        MainController mainController = new MainController();
        mainController.setMainApp(mnApp);
        mainController.setWorkEntityList(beehiveEntity.getWorks());
        mainController.changeStateToWorkList();
    }
}
