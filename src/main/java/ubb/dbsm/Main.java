package ubb.dbsm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ubb.dbsm.utils.JPAUtils;

public class Main extends Application {
    private ApplicationContext springContext;
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void init() {
        springContext = new ClassPathXmlApplicationContext("applicationConfig.java.xml");
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug("Crating the fxmlLoader");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);  // Spring creates the controller

        logger.debug("Loading the fxmlLoader");
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("TankingTanks");
        stage.setResizable(false);

        logger.debug("Opening main window");
        stage.show();
    }

    @Override
    public void stop() {
        logger.info("Application shutting down, closing resources");
        JPAUtils.closeEntityManagerFactory();
        ((ClassPathXmlApplicationContext) springContext).close();
        logger.info("Resources closed successfully");
    }

    public static void main(String[] args) {
        logger.debug("Application starting");
        launch(args);
    }

    public static void showInfo(String msg) {
        logger.debug("Showing info: {}", msg);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public static void showError(String msg) {
        logger.debug("Showing error: {}", msg);
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}
