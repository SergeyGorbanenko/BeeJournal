package resurs;

import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class ResursCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private ResursListController resursListController;
    public void setResursListController(ResursListController resursListController) {
        this.resursListController = resursListController;
    }

    private ResursHistoryController resursHistoryController;
    public void setResursHistoryController(ResursHistoryController resursHistoryController) {
        this.resursHistoryController = resursHistoryController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private TextField txtfldName;
    @FXML private TextField txtfldCategory;
    @FXML private TextField txtfldDescription;
    @FXML private ComboBox<String> cmbMeasure;
    private ObservableList<String> obserlistMeasure;

    public void initComboboxMeasure() {
        List<String> lst = new ArrayList<>();
        lst.add("кг");
        lst.add("гр");
        lst.add("литр");
        lst.add("мл");
        lst.add("шт");
        lst.add("метр");
        this.obserlistMeasure = FXCollections.observableArrayList(lst);
        this.cmbMeasure.getItems().clear();
        this.cmbMeasure.setItems(this.obserlistMeasure);
    }

    @FXML       //[НАЗАД]
    public void goBack() {
        this.txtfldName.setText(null);
        this.txtfldCategory.setText(null);
        this.txtfldDescription.setText(null);
    }

    @FXML       //[ДОБАВИТЬ НОВЫЙ РЕСУРС]
    public void goAdd() {

    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {

    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {

    }

    public void initResursAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить ресурс");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
    }

    public void initResursEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить ресурс");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        this.txtfldName.setText("");        //TODO
        this.txtfldCategory.setText("");
        this.txtfldDescription.setText("");
    }

}
