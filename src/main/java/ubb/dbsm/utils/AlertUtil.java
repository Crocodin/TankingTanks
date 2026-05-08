package ubb.dbsm.utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void showConflictError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update Conflict");
        alert.setHeaderText("Record Modified By Another User");
        alert.setContentText(
                "The record you are trying to update was modified by someone else.\n" +
                        "Please reload the data and try again."
        );
        alert.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
