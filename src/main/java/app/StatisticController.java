package app;

import hba.IncomeExpenseEntity;
import hba.ResourceTypeEntity;
import hba.WorkEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    @FXML private ScrollPane scrlPane;
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
    private void calculateWorkStatistic() {
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
    private void calculateResursStatistic() {
        double dohod = 0;
        double rashod = 0;
        List<ResourceTypeEntity> resourceTypeEntityList = mainController.loadResursList();
        //
        AnchorPane aPane = null;
        scrlPane.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (ResourceTypeEntity rsE : resourceTypeEntityList) {
            Collection<IncomeExpenseEntity> incomeExpenseEntityCollection = rsE.getIncomeExpensesByIdResourseType();
            for (IncomeExpenseEntity ieE : incomeExpenseEntityCollection) {
                if (ieE.getDate().isAfter(dtDateStart.getValue()) && ieE.getDate().isBefore(dtDateEnd.getValue())) {
                    if (ieE.getOperationType()) dohod += ieE.getCount();
                    else rashod += ieE.getCount();
                }
            }
            //Динамическое формирование элементов управления
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(10, 5, 10, 5));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 25;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            Label lblResursName = new Label(rsE.getName());
            Label lblDohod = new Label(ServiseUtil.cutZero(dohod));
            Label lblRashod = new Label(ServiseUtil.cutZero(rashod));
            Label lblItog = new Label(ServiseUtil.cutZero(dohod - rashod) + " " + rsE.getMeasure());
            lblResursName.setFont(new Font("Arial", 13));
            lblDohod.setFont(new Font("Arial Bold", 13));
            lblRashod.setFont(new Font("Arial Bold", 13));
            lblItog.setFont(new Font("Arial Bold", 14));
            if ((dohod - rashod) > 0)
                lblItog.setTextFill(Color.GREEN);
            else
                lblItog.setTextFill(Color.RED);
            //
            gridPane.add(lblResursName, 0, 0, 1, 1);
            gridPane.add(lblDohod, 1, 0, 1, 1);
            gridPane.add(lblRashod, 2, 0, 1, 1);
            gridPane.add(lblItog, 3, 0, 1, 1);
            col1.setPercentWidth(40);
            col2.setPercentWidth(24);
            col3.setPercentWidth(18);
            col4.setPercentWidth(18);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            aPane = (AnchorPane) scrlPane.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 25;
            scrlPane.setContent(aPane);
            //
            dohod = 0;
            rashod = 0;
        }
    }

    //Рассчет статистики по финансам
    private void calculateFinansStatistic() {

    }

}
