package beehive;

import app.HBUtil;
import app.Main;
import hba.BeegardenEntity;
import hba.BeehiveEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import work.WorkCUDController;

import java.util.Optional;

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
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (txtfldHiveNumber.getText() == null || txtfldHiveNumber.getText().equals("")) throw new Exception();
            if (txtfldHiveType.getText() == null || txtfldHiveType.getText().equals("")) throw new Exception();
            if (txtfldHiveDescription.getText() == null || txtfldHiveDescription.getText().equals("")) throw new Exception();
            beehiveEntity = new BeehiveEntity();
            beehiveEntity.setHiveNumber(this.txtfldHiveNumber.getText());
            beehiveEntity.setHiveType(this.txtfldHiveType.getText());
            beehiveEntity.setDescription(this.txtfldHiveDescription.getText());
            beehiveEntity.setIdBeegarden(this.cmbBeegarden.getValue().getIdBeegarden());
            beehiveEntity.setBeegardenByIdBeegarden(this.cmbBeegarden.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(beehiveEntity);
            transaction.commit();
            //
            hiveListController.changeStateToHiveDetail(beehiveEntity);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (beehiveEntity == null) throw new Exception();
            if (txtfldHiveNumber.getText() == null || txtfldHiveNumber.getText().equals("")) throw new Exception();
            if (txtfldHiveType.getText() == null || txtfldHiveType.getText().equals("")) throw new Exception();
            if (txtfldHiveDescription.getText() == null || txtfldHiveDescription.getText().equals("")) throw new Exception();
            beehiveEntity.setHiveNumber(this.txtfldHiveNumber.getText());
            beehiveEntity.setHiveType(this.txtfldHiveType.getText());
            beehiveEntity.setDescription(this.txtfldHiveDescription.getText());
            beehiveEntity.setIdBeegarden(this.cmbBeegarden.getValue().getIdBeegarden());
            beehiveEntity.setBeegardenByIdBeegarden(this.cmbBeegarden.getValue());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.update(beehiveEntity);
            transaction.commit();
            //
            hiveListController.changeStateToHiveDetail(beehiveEntity);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String hiveNumber = null;
        String hiveType = null;
        try {
            hiveNumber = beehiveEntity.getHiveNumber();
            hiveType = beehiveEntity.getHiveType();
            if (hiveNumber == null || hiveType == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Создать новый улей\"");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление улья");
        alertSure.setHeaderText("Удалить улей " + "[№ " + hiveNumber + " " + hiveType + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить улей " + "[№ " + hiveNumber + " " + hiveType + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {
                if (!beehiveEntity.getCountFrames().isEmpty()) throw new Exception();
                if (!beehiveEntity.getWorks().isEmpty()) throw new Exception();
                if (!beehiveEntity.getIncomeExpenses().isEmpty()) throw new Exception();
                transaction = session.beginTransaction();
                session.delete(this.beehiveEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Улей удален");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Улей " + "[№ " + hiveNumber + " " + hiveType + "]" + " был успешно удален!");
                alertSuccess.showAndWait();
                //
                hiveListController.getMainController().changeStateToHiveList();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("- нельзя удалить улей, у которого есть история количества рамок\n" +
                        "- нельзя удалить улей, у которого есть история приходов/расходов\n" +
                        "- нельзя удалить улей, по которому есть работы");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
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
