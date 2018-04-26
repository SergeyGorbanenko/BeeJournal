package finans;

import app.HBUtil;
import app.Main;
import app.ServiseUtil;
import hba.FinancialOperateEntity;
import hba.ResourceTypeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FinansCUDController {

    private Main mnApp;
    public void setMainApp(Main mainApp) {
        this.mnApp = mainApp;
    }

    private FinansListController finansListController;
    public void setFinansListController(FinansListController finansListController) {
        this.finansListController = finansListController;
    }

    private FinansDetailController finansDetailController;
    public void setFinansDetailController(FinansDetailController finansDetailController) {
        this.finansDetailController = finansDetailController;
    }

    private Scene ownerScene;
    public Scene getOwnerScene() {
        return ownerScene;
    }


    @FXML       //[НАЗАД]
    public void goBack() {
        Stage mainStage = mnApp.getPrimaryStage();
        if (finansDetailController != null) {
            if (finansDetailController.getOwnerScene() != null)
                mainStage.setScene(finansDetailController.getOwnerScene());
        } else if (finansListController != null) {
            if (finansListController.getOwnerScene() != null)
                mainStage.setScene(finansListController.getOwnerScene());
        }
        mnApp.setPrimaryStage(mainStage);
        mnApp.getPrimaryStage().show();
    }

    @FXML       //[ДОБАВИТЬ]
    public void goAdd() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDate.getValue() == null) throw new NullPointerException();
            if (txtfldCount.getText() == null || txtfldCount.getText().equals("")) throw new NullPointerException();
            if (txtfldUnitPrice.getText() == null || txtfldUnitPrice.getText().equals("")) throw new NullPointerException();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new NullPointerException();
            //
            if (cmbOperationType.getValue().equals("Покупка") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            if (cmbOperationType.getValue().equals("Продажа") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();

            financialOperateEntity = new FinancialOperateEntity();
            financialOperateEntity.setIdResourseType(cmbResurs.getValue().getIdResourseType());
            financialOperateEntity.setResourceTypeByIdResourseType(cmbResurs.getValue());
            if (cmbOperationType.getValue().equals("Продажа"))
                financialOperateEntity.setOperationType(true);
            if (cmbOperationType.getValue().equals("Покупка"))
                financialOperateEntity.setOperationType(false);
            financialOperateEntity.setCount(Double.valueOf(txtfldCount.getText()));
            financialOperateEntity.setUnitPrice(Double.valueOf(txtfldUnitPrice.getText()));
            financialOperateEntity.setDate(dtpckrDate.getValue());
            financialOperateEntity.setDescription(txtfldDescription.getText());
            //
            transaction = session.getTransaction();
            transaction.begin();
            session.save(financialOperateEntity);
            transaction.commit();
            //
            finansListController.changeStateToFinansDetail(this.financialOperateEntity);
        } catch (NumberFormatException npe) {
            npe.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поле \"Количество\" и \"Цена за ед.\" может содержать только числа");
            alert.showAndWait();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- дата покупки/продажи не может быть позже текущей даты.");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[ИЗМЕНИТЬ]
    public void goEdit() {
        Transaction transaction = null;
        Transaction transaction2 = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            if (dtpckrDate.getValue() == null) throw new NullPointerException();
            if (txtfldCount.getText() == null || txtfldCount.getText().equals("")) throw new NullPointerException();
            if (txtfldUnitPrice.getText() == null || txtfldUnitPrice.getText().equals("")) throw new NullPointerException();
            if (txtfldDescription.getText() == null || txtfldDescription.getText().equals("")) throw new NullPointerException();
            //
            if (cmbOperationType.getValue().equals("Покупка") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            if (cmbOperationType.getValue().equals("Продажа") && dtpckrDate.getValue().isAfter(LocalDate.now())) throw new Exception();
            // так как связь между таблицами типРесурса -* финОперация инициализирующая, то при изменении типа работы придется удалить фин операцию и создать заново
            if (!financialOperateEntity.getIdResourseType().equals(cmbResurs.getValue().getIdResourseType())) {
                transaction2 = session.getTransaction();
                transaction2.begin();
                session.delete(financialOperateEntity);
                transaction2.commit();
                //
                financialOperateEntity = new FinancialOperateEntity();
                financialOperateEntity.setIdResourseType(cmbResurs.getValue().getIdResourseType());
                financialOperateEntity.setResourceTypeByIdResourseType(cmbResurs.getValue());
                if (cmbOperationType.getValue().equals("Продажа"))
                    financialOperateEntity.setOperationType(true);
                if (cmbOperationType.getValue().equals("Покупка"))
                    financialOperateEntity.setOperationType(false);
                financialOperateEntity.setCount(Double.valueOf(txtfldCount.getText()));
                financialOperateEntity.setUnitPrice(Double.valueOf(txtfldUnitPrice.getText()));
                financialOperateEntity.setDate(dtpckrDate.getValue());
                financialOperateEntity.setDescription(txtfldDescription.getText());
                //
                transaction = session.getTransaction();
                transaction.begin();
                session.save(financialOperateEntity);
                transaction.commit();
                //
                finansListController.changeStateToFinansDetail(this.financialOperateEntity);
            } else {
                if (cmbOperationType.getValue().equals("Продажа"))
                    financialOperateEntity.setOperationType(true);
                if (cmbOperationType.getValue().equals("Покупка"))
                    financialOperateEntity.setOperationType(false);
                financialOperateEntity.setCount(Double.valueOf(txtfldCount.getText()));
                financialOperateEntity.setUnitPrice(Double.valueOf(txtfldUnitPrice.getText()));
                financialOperateEntity.setDate(dtpckrDate.getValue());
                financialOperateEntity.setDescription(txtfldDescription.getText());
                //
                transaction = session.getTransaction();
                transaction.begin();
                session.update(financialOperateEntity);
                transaction.commit();
                //
                finansListController.changeStateToFinansDetail(this.financialOperateEntity);
            }
        } catch (NumberFormatException npe) {
            npe.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- поле \"Количество\" и \"Цена за ед.\" может содержать только числа");
            alert.showAndWait();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- недопустимы пустые поля");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            } //
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Проверьте правильность введенных данных");
            alert.setContentText(   "- дата покупки/продажи не может быть позже текущей даты.");
            alert.showAndWait();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @FXML       //[УДАЛИТЬ]
    public void goDelete() {
        String foType = null;
        String foResursName = null;
        String foCount = null;
        try {
            foCount = ServiseUtil.cutZero(financialOperateEntity.getCount()) + " " + financialOperateEntity.getResourceTypeByIdResourseType().getMeasure();
            if (financialOperateEntity.getOperationType())  foType = "Продажа";
            else                                            foType = "Покупка";
            foResursName = financialOperateEntity.getResourceTypeByIdResourseType().getName();
            if (foType == null || foResursName == null || foCount == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Ошибка");
            alertError.setHeaderText("Что-то пошло не так(");
            alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить новую операцию\"");
            alertError.showAndWait();
            return;
        }
        Alert alertSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertSure.setTitle("Удаление финансовой операции");
        alertSure.setHeaderText("Удалить фин. операцию " + "[" + foType + ": " + foResursName + " " + foCount + "]" + "?");
        alertSure.setContentText("Вы уверены, что хотите удалить фин. операцию " + "[" + foType + ": " + foResursName + " " + foCount + "]" + "?");
        Optional<ButtonType> result = alertSure.showAndWait();
        if (result.get() == ButtonType.OK){
            Transaction transaction = null;
            Session session = HBUtil.getSessionFactory().openSession();
            try {

                transaction = session.beginTransaction();
                session.delete(this.financialOperateEntity);
                transaction.commit();
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Финансовая операция удалена");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("Фин. операция " + "[" + foType + ": " + foResursName + " " + foCount + "]" + " была успешна удалена!");
                alertSuccess.showAndWait();
                //
                finansListController.getMainController().changeStateToFinansList();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Ошибка");
                alertError.setHeaderText("Что-то пошло не так(");
                alertError.setContentText("- нельзя удалить несуществующую запись, сперва выполите \"Добавить новую операцию\"\n");
                alertError.showAndWait();
            } finally {
                if (session != null)
                    session.close();
            }
        } else {
            alertSure.hide();
        }
    }


    @FXML private Label lblcaptionTitle;
    @FXML private Hyperlink hyperlinkAddOrEdit;
    @FXML private Hyperlink hyperlinkDelete;
    //
    @FXML private ComboBox<String> cmbOperationType;
    @FXML private ComboBox<ResourceTypeEntity> cmbResurs;
    @FXML private TextField txtfldCount;
    @FXML private TextField txtfldUnitPrice;
    @FXML private TextField txtfldDescription;
    @FXML private DatePicker dtpckrDate;
    //

    private ObservableList<String> obserlistOperationType;
    private ObservableList<ResourceTypeEntity> obserlistResursType;

    public StringConverter<ResourceTypeEntity> converterResursName = new StringConverter<ResourceTypeEntity>() {
        @Override
        public String toString(ResourceTypeEntity resourceTypeEntity) {
            return resourceTypeEntity.getName();
        }
        @Override
        public ResourceTypeEntity fromString(String string) {
            return null;
        }
    };

    //получить индекс Ресурса (ResourceType) в obserList соответствующий редактируемой Фин.Операции (FinancialOperate)
    public int getIndexOfResurs(ResourceTypeEntity resourceTypeEntityChoose, ObservableList<ResourceTypeEntity> ObservableList){
        int index = -1;
        for (ResourceTypeEntity resourceTypeEntity : ObservableList)
            if (resourceTypeEntity.getIdResourseType().equals(resourceTypeEntityChoose.getIdResourseType()))
                index = ObservableList.indexOf(resourceTypeEntity);
        return index;
    }


    //Конкретная финансовая операция
    private FinancialOperateEntity financialOperateEntity = null;
    public FinancialOperateEntity getFinancialOperateEntity() {
        return financialOperateEntity;
    }
    public void setFinancialOperateEntity(FinancialOperateEntity financialOperateEntity) {
        this.financialOperateEntity = financialOperateEntity;
    }


    //Инициализирует Комбобоксы данными
    public void initFinansDataInCombobox() {
        List<String> lst = new ArrayList<>();
        lst.add("Покупка");
        lst.add("Продажа");
        this.obserlistOperationType = FXCollections.observableArrayList(lst);
        this.cmbOperationType.getItems().clear();
        this.cmbOperationType.setItems(this.obserlistOperationType);
        //
        this.obserlistResursType = FXCollections.observableArrayList(loadResursList());
        this.cmbResurs.setConverter(converterResursName);
        this.cmbResurs.getItems().clear();
        this.cmbResurs.setItems(obserlistResursType);
    }

    //Инициализирует элементы управления на добавление записи
    public void initFinansAddState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goAdd();
        });
        //
        this.lblcaptionTitle.setText("Добавить фин. операцию");
        this.hyperlinkAddOrEdit.setText("  Добавить");
        this.hyperlinkDelete.setVisible(false);
        //
        this.txtfldCount.setText(null);
        this.txtfldUnitPrice.setText(null);
        this.txtfldDescription.setText(null);
        this.dtpckrDate.setValue(null);
    }

    //Инициализирует элементы управления на редактирование записи
    public void initFinansEditState() {
        this.hyperlinkAddOrEdit.setOnMouseClicked(event -> {
            goEdit();
        });
        this.lblcaptionTitle.setText("Изменить фин. операцию");
        this.hyperlinkAddOrEdit.setText("  Изменить");
        this.hyperlinkDelete.setVisible(true);
        //
        if (financialOperateEntity.getOperationType())
            this.cmbOperationType.getSelectionModel().select(1);
        else
            this.cmbOperationType.getSelectionModel().select(0);
        this.cmbResurs.getSelectionModel().select(getIndexOfResurs(financialOperateEntity.getResourceTypeByIdResourseType(), obserlistResursType));
        this.txtfldCount.setText(ServiseUtil.cutZero(financialOperateEntity.getCount()));
        this.txtfldUnitPrice.setText(ServiseUtil.cutZero(financialOperateEntity.getUnitPrice()));
        this.txtfldDescription.setText(financialOperateEntity.getDescription());
        this.dtpckrDate.setValue(financialOperateEntity.getDate());
    }


    private List<ResourceTypeEntity> resourceTypeEntityList;
    //Получить список Ресурсов
    public List<ResourceTypeEntity> loadResursList() {
        Transaction transaction = null;
        Session session = HBUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<ResourceTypeEntity> query = builder.createQuery(ResourceTypeEntity.class);
            Root<ResourceTypeEntity> root = query.from(ResourceTypeEntity.class);
            query.select(root);
            Query<ResourceTypeEntity> q = session.createQuery(query);
            this.resourceTypeEntityList = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null)
                session.close();
        }
        return this.resourceTypeEntityList;
    }

}
