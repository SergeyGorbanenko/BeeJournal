package finans;

import app.Main;
import app.ServiseUtil;
import hba.FinancialOperateEntity;
import hba.WorkEntity;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToFinansList();
    }

    @FXML       //[РЕД]
    public void goEdit() {

    }

    //Конкретная финансовая операция
    private FinancialOperateEntity financialOperateEntity = null;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.lblvalueDate.setText(financialOperateEntity.getDate().format(formatter));
        this.lblvalueCount.setText(ServiseUtil.cutZero(financialOperateEntity.getCount()));
        this.lblvalueUnitPrice.setText(ServiseUtil.cutZero(financialOperateEntity.getUnitPrice()));
        this.lblvalueSummaryPrice.setText(String.valueOf(financialOperateEntity.getCount() * financialOperateEntity.getUnitPrice()));
        this.lblvalueDescription.setText(financialOperateEntity.getDescription());
    }

}
