import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Scene rootScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        this.primaryStage.setTitle("Пасечный журнал");
        initRootLayout();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Main.fxml"));
            rootLayout = (BorderPane) loader.load();
            rootScene = new Scene(rootLayout);
            primaryStage.setScene(rootScene);
            primaryStage.show();
            MainController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}