package beehive;

import app.Main;
import hba.BeegardenEntity;
import hba.BeehiveEntity;
import hba.WorkKindEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import work.WorkCUDController;

public class HiveCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private HiveListController hiveListController;
    public void setHiveListController(HiveListController hiveListController) {
        this.hiveListController = hiveListController;
    }

    private HiveDetailController hiveDetailController;
    public void setHiveDetailController(HiveDetailController hiveDetailController) {
        this.hiveDetailController = hiveDetailController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    @FXML
    private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private ComboBox<BeegardenEntity> cmbBeegarden;
    @FXML private TextField txtfldHiveNumber;
    @FXML private TextField txtfldHiveType;
    @FXML private TextField txtfldHiveDescription;

    ObservableList<BeegardenEntity> obserlistBeegarden;
    public StringConverter<BeegardenEntity> converterBeegardenName = new StringConverter<BeegardenEntity>() {
        @Override
        public String toString(BeegardenEntity beegardenEntity) {
            return beegardenEntity.getName();
        }
        @Override
        public BeegardenEntity fromString(String string) {
            return null;
        }
    };

    //Конкретный Улей
    private BeehiveEntity beehiveEntity = null;
    public BeehiveEntity getBeehiveEntity() {
        return beehiveEntity;
    }
    public void setBeehiveEntity(BeehiveEntity beehiveEntity) {
        this.beehiveEntity = beehiveEntity;
    }


    public void initComboboxBeegarden() {
        this.cmbBeegarden.setConverter(converterBeegardenName);
        obserlistBeegarden = FXCollections.observableArrayList(new WorkCUDController().loadBeegarden());
        this.cmbBeegarden.getItems().clear();
        this.cmbBeegarden.setItems(obserlistBeegarden);
        this.cmbBeegarden.getSelectionModel().select(0);
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        this.txtfldHiveNumber.setText(null);
        this.txtfldHiveType.setText(null);
        this.txtfldHiveDescription.setText(null);
        //
        Stage mainStage = mnApp.getPrimaryStage();
        if (hiveDetailController != null) {
            if (hiveDetailController.getOwnerScene() != null)
                mainStage.setScene(hiveDetailController.getOwnerScene());
        } else if (hiveListController != null) {
            if (hiveListController.getOwnerScene() != null)
                mainStage.setScene(hiveListController.getOwnerScene());
        }
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {


    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {

    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {

    }

    public void initHiveAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить улей");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
        //
        this.txtfldHiveNumber.setText(null);
        this.txtfldHiveType.setText(null);
        this.txtfldHiveDescription.setText(null);
    }

    public void initHiveEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить улей");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        this.txtfldHiveNumber.setText(beehiveEntity.getHiveNumber());
        this.txtfldHiveType.setText(beehiveEntity.getHiveType());
        this.txtfldHiveDescription.setText(beehiveEntity.getDescription());
    }


}
