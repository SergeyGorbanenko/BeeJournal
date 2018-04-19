package resurs;

import app.Main;
import hba.ResourceTypeEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import work.WorkCUDController;

import java.io.IOException;

public class ResursHistoryController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private ResursListController resursListController;
    public void setResursListController(ResursListController resursListController) {
        this.resursListController = resursListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToResursList() {
        resursListController.getMainController().changeStateToResursList();
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
            resursCUDController.setMainApp(mnApp);
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
                resursCUDController.setMainApp(mnApp);
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
            resursIncomeExpenseController.setMainApp(mnApp);
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
                resursIncomeExpenseController.setMainApp(mnApp);
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

    @FXML       //[+/-]
    void goIncomeExpense() {
        changeStateToResursIncomeExpense();
    }


    @FXML private Label lblvalueName;
    @FXML private Label lblvalueCategory;

    //Конкретный тип ресурса
    private ResourceTypeEntity resourceTypeEntity = null;

    public void fillHeaderOfResursInHistory(ResourceTypeEntity resourceTypeEntity) {
        this.resourceTypeEntity = resourceTypeEntity;
        this.lblvalueName.setText(resourceTypeEntity.getName());
        this.lblvalueCategory.setText(resourceTypeEntity.getCategory());
    }

}
