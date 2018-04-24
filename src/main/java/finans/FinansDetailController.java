package finans;

import app.Main;
import app.ServiseUtil;
import hba.FinancialOperateEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FinansDetailController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private FinansListController finansListController;
    public void setFinansListController(FinansListController finansListController) {
        this.finansListController = finansListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToFinansList() {
        finansListController.getMainController().changeStateToFinansList();
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
            finansCUDController.setFinansListController(finansListController);
            finansCUDController.setFinansDetailController(this);
            finansCUDController.setMainApp(mnApp);
            finansCUDController.setFinancialOperateEntity(financialOperateEntity);
            finansCUDController.initFinansDataInCombobox();
            finansCUDController.initFinansEditState();
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
                finansCUDController.setFinansListController(finansListController);
                finansCUDController.setFinansDetailController(this);
                finansCUDController.setMainApp(mnApp);
                finansCUDController.setFinancialOperateEntity(financialOperateEntity);
                finansCUDController.initFinansDataInCombobox();
                finansCUDController.initFinansEditState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToFinansList();
    }

    @FXML       //[РЕД]
    public void goEdit() {
        changeStateToFinansCUD();
    }

    //Конкретная финансовая операция
    private FinancialOperateEntity financialOperateEntity = null;

    @FXML private Label lblvalueTitle;
    @FXML private Label lblvalueResursName;
    @FXML private Label lblvalueOperationType;
    @FXML private Label lblvalueDate;
    @FXML private Label lblvalueCount;
    @FXML private Label lblvalueDescription;
    @FXML private Label lblvalueUnitPrice;
    @FXML private Label lblvalueSummaryPrice;

    public void fillFinansDetailState(FinancialOperateEntity financialOperateEntity) {
        this.financialOperateEntity = financialOperateEntity;
        //
        this.lblvalueResursName.setText(financialOperateEntity.getResourceTypeByIdResourseType().getName());
        if (financialOperateEntity.getOperationType())
            this.lblvalueOperationType.setText("Продажа");
        else
            this.lblvalueOperationType.setText("Покупка");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        this.lblvalueDate.setText(financialOperateEntity.getDate().format(formatter));
        this.lblvalueCount.setText(ServiseUtil.cutZero(financialOperateEntity.getCount()));
        this.lblvalueUnitPrice.setText(ServiseUtil.cutZero(financialOperateEntity.getUnitPrice()));
        this.lblvalueSummaryPrice.setText(String.valueOf(financialOperateEntity.getCount() * financialOperateEntity.getUnitPrice()));
        this.lblvalueDescription.setText(financialOperateEntity.getDescription());
    }

}
