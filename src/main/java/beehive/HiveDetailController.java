package beehive;

import app.HBUtil;
import app.Main;
import app.MainController;
import hba.BeehiveEntity;
import hba.CountFrameEntity;
import hba.IncomeExpenseEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import resurs.ResursCUDController;
import resurs.ResursListController;
import work.WorkCUDController;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public class HiveDetailController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private HiveListController hiveListController;
    public void setHiveListController(HiveListController hiveListController) {
        this.hiveListController = hiveListController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }

    public void changeStateToHiveList() {
        hiveListController.getMainController().changeStateToHiveList();
    }

    private HiveCUDController hiveCUDController;
    private BorderPane hiveCUDLayout;
    private Scene hiveCUDScene;
    public void changeStateToHiveCUD() {
        ownerScene = mnApp.getPrimaryStage().getScene();
        if (hiveCUDScene != null) {
            Stage mainStage = mnApp.getPrimaryStage();
            mainStage.setScene(hiveCUDScene);
            mnApp.setPrimaryStage(mainStage);
            mnApp.getPrimaryStage().show();
            hiveCUDController.setHiveDetailController(this);
            hiveCUDController.setHiveListController(hiveListController);
            hiveCUDController.setMainApp(mnApp);
            hiveCUDController.initComboboxBeegarden();
            hiveCUDController.setBeehiveEntity(beehiveEntity);
            hiveCUDController.initHiveEditState();
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("/beehive/HiveCUD.fxml"));
                hiveCUDLayout = (BorderPane) loader.load();
                hiveCUDScene = new Scene(hiveCUDLayout);
                Stage mainStage = mnApp.getPrimaryStage();
                mainStage.setScene(hiveCUDScene);
                mnApp.setPrimaryStage(mainStage);
                mnApp.getPrimaryStage().show();
                hiveCUDController = loader.getController();
                hiveCUDController.setHiveDetailController(this);
                hiveCUDController.setHiveListController(hiveListController);
                hiveCUDController.setMainApp(mnApp);
                hiveCUDController.initComboboxBeegarden();
                hiveCUDController.setBeehiveEntity(beehiveEntity);
                hiveCUDController.initHiveEditState();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML private Label lblTitleHiveNumber;
    @FXML private Label lblvalueType;
    @FXML private Label lblvalueBeegarden;
    @FXML private Label lblvalueDescription;
    @FXML private ScrollPane scrlPaneCountFrame;
    @FXML private ScrollPane scrlPaneResurs;
    @FXML private Label lblCountFramesNotFound;
    @FXML private Label lblResursNotFound;
    //
    @FXML private DatePicker dtpckrDateCountFrameAdd;
    @FXML private TextField txtfldCountFrameAdd;


    @FXML       //[НАЗАД]
    public void goBack() {
        changeStateToHiveList();
    }

    @FXML       //[РЕД]
    public void goEdit() {
        changeStateToHiveCUD();
    }

    private BeehiveEntity beehiveEntity = null;
    public void initDataInHiveDetail(BeehiveEntity beehiveEntity) {
        this.beehiveEntity = beehiveEntity;
        this.lblTitleHiveNumber.setText("Улей № " + beehiveEntity.getHiveNumber());
        this.lblvalueType.setText(beehiveEntity.getHiveType());
        this.lblvalueBeegarden.setText(beehiveEntity.getBeegardenByIdBeegarden().getName());
        this.lblvalueDescription.setText(beehiveEntity.getDescription());
        //
        viewCountFrames(beehiveEntity.getCountFrames());
        viewResursHistory(beehiveEntity.getIncomeExpenses());
    }

    //Конкретная запись о количестве рамок
    private CountFrameEntity countFrameEntity = null;

    //Вывести спсок Истории рамок по конкретному улью
    public void viewCountFrames(Collection<CountFrameEntity> countFrameEntityList) {
        if (countFrameEntityList.isEmpty()) {
            this.lblCountFramesNotFound.setVisible(true);
            scrlPaneCountFrame.setContent(new AnchorPane());
            return;
        }
        this.lblCountFramesNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPaneCountFrame.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 10;
        Integer aPanePrefHeight = 50;
        for (CountFrameEntity cfE : countFrameEntityList) {
            GridPane gridPane = new GridPane();
            //gridPane.setStyle("-fx-background-color:  #ffd6e4;");
            gridPane.setPadding(new Insets(5, 10, 5, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY);
            gridPaneLayoutY += 35;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(290);
            //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru", "RU"));
            Label lblvalueDate = new Label(cfE.getCheckDate().format(formatter));
            Label lblvalueCount = new Label(cfE.getCountFrame() + " шт");
            Label lblIconEdit = new Label();
            Label lblIconDelete = new Label();
            //
            DatePicker dtpckrData = new DatePicker();
            TextField txtfldCount = new TextField();
            dtpckrData.setVisible(false);
            txtfldCount.setVisible(false);
            dtpckrData.setPrefWidth(100);
            txtfldCount.setPrefWidth(50);
            txtfldCount.setMaxWidth(50);
            dtpckrData.setValue(cfE.getCheckDate());
            txtfldCount.setText(cfE.getCountFrame());
            //
            lblvalueDate.setFont(new Font("Arial Bold", 13));
            lblvalueCount.setFont(new Font("Arial Bold", 13));
            lblvalueCount.setTextFill(Color.web("#7f5c2f"));
            lblIconDelete.setFont(new Font("Arial Bold", 18));
            lblIconDelete.setStyle("-fx-background-image: url('/icons/DeleteMini.png')");
            lblIconDelete.setPrefHeight(25);
            lblIconDelete.setPrefWidth(22);
            lblIconEdit.setFont(new Font("Arial Bold", 18));
            lblIconEdit.setStyle("-fx-background-image: url('/icons/shestYellow2.png')");
            lblIconEdit.setPrefHeight(25);
            lblIconEdit.setPrefWidth(25);
            //
            gridPane.add(lblvalueDate, 0, 0, 1, 1);
            gridPane.add(dtpckrData, 0, 0, 1, 1);
            gridPane.add(lblvalueCount, 1, 0, 1, 1);
            gridPane.add(txtfldCount, 1, 0, 1, 1);
            gridPane.add(lblIconEdit, 2, 0, 1,1);
            gridPane.add(lblIconDelete, 3, 0, 1,1);
            //
            col1.setPercentWidth(44);
            col2.setPercentWidth(30);
            col3.setPercentWidth(13);
            col4.setPercentWidth(13);
            gridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
            //
            dtpckrData.setOnAction(event -> {
                performEditCountFrame(cfE, dtpckrData, txtfldCount);
            });
            txtfldCount.setOnAction(event -> {
                performEditCountFrame(cfE, dtpckrData, txtfldCount);
            });
            lblIconEdit.setOnMouseClicked(event ->  {
                setShownFieldsForEditCF(dtpckrData, txtfldCount);
            });
            lblIconDelete.setOnMouseClicked(event -> {
                performDeleteCountFrame(cfE);
            });
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                this.countFrameEntity = cfE;
                //changeStateToHiveDetail(this.beehiveEntity);
            });
            //
            aPane = (AnchorPane) scrlPaneCountFrame.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight);
            aPanePrefHeight += 35;
            scrlPaneCountFrame.setContent(aPane);
        }
    }

    //Вывести список Истории ресурса по кокретному улью
    public void viewResursHistory(Collection<IncomeExpenseEntity> incomeExpenseEntityList) {
        if (incomeExpenseEntityList.isEmpty()) {
            this.lblResursNotFound.setVisible(true);
            scrlPaneResurs.setContent(new AnchorPane());
            return;
        }
        this.lblResursNotFound.setVisible(false);
        AnchorPane aPane = null;
        scrlPaneResurs.setContent(new AnchorPane());
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        Integer gridPaneLayoutY = 10;
        Integer gridPaneLayoutX = 5;
        Integer aPanePrefHeight = 50;
        for (IncomeExpenseEntity ieE : incomeExpenseEntityList) {
            GridPane gridPane = new GridPane();
            //gridPane.setStyle("-fx-background-color:  #ceffd7;");
            gridPane.setPadding(new Insets(5, 10, 5, 10));
            gridPane.setHgap(3);
            gridPane.setVgap(3);
            gridPane.setLayoutY(gridPaneLayoutY); gridPaneLayoutY+=50;
            gridPane.setLayoutX(gridPaneLayoutX);
            gridPane.setPrefWidth(302);
            //
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMM", new Locale("ru", "RU"));
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE");
            Label lblvalueDate = new Label(ieE.getDate().format(formatter1));
            Label lblvalueDayDate = new Label("   " + ieE.getDate().format(formatter2));
            Label lblvalueDescription = new Label(ieE.getDescription());
            Label lblvalueResursName = new Label(ieE.getResourceTypeByIdResourseType().getName());
            Label lblvalueCount = new Label();
            //
            if (ieE.getOperationType()) {
                lblvalueCount.setText("+" + ieE.getCount() + " " + ieE.getResourceTypeByIdResourseType().getMeasure());
                lblvalueCount.setTextFill(Color.GREEN);
            } else {
                lblvalueCount.setText("-" + ieE.getCount() + " " + ieE.getResourceTypeByIdResourseType().getMeasure());
                lblvalueCount.setTextFill(Color.RED);
            }
            //
            lblvalueDate.setFont(new Font("Arial Bold", 13));
            lblvalueDayDate.setFont(new Font("Arial Bold", 12));
            lblvalueDescription.setFont(new Font("Arial Bold", 14));
            lblvalueDescription.setTextFill(Color.web( "#703e00"));
            lblvalueDescription.setWrapText(true);
            lblvalueCount.setFont(new Font("Arial Bold", 14));
            lblvalueResursName.setFont(new Font("Arial", 12));
            //
            gridPane.add(lblvalueDate, 0, 0, 1, 1);
            gridPane.add(lblvalueDayDate, 0, 1, 1, 1);
            gridPane.add(lblvalueResursName, 1, 0, 1, 1);
            gridPane.add(lblvalueDescription, 1, 1, 1, 1);
            gridPane.add(lblvalueCount, 2, 0, 1, 2);
            //
            col1.setPercentWidth(18);
            col2.setPercentWidth(64);
            col3.setPercentWidth(18);
            gridPane.getColumnConstraints().addAll(col1, col2, col3);
            //
            gridPane.setOnMouseClicked((MouseEvent event) -> {
                ResursListController resursListController = new ResursListController();
                resursListController.setMainApp(mnApp);
                resursListController.setHiveListController(hiveListController);
                resursListController.changeStateToResursHistory(ieE.getResourceTypeByIdResourseType());
            });
            //
            aPane = (AnchorPane) scrlPaneResurs.getContent();
            aPane.getChildren().add(gridPane);
            aPane.setPrefHeight(aPanePrefHeight); aPanePrefHeight+=50;
            scrlPaneResurs.setContent(aPane);
        }
    }


    @FXML//      [ПОКАЗАТЬ РАБОТЫ ПО УЛЬЮ]
    public void showWorksByBeehive() {
        MainController mainController = new MainController();
        mainController.setMainApp(mnApp);
        mainController.setWorkEntityList(beehiveEntity.getWorks());
        mainController.changeStateToWorkList();
    }


    @FXML       //[УДАЛИТЬ ЗАПИСЬ О КОЛИЧЕСТВЕ РАМОК]
    public void performDeleteCountFrame(CountFrameEntity countFrameEntity) {
        String cfData = null;
        String cfCount = null;
        try {
            cfData = countFrameEntity.getCheckDate().toString();
            cfCount = countFrameEntity.getCountFrame();
            if (cfData == null || cfCount == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление записи о количестве рамок");
        alertSure.setHeaderText("Удалить запись " + "[" + cfData + " было " + cfCount + " шт]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить запись " + "[" + cfData + " было " + cfCount + " шт]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction1 = null;
            Session session = null;
            try {
                session = HBUtil.getSessionFactory().openSession();
                transaction1 = session.beginTransaction();
                session.delete(countFrameEntity);
                transaction1.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Запись о количестве рамок удалена");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Запись " + "[" + cfData + " было " + cfCount + " шт]" + " была успешна удалена из истории улья!");
                alertSuccess.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction1 != null) {
                    transaction1.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
            //
            initBeehive();
            viewCountFrames(beehiveEntity.getCountFrames());
        } else {
            alertSure.hide();
        }
    }


    //Скрываем / показываем элементы для ввода новых данных при создании НОВОЙ записи о количестве рамок улья
    @FXML
    public void setShownFieldsForNewCF() {
        if (!dtpckrDateCountFrameAdd.isVisible()) {
            this.dtpckrDateCountFrameAdd.setValue(null);
            this.txtfldCountFrameAdd.setText(null);
            this.dtpckrDateCountFrameAdd.setVisible(true);
            this.txtfldCountFrameAdd.setVisible(true);
        } else {
            this.dtpckrDateCountFrameAdd.setVisible(false);
            this.txtfldCountFrameAdd.setVisible(false);
        }
    }

    //Скрываем / показываем элементы для ввода новых данных при изменении СУЩЕСТВУЮЩЕЙ записи о количестве рамок улья
    @FXML
    public void setShownFieldsForEditCF(DatePicker dtpckrDateCountFrameEdit, TextField txtfldCountFrameEdit) {
        if (!dtpckrDateCountFrameEdit.isVisible()) {
            dtpckrDateCountFrameEdit.setVisible(true);
            txtfldCountFrameEdit.setVisible(true);
        } else {
            dtpckrDateCountFrameEdit.setVisible(false);
            txtfldCountFrameEdit.setVisible(false);
        }
    }

    //[РЕДАКТИРОВАТЬ ЗАПИСЬ О КОЛИЧЕСТВЕ РАМОК]
    public void performEditCountFrame(CountFrameEntity countFrameEntity, DatePicker dtpckrDate, TextField txtfldCount) {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (countFrameEntity == null) throw new Exception();
            if (dtpckrDate.getValue() == null) throw new Exception();
            if (txtfldCount.getText() == null || txtfldCount.getText().equals("")) throw new Exception();
            if (dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            countFrameEntity.setCheckDate(dtpckrDate.getValue());
            countFrameEntity.setCountFrame(txtfldCount.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(countFrameEntity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- введенная дата не может быть позже текущей даты");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
        //
        initBeehive();
        viewCountFrames(beehiveEntity.getCountFrames());
    }

    @FXML //[ДОБАВИТЬ ЗАПИСЬ О КОЛИЧЕСТВЕ РАМОК]
    public void performAddCountFrame() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDateCountFrameAdd.getValue() == null) throw new Exception();
            if (txtfldCountFrameAdd.getText() == null || txtfldCountFrameAdd.getText().equals("")) throw new Exception();
            if (dtpckrDateCountFrameAdd.getValue().isAfter(LocalDate.now())) throw new Exception();
            CountFrameEntity countFrameEntity = new CountFrameEntity();
            countFrameEntity.setCheckDate(this.dtpckrDateCountFrameAdd.getValue());
            countFrameEntity.setCountFrame(this.txtfldCountFrameAdd.getText());
            countFrameEntity.setIdBeehive(beehiveEntity.getIdBeehive());
            countFrameEntity.setBeehive(beehiveEntity);
            countFrameEntity.setIdBeegarden(new WorkCUDController().loadBeegarden().getIdBeegarden());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(countFrameEntity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля\n" +
                    "- введенная дата не может быть позже текущей даты");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
        //
        initBeehive();
        setShownFieldsForNewCF();
        viewCountFrames(beehiveEntity.getCountFrames());
    }

    //Инициализировать Улей (BeehiveEntity)
    private void initBeehive() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HBUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<BeehiveEntity> query = builder.createQuery(BeehiveEntity.class);
            Root<BeehiveEntity> root = query.from(BeehiveEntity.class);
            query.select(root).where(builder.equal(root.get("idBeehive"), this.beehiveEntity.getIdBeehive()));
            Query<BeehiveEntity> q = session.createQuery(query);
            this.beehiveEntity = null;
            this.beehiveEntity = q.getSingleResult();
            transaction.commit();
        }  catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        } finally {
            if (session != null)
                session.close();
        }
    }

}
