package app;

import hba.WorkEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
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
    @FXML private ComboBox<String> cmbRazdelName;
    @FXML private DatePicker dtDateStart;
    @FXML private DatePicker dtDateEnd;
    //
    @FXML private Pane workPane;
    @FXML private Label lblCountPlan;
    @FXML private Label lblCountProcess;
    @FXML private Label lblCountDone;
    @FXML private Label lblCountOverdue;
    //
    @FXML private Pane resursPane;
    //
    @FXML private Pane finansPane;

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
            if (cmbRazdelName.getValue().equals("Работы")) {
                lblTitle.setText("Статистика по работам");
                calculateWorkStatistic();
                workPane.setVisible(true);
                resursPane.setVisible(false);
                finansPane.setVisible(false);
            }
            if (cmbRazdelName.getValue().equals("Ресурсы")) {
                lblTitle.setText("Статистика по ресурсам");
                calculateResursStatistic();
                resursPane.setVisible(true);
                workPane.setVisible(false);
                finansPane.setVisible(false);
            }
            if (cmbRazdelName.getValue().equals("Финансы")) {
                lblTitle.setText("Статистика по финансам");
                calculateFinansStatistic();
                finansPane.setVisible(true);
                workPane.setVisible(false);
                resursPane.setVisible(false);
            }
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

    //Рассчет статистики по работам
    public void calculateWorkStatistic() {
        int countPlan = 0;
        int countProcess = 0;
        int countDone = 0;
        int countOverdue = 0;
        Collection<WorkEntity> workEntityCollection = mainController.loadWorkList();
        for (WorkEntity wrkE : workEntityCollection) {
            if (wrkE.getDateStart().isAfter(dtDateStart.getValue()) && wrkE.getDateEnd().isBefore(dtDateEnd.getValue())) {
                if (wrkE.getWorkStatus().equals("Планируется")) countPlan++;
                if (wrkE.getWorkStatus().equals("В процессе")) countProcess++;
                if (wrkE.getWorkStatus().equals("Выполнена")) countDone++;
                if (wrkE.getWorkStatus().equals("Просрочена")) countOverdue++;
            }
        }
        lblCountPlan.setText(String.valueOf(countPlan));
        lblCountProcess.setText(String.valueOf(countProcess));
        lblCountDone.setText(String.valueOf(countDone));
        lblCountOverdue.setText(String.valueOf(countOverdue));
    }

    //Рассчет статистики по ресурсам
    public void calculateResursStatistic() {

    }

    //Рассчет статистики по финансам
    public void calculateFinansStatistic() {

    }
}
