package resurs;

import app.Main;
import app.MainController;
import hba.IncomeExpenseEntity;
import hba.ResourceTypeEntity;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.List;

public class ResursListController {

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

    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML private ScrollPane scrlPane;

    //Конкретный тип ресурса
    private ResourceTypeEntity resourceTypeEntity;

    public void viewResurses(List<ResourceTypeEntity> resourceTypeEntityList) {
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (ResourceTypeEntity rstE : resourceTypeEntityList) {
            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color:  #ede0ff;");
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 65;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            Label lblvalueIconResurs = new Label(" R");
            Label lblvalueName = new Label(rstE.getName());
            Label lblvalueCategory = new Label(rstE.getCategory());
            Label lblvalueSummaryCount = new Label(calculateSummaryCount(rstE) + " " + rstE.getMeasure());
            //
            lblvalueIconResurs.setFont(new Font("Arial Bold", 25));
            lblvalueName.setFont(new Font("Arial Bold", 15));
            lblvalueCategory.setFont(new Font("Arial Bold", 13));
            lblvalueCategory.setTextFill(Color.web("#7f5c2f"));
            lblvalueSummaryCount.setFont(new Font("Arial Bold", 19));
            lblvalueSummaryCount.setTextFill(Color.web("#ffae00"));
            //
            gridPane.add(lblvalueIconResurs, 0, 0, 1, 2);
            gridPane.add(lblvalueName, 1, 0, 1, 1);
            gridPane.add(lblvalueCategory, 1, 1, 1, 1);
            gridPane.add(lblvalueSummaryCount, 2, 0, 1, 2);
            //
            col1.setPercentWidth(20);
            col2.setPercentWidth(55);
            col3.setPercentWidth(25);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.resourceTypeEntity = rstE;
                //changeStateToResursHistory(this.resourceTypeEntity); //TODO
            });
            //
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 65;
            scrlPane.setContent(aPane);
        }
    }

    //Подсчитываем оставшееся количество ресурса по его записям о приходе/расходе в зависимости от типа операции
    public String calculateSummaryCount(ResourceTypeEntity resourceTypeEntity) {
        int resSummaryCount = 0;
        for (IncomeExpenseEntity incomeExpenseEntity : resourceTypeEntity.getIncomeExpensesByIdResourseType()) {
            if (incomeExpenseEntity.getOperationType())
                resSummaryCount += incomeExpenseEntity.getCount();
            else
                resSummaryCount -= incomeExpenseEntity.getCount();
        }
        return String.valueOf(resSummaryCount);
    }

}
