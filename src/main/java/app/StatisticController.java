package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticController {
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

    @FXML
    public void changeStateToHome() {
        mnApp.initRootLayout();
    }

    @FXML       //[ДОМОЙ]
    public void goHome() {
        changeStateToHome();
    }

    @FXML private Label lblTitle;
    @FXML private ScrollPane scrlPane;
    @FXML private ComboBox<String> cmbRazdelName;
    @FXML private DatePicker dtDateStart;
    @FXML private DatePicker dtDateEnd;

    private ObservableList<String> obserlistRazdelName;

    //Инициализировать комбобокс выбора раздела статистики
    public void initCombobox() {
        List<String> lst = new ArrayList<>();
        lst.add("Работы");
        lst.add("Ресурсы");
        lst.add("Финансы");
        this.obserlistRazdelName = FXCollections.observableArrayList(lst);
        this.cmbRazdelName.getItems().clear();
        this.cmbRazdelName.setItems(this.obserlistRazdelName);
    }

    public void performCalculateStatistic() {
        try {
            if (cmbRazdelName.getValue() == null) throw new NullPointerException();
            if (dtDateStart.getValue() == null) throw new NullPointerException();
            if (dtDateEnd.getValue() == null) throw new NullPointerException();
            if (dtDateStart.getValue().isAfter(dtDateEnd.getValue())) throw new Exception();
            if (cmbRazdelName.getValue().equals("Работы"))
                calculateWorkStatistic();
            if (cmbRazdelName.getValue().equals("Ресурсы"))
                calculateResursStatistic();
            if (cmbRazdelName.getValue().equals("Финансы"))
                calculateFinansStatistic();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- дата начала статистического периода не может быть позже даты окончания");
            alert.showAndWait();
        }
    }

    public void calculateWorkStatistic() {

    }

    public void calculateResursStatistic() {

    }

    public void calculateFinansStatistic() {

    }
}
