package app;

import hba.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import work.WorkCUDController;

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
    @FXML private ScrollPane scrlPaneResurs;
    @FXML private Pane finansPane;
    @FXML private ScrollPane scrlPaneFinans;
    @FXML private ScrollPane scrlPaneWorkKind;

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
                //calculateWorkStatistic();
                calculateWorkKindStatistic();
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
            if (wrkE.getDateStart().isAfter(dtDateStart.getValue().minusDays(1)) && wrkE.getDateEnd().isBefore(dtDateEnd.getValue().plusDays(1))) {
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

    //Рассчет статистики по видам работ
    private void calculateWorkKindStatistic() {
        AnchorPane aPane = null;
        scrlPaneWorkKind.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        //
        List<WorkKindEntity> workKindEntityList = new WorkCUDController().loadWorkKindList();
        for (WorkKindEntity workKindEntity : workKindEntityList) {
            Collection<WorkEntity> workEntityList = workKindEntity.getWorksByIdWorkKind();
            int count = 0;
            for (WorkEntity wEntity : workEntityList)
                if (wEntity.getDateEnd().isAfter(dtDateStart.getValue().minusDays(1)) && wEntity.getDateEnd().isBefore(dtDateEnd.getValue().plusDays(1)))
                    if (wEntity.getWorkStatus().equals("Выполнена")) count++;

            //
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
            Label lblWorkKind = new Label(workKindEntity.getName());
            Label lblCount = new Label(String.valueOf(count));
            lblWorkKind.setFont(new Font("Arial", 14));
            lblCount.setFont(new Font("Arial Bold", 14));
            //
            gridPane.add(lblWorkKind, 0, 0, 1, 1);
            gridPane.add(lblCount, 1, 0, 1, 1);
            col1.setPercentWidth(60);
            col2.setPercentWidth(40);
            gridPane.getColumnConstraints().addAll(col1, col2);
            aPane = (AnchorPane) scrlPaneWorkKind.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 25;
            scrlPaneWorkKind.setContent(aPane);
        }
    }

    //Рассчет статистики по ресурсам
    private void calculateResursStatistic() {
        double dohodCount = 0;
        double rashodCount = 0;
        List<ResourceTypeEntity> resourceTypeEntityList = mainController.loadResursList();
        //
        AnchorPane aPane = null;
        scrlPaneResurs.setContent(new AnchorPane());
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
                if (ieE.getDate().isAfter(dtDateStart.getValue().minusDays(1)) && ieE.getDate().isBefore(dtDateEnd.getValue().plusDays(1))) {
                    if (ieE.getOperationType()) dohodCount += ieE.getCount();
                    else                        rashodCount += ieE.getCount();
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
            Label lblDohod = new Label(ServiseUtil.cutZero(dohodCount));
            Label lblRashod = new Label(ServiseUtil.cutZero(rashodCount));
            Label lblItog = new Label(ServiseUtil.cutZero(dohodCount - rashodCount) + " " + rsE.getMeasure());
            lblResursName.setFont(new Font("Arial", 13));
            lblDohod.setFont(new Font("Arial Bold", 13));
            lblRashod.setFont(new Font("Arial Bold", 13));
            lblItog.setFont(new Font("Arial Bold", 14));
            if ((dohodCount - rashodCount) > 0)
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
            aPane = (AnchorPane) scrlPaneResurs.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 25;
            scrlPaneResurs.setContent(aPane);
            //
            dohodCount = 0;
            rashodCount = 0;
        }
    }

    //Рассчет статистики по финансам
    private void calculateFinansStatistic() {
        double dohodCount = 0;
        double rashodCount = 0;
        double dohodSummaryPrice = 0;
        double rashodSummaryPrice = 0;
        double globalItog = 0;
        List<ResourceTypeEntity> resourceTypeEntityList = mainController.loadResursList();
        //
        AnchorPane aPane = null;
        scrlPaneFinans.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (ResourceTypeEntity rsE : resourceTypeEntityList) {
            Collection<FinancialOperateEntity> financialOperateEntityCollection = rsE.getFinancialOperatesByIdResourseType();
            for (FinancialOperateEntity foE : financialOperateEntityCollection) {
                if (foE.getDate().isAfter(dtDateStart.getValue().minusDays(1)) && foE.getDate().isBefore(dtDateEnd.getValue().plusDays(1))) {
                    if (foE.getOperationType()) {
                        dohodCount += foE.getCount();
                        dohodSummaryPrice += foE.getCount() * foE.getUnitPrice();
                    } else {
                        rashodCount += foE.getCount();
                        rashodSummaryPrice += foE.getCount() * foE.getUnitPrice();
                    }
                }
            }
            globalItog += dohodSummaryPrice - rashodSummaryPrice;
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
            Label lblDohod = new Label(ServiseUtil.cutZero(dohodSummaryPrice));
            Label lblRashod = new Label(ServiseUtil.cutZero(rashodSummaryPrice));
            Label lblItog = new Label(ServiseUtil.cutZero(dohodSummaryPrice - rashodSummaryPrice) + " P");
            lblResursName.setFont(new Font("Arial", 13));
            lblDohod.setFont(new Font("Arial Bold", 13));
            lblRashod.setFont(new Font("Arial Bold", 13));
            lblItog.setFont(new Font("Arial Bold", 14));
            if ((dohodSummaryPrice - rashodSummaryPrice) > 0)
                lblItog.setTextFill(Color.GREEN);
            else
                lblItog.setTextFill(Color.RED);
            //
            gridPane.add(lblResursName, 0, 0, 1, 1);
            gridPane.add(lblDohod, 1, 0, 1, 1);
            gridPane.add(lblRashod, 2, 0, 1, 1);
            gridPane.add(lblItog, 3, 0, 1, 1);
            col1.setPercentWidth(39);
            col2.setPercentWidth(23);
            col3.setPercentWidth(17);
            col4.setPercentWidth(21);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            aPane = (AnchorPane) scrlPaneFinans.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 25;
            scrlPaneFinans.setContent(aPane);
            //
            dohodSummaryPrice = 0;
            rashodSummaryPrice = 0;
        }
        Label lblGlobalItog = new Label();
        lblGlobalItog.setFont(new Font("Arial Bold", 14));
        if (globalItog > 0) {
            lblGlobalItog.setText("Прибыль: " + ServiseUtil.cutZero(globalItog) + " P  ");
            lblGlobalItog.setTextFill(Color.GREEN);
        } else {
            lblGlobalItog.setText("Убыток: " + ServiseUtil.cutZero(globalItog) + " P  ");
            lblGlobalItog.setTextFill(Color.RED);
        }
        AnchorPane anchorPane = (AnchorPane) scrlPaneFinans.getContent();
        VBox vBox = new VBox(anchorPane, lblGlobalItog);
        vBox.setAlignment(Pos.TOP_RIGHT);
        scrlPaneFinans.setContent(vBox);
    }

}
