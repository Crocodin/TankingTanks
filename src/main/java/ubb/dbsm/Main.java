package ubb.dbsm;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class Main extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = SpringApplication.run(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        log.debug("Crating the fxmlLoader");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);  // Spring creates the controller

        log.debug("Loading the fxmlLoader");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("TankingTanks");
        stage.setResizable(false);

        log.debug("Opening main window");
        stage.show();
    }

    @Override
    public void stop() {
        log.info("Application shutting down, closing resources");
        springContext.close();
        log.info("Application shutting down");
        Platform.exit();
    }

    public static void main(String[] args) {
        log.debug("Application starting");
        launch(args);
    }

    public static void showInfo(String msg) {
        log.debug("Showing info: {}", msg);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public static void showError(String msg) {
        log.debug("Showing error: {}", msg);
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}
