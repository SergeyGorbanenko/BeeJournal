package finans;

import app.Main;
import app.MainController;
import app.ServiseUtil;
import hba.BeehiveEntity;
import hba.FinancialOperateEntity;
import hba.WorkEntity;
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
import work.WorkCUDController;
import work.WorkDetailController;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class FinansListController {

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

    private FinansDetailController finansDetailController;
    private BorderPane finansDetailLayout;
    private Scene finansDetailScene;
    public void changeStateToFinansDetail(FinancialOperateEntity financialOperateEntity) {
        if (finansDetailScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(finansDetailScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            finansDetailController.setFinansListController(this);
            finansDetailController.setMainApp(mnApp);
            finansDetailController.fillFinansDetailState(financialOperateEntity);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/finans/FinansDetail.fxml"));
                finansDetailLayout = (BorderPane) loader.load();
                finansDetailScene = new Scene(finansDetailLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(finansDetailScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                finansDetailController = loader.getController();
                finansDetailController.setFinansListController(this);
                finansDetailController.setMainApp(mnApp);
                finansDetailController.fillFinansDetailState(financialOperateEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private FinansCUDController finansCUDController;
    private BorderPane finansCUDLayout;
    private Scene finansCUDScene;
    public void changeStateToFinansCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (finansCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(finansCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            finansCUDController.setFinansListController(this);
            finansCUDController.setMainApp(mnApp);
            finansCUDController.initFinansDataInCombobox();
            finansCUDController.initFinansAddState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/finans/FinansCUD.fxml"));
                finansCUDLayout = (BorderPane) loader.load();
                finansCUDScene = new Scene(finansCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(finansCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                finansCUDController = loader.getController();
                finansCUDController.setFinansListController(this);
                finansCUDController.setMainApp(mnApp);
                finansCUDController.initFinansDataInCombobox();
                finansCUDController.initFinansAddState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML       //[СОЗДАТЬ НОВУЮ ФИНАНСОВУЮ ОПЕРАЦИЮ]
    public void goAddFinans() {
        changeStateToFinansCUD();
    }


    @FXML private Label lblTitle;
    @FXML private Label lblFinansNotFound;
    @FXML private ScrollPane scrlPane;

    public void setTextlblTitle(String lblTitleText) {
        this.lblTitle.setText(lblTitleText);
    }


    //Конкретная финансовая операция
    private FinancialOperateEntity financialOperateEntity;
    public FinancialOperateEntity getFinancialOperateEntity() {
        return financialOperateEntity;
    }

    //Вывести список всех Финансовых операций
    public void viewFinanses(List<FinancialOperateEntity> financialOperateEntityList) {
        if (financialOperateEntityList.isEmpty()) {
            this.lblFinansNotFound.setVisible(true);
            scrlPane.setContent(new AnchorPane());
            return;
        }
        this.lblFinansNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (FinancialOperateEntity foE : financialOperateEntityList) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #c7dbff;");
            gridPane.setPadding(new Insets(10, 5, 10, 5));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);     //295 для отсутствия полосы прокрутки снизу
            //
            Label lblvalueIconFinansIn = new Label();
            Label lblvalueIconFinansOut = new Label();
            Label lblvalueResursType = new Label(foE.getResourceTypeByIdResourseType().getName());
            Label lblvalueOperateType = new Label();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru", "RU"));
            Label lblvalueDate = new Label(foE.getDate().format(formatter));
            Label lblvalueCount = new Label(ServiseUtil.cutZero(foE.getCount()) + " " + foE.getResourceTypeByIdResourseType().getMeasure());
            Label lblvalueSummaryPrice = new Label(ServiseUtil.cutZero(foE.getCount() * foE.getUnitPrice()) + " Р");
            //
            if (foE.getOperationType()) {       //Если Продажа
                lblvalueOperateType.setText("Продажа: ");
                lblvalueIconFinansOut.setFont(new Font("Arial Bold", 25));
                lblvalueIconFinansOut.setStyle("-fx-background-image: url('/icons/sale.png')");
                lblvalueIconFinansOut.setPrefHeight(32);
                lblvalueIconFinansOut.setPrefWidth(32);
                //
                lblvalueSummaryPrice.setTextFill(Color.GREEN);
                gridPane.add(lblvalueIconFinansOut, 0, 0, 1, 2);
            } else {        //Если покупка
                lblvalueOperateType.setText("Покупка: ");
                lblvalueIconFinansIn.setFont(new Font("Arial Bold", 25));
                lblvalueIconFinansIn.setStyle("-fx-background-image: url('/icons/prch.png')");
                lblvalueIconFinansIn.setPrefHeight(32);
                lblvalueIconFinansIn.setPrefWidth(32);
                //
                lblvalueSummaryPrice.setTextFill(Color.RED);
                gridPane.add(lblvalueIconFinansIn, 0, 0, 1, 2);
            }
            lblvalueResursType.setFont(new Font("Arial Bold", 15));
            lblvalueOperateType.setFont(new Font("Arial", 12));
            lblvalueDate.setFont(new Font("Arial", 12));
            lblvalueCount.setFont(new Font("Arial Bold", 16));
            lblvalueCount.setTextFill(Color.web("#0061ff"));
            lblvalueSummaryPrice.setFont(new Font("Arial Bold", 14));
            //
            lblvalueDate.setLayoutX(50);
            //
            HBox hBox = new HBox(lblvalueOperateType, lblvalueDate);
            gridPane.add(lblvalueResursType, 1, 0, 1, 1);
            gridPane.add(hBox, 1, 1, 1, 1);
            gridPane.add(lblvalueCount, 2, 0, 1, 1);
            gridPane.add(lblvalueSummaryPrice, 2, 1, 1, 1);
            //
            col1.setPercentWidth(15);
            col2.setPercentWidth(67);
            col3.setPercentWidth(28);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.financialOperateEntity = foE;
                changeStateToFinansDetail(financialOperateEntity);
            });
            //
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrlPane.setContent(aPane);
        }
    }

    @FXML
    public void viewFinansesByOperationTypeTrue() {
        viewFinanses(mainController.loadFinansList(true));
    }

    @FXML
    public void viewFinansesByOperationTypeFalse() {
        viewFinanses(mainController.loadFinansList(false));
    }

}
