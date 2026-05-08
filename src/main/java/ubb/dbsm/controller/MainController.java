package ubb.dbsm.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import ubb.dbsm.Main;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.service.ManufacturerService;
import ubb.dbsm.service.TankService;

@Controller
public class MainController {
    @FXML public Label pageCountLabel;
    @FXML public ComboBox<Integer> pageSizeComboBox;

    @FXML public Label pageCountLabelChildren;
    @FXML public ComboBox<Integer> pageSizeComboBoxChildren;

    @FXML private TableView<Manufacturer> manufacturerTabableView;
    @FXML private TableColumn<Manufacturer, Integer> manufacturerIdColumn;
    @FXML private TableColumn<Manufacturer, String> manufacturerNameColumn, manufacturerCountryColumn;


    private static class PageableTable {
        int page = 0;
        int pageSize;
        boolean hasNext;
        boolean hasPrevious;

        public PageableTable(int pageSize) {
            this.pageSize = pageSize;
        }
    }
    private final PageableTable manufacturerPageable = new PageableTable(10);
    private final PageableTable tankPageable =  new PageableTable(10);

    @FXML private TableView<Tank> tankTabableView;
    @FXML private TableColumn<Tank, Integer> tankIdColumn, tankProductionColumn;
    @FXML private TableColumn<Tank, String> tankNameColumn, tankManufacturerNameColumn;

    @FXML private Button addTankButton, updateTankButton, removeTankButton, refreshTables;
    @FXML private TextField tankNameTextField, tankManufacturerNameTextField, tankProductionTextField, searchTextField;

    private final TankService tankService;
    private final ManufacturerService manufacturerService;

    public MainController(TankService tankService, ManufacturerService manufacturerService) {
        this.tankService = tankService;
        this.manufacturerService = manufacturerService;
    }

    private final ObservableList<Manufacturer> manufacturerList = FXCollections.observableArrayList();
    private final ObservableList<Tank> tankList = FXCollections.observableArrayList();

    private Manufacturer selectedManufacturer = null;
    private Tank selectedTank = null;

