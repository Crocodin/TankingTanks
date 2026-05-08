package ubb.dbsm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import ubb.dbsm.Main;
import ubb.dbsm.domain.User;
import ubb.dbsm.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    @FXML private TextField usernameBox;
    @FXML private PasswordField passwordBox;

    private final ConfigurableApplicationContext springContext;
    private final UserService userService;

    @FXML private void initialize() {
        usernameBox.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                passwordBox.requestFocus();
            }
        });

        passwordBox.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                loginPressed(null);
            }
        });
    }

    @FXML private void loginPressed(ActionEvent event) {
        String username = usernameBox.getText();
        String password = passwordBox.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Main.showError("Username and/or password are empty");
            return;
        }

        try {
            User user = userService.authenticate(username, password);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/main-view.fxml"));
            fxmlLoader.setControllerFactory(springContext::getBean);

            Parent root = fxmlLoader.load();
            Stage stage = (Stage) usernameBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("TankingTanks - " + username);
            stage.setResizable(false);

            MainController controller = fxmlLoader.getController();
            controller.setLoggedUser(user);
            stage.show();
        } catch (Exception e) {
            log.error("Failed in login", e);
            Main.showError(e.getMessage());
        }
    }
}