    @FXML void initialize() {
        refreshTables(null);
        manufacturerTabableView.setItems(manufacturerList);

        manufacturerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        manufacturerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        manufacturerCountryColumn.setCellValueFactory(
                cell -> {
                    Manufacturer manufacturer = (Manufacturer) cell.getValue();
                    return new SimpleStringProperty(manufacturer.getCountry().getName());
                }
        );

        tankTabableView.setItems(tankList);
        tankIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tankNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tankProductionColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfProduction"));
        tankManufacturerNameColumn.setCellValueFactory(
                cell -> {
                    Tank tank = (Tank) cell.getValue();
                    return new SimpleStringProperty(tank.getManufacturer().getName());
                }
        );

        ObservableList<Integer> pageSizeComboBoxItems = FXCollections.observableArrayList(10, 25, 50, 100);
        pageSizeComboBox.setItems(pageSizeComboBoxItems);
        pageSizeComboBox.setValue(pageSizeComboBoxItems.getFirst());
        pageSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.manufacturerPageable.pageSize = newValue;
            Platform.runLater(() -> {
                this.refreshTables(null);
            });
        });

        ObservableList<Integer> pageSizeComboBoxChildrenItems = FXCollections.observableArrayList(10, 25, 50, 100);
        pageSizeComboBoxChildren.setItems(pageSizeComboBoxChildrenItems);
        pageSizeComboBoxChildren.setValue(pageSizeComboBoxChildrenItems.getFirst());
        pageSizeComboBoxChildren.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.tankPageable.pageSize = newValue;
            Platform.runLater(this::refreshTankTable);
        });
    }

    @FXML public void populateTanks(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            selectedManufacturer = manufacturerTabableView.getSelectionModel().getSelectedItem();
            System.out.println("Selected Manufacturer: " + selectedManufacturer);
            this.tankPageable.page = 0;
            this.refreshTankTable();
        }
        if (selectedManufacturer != null) {
            tankList.clear();
            this.refreshTankTable();
        }
    }

    @FXML public void refreshTables(ActionEvent actionEvent) {
        manufacturerTabableView.getSelectionModel().clearSelection();
        selectedManufacturer = null;

        this.resetSelectedTank();

        manufacturerList.clear();
        tankList.clear();

        Page<Manufacturer> manufacturerPage = manufacturerService.getPage(manufacturerPageable.page,  manufacturerPageable.pageSize);
        manufacturerList.addAll(manufacturerPage.getContent());
        manufacturerPageable.hasNext = manufacturerPage.hasNext();
        manufacturerPageable.hasPrevious = manufacturerPage.hasPrevious();

        this.pageCountLabel.setText(manufacturerPageable.page + 1 + "/" + manufacturerPage.getTotalPages());
        this.searchTextField.clear();
    }

    @FXML public void fillTankTextFields(MouseEvent mouseEvent) {
        this.selectedTank = (Tank) tankTabableView.getSelectionModel().getSelectedItem();
        if (this.selectedTank != null) {
            updateTankButton.setDisable(false);
            removeTankButton.setDisable(false);
            tankNameTextField.setText(this.selectedTank.getName());
            tankProductionTextField.setText(String.valueOf(this.selectedTank.getYearOfProduction()));
            tankManufacturerNameTextField.setText(this.selectedTank.getManufacturer().getName());
        }
    }

    @FXML public void updateTank(ActionEvent actionEvent) {
        if (selectedTank != null) {
            manufacturerService.findByName(tankManufacturerNameTextField.getText()).ifPresentOrElse(
                    manufacturer -> {
                        try {
                            selectedTank.setManufacturer(manufacturer);
                            selectedTank.setName(tankNameTextField.getText());
                            selectedTank.setYearOfProduction(Integer.parseInt(tankProductionTextField.getText()));
                            tankService.update(selectedTank);
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                Main.showError(e.getMessage());
                            });
                        }
                    },
                    () -> Platform.runLater(() -> {
                        Main.showError("Invalid Manufacturer Name!");
                    })
            );
        }
    }

    @FXML public void removeTank(ActionEvent actionEvent) {
        if (selectedTank != null) {
            tankService.delete(selectedTank.getId());
            Platform.runLater(() -> Main.showInfo("Tank Deleted Successfully!"));
            selectedTank = null;
        }
    }

    private void resetSelectedTank() {
        tankTabableView.getSelectionModel().clearSelection();
        selectedTank = null;
        updateTankButton.setDisable(true);
        removeTankButton.setDisable(true);
        tankNameTextField.clear();
        tankProductionTextField.clear();
        tankManufacturerNameTextField.clear();
    }

    private void refreshTankTable() {
        this.resetSelectedTank();
        tankList.clear();

        Page<Tank> tankPage = tankService.findByNameAndManufacturer(
                searchTextField.getText(),
                selectedManufacturer,
                this.tankPageable.page,
                this.tankPageable.pageSize
        );

        tankList.addAll(tankPage.getContent());
        tankPageable.hasNext = tankPage.hasNext();
        tankPageable.hasPrevious = tankPage.hasPrevious();
        this.pageCountLabelChildren.setText(this.tankPageable.page + 1 + "/" + tankPage.getTotalPages());
    }

    @FXML public void addTank(ActionEvent actionEvent) {
        manufacturerService.findByName(tankManufacturerNameTextField.getText()).ifPresentOrElse(
                manufacturer -> {
                    try {
                        tankService.save(tankNameTextField.getText(), tankProductionTextField.getText(), manufacturer);
                    } catch (Exception e) {
                        Platform.runLater(() -> {Main.showError(e.getMessage());});
                    }
                },
                () -> Platform.runLater(() -> Main.showError("Manufacturer couldn't be found"))
        );
    }

    @FXML public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (selectedManufacturer == null) {
                Main.showError("No Manufacturer Selected");
            } else {
                this.resetSelectedTank();
                tankList.clear();
                tankList.addAll(tankService.findByNameAndManufacturer(searchTextField.getText(), selectedManufacturer, tankPageable.page, tankPageable.pageSize).getContent());
            }
        }
    }

    @FXML public void prevPage(ActionEvent actionEvent) {
        if (manufacturerPageable.hasPrevious) {
            manufacturerPageable.page--;
            this.refreshTables.fire();
        }
    }

    @FXML public void nextPage(ActionEvent actionEvent) {
        if (manufacturerPageable.hasNext) {
            manufacturerPageable.page++;
            this.refreshTables.fire();
        }
    }

    @FXML public void nextPageChildren(ActionEvent actionEvent) {
        if (tankPageable.hasNext) {
            tankPageable.page++;
            this.refreshTankTable();
        }
    }

    @FXML public void prevPageChildren(ActionEvent actionEvent) {
        if (tankPageable.hasPrevious) {
            tankPageable.page--;
            this.refreshTankTable();
        }
    }
}