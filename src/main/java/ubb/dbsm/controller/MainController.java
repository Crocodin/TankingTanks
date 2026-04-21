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
import ubb.dbsm.Main;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.exceptions.DatabaseError;
import ubb.dbsm.service.model.ManufacturerService;
import ubb.dbsm.service.model.TankService;
import ubb.dbsm.utils.paging.Page;
import ubb.dbsm.utils.paging.Pageable;


public class MainController {
    @FXML public Label pageCountLabel;
    @FXML private TableView<Manufacturer> manufacturerTabableView;
    @FXML private TableColumn<Manufacturer, Integer> manufacturerIdColumn;
    @FXML private TableColumn<Manufacturer, String> manufacturerNameColumn, manufacturerCountryColumn;

    @FXML public ComboBox<Integer> pageSizeComboBox;
    private final Pageable currentPageable = new Pageable(1, 10);

    @FXML private TableView<Tank> tankTabableView;
    @FXML private TableColumn<Tank, Integer> tankIdColumn, tankProductionColumn;
    @FXML private TableColumn<Tank, String> tankNameColumn, tankManufacturerNameColumn;

    @FXML private Button addTankButton, updateTankButton, removeTankButton, refreshTables;
    @FXML private TextField tankNameTextField, tankManufacturerNameTextField, tankProductionTextField, searchTextField;

    private final TankService tankService;
    private final ManufacturerService manufacturerService;

    private long totalManufacturers;

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
            this.currentPageable.setPageSize(newValue);
            Platform.runLater(() -> {
                this.refreshTables(null);
            });
        });
    }

    @FXML public void populateTanks(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            selectedManufacturer = manufacturerTabableView.getSelectionModel().getSelectedItem();
            this.resetSelectedTank();
        }
        if (selectedManufacturer != null) {
            tankList.clear();
            tankList.addAll(tankService.findByManufacturer(selectedManufacturer));
        }
    }

    @FXML public void refreshTables(ActionEvent actionEvent) {
        manufacturerTabableView.getSelectionModel().clearSelection();
        selectedManufacturer = null;

        this.resetSelectedTank();

        manufacturerList.clear();
        tankList.clear();

        Page<Manufacturer> manufacturerPage = manufacturerService.getPage(this.currentPageable);
        this.totalManufacturers = manufacturerPage.getTotalItems();
        manufacturerList.addAll(manufacturerPage.getItemsOnPage());

        long comp = manufacturerPage.getPageable().getPageSize();
        if (this.totalManufacturers % comp == 0) {
            this.pageCountLabel.setText(manufacturerPage.getPageable().getPageNumber() + "/" + (this.totalManufacturers / comp));
        }
        else this.pageCountLabel.setText(manufacturerPage.getPageable().getPageNumber() + "/" + (this.totalManufacturers / comp + 1));
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
            selectedTank.setName(tankNameTextField.getText());
            selectedTank.setYearOfProduction(Integer.parseInt(tankProductionTextField.getText()));
            manufacturerService.findByName(tankManufacturerNameTextField.getText()).ifPresentOrElse(
                    manufacturer -> this.selectedTank.setManufacturer(manufacturer),
                    () -> Main.showError("Invalid Manufacturer: Couldn't find the Manufacturer " + tankManufacturerNameTextField.getText())
            );
            try {
                if(tankService.update(selectedTank).isEmpty()) {
                    Main.showError("Invalid tank information");
                } else {
                    this.refersTankTable();
                    Main.showInfo("Tank Updated");
                }
            } catch (DatabaseError error) {
                Main.showError(error.getMessage());
            }
        }
    }

    @FXML public void removeTank(ActionEvent actionEvent) {
        if (selectedTank != null) {
            try {
                tankService.delete(selectedTank);
                this.refersTankTable();
                Main.showInfo("Tank Deleted");
            } catch (DatabaseError error) {
                Main.showError(error.getMessage());
            }
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

    private void refersTankTable() {
        this.resetSelectedTank();
        tankList.clear();
        tankList.addAll(tankService.findByNameAndManufacturer(searchTextField.getText(), selectedManufacturer));
    }

    @FXML public void addTank(ActionEvent actionEvent) {
        manufacturerService.findByName(tankManufacturerNameTextField.getText()).ifPresentOrElse(
                manufacturer -> {
                    Tank tank = Tank.builder()
                            .name(tankNameTextField.getText())
                            .yearOfProduction(Integer.parseInt(tankProductionTextField.getText()))
                            .manufacturer(manufacturer).build();
                    try {
                        tankService.save(tank).ifPresentOrElse(
                                t -> {
                                    Main.showInfo("Tank added");
                                    this.refersTankTable();
                                },
                                () -> Main.showError("Invalid tank production date")
                        );
                    } catch (DatabaseError error) {
                        Main.showError(error.getMessage());
                    }
                },
                () -> Main.showError("Invalid Manufacturer: Couldn't find the Manufacturer " + tankManufacturerNameTextField.getText())
        );
    }

    @FXML public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (selectedManufacturer == null) {
                Main.showError("No Manufacturer Selected");
            } else {
                this.resetSelectedTank();
                tankList.clear();
                tankList.addAll(tankService.findByNameAndManufacturer(searchTextField.getText(), selectedManufacturer));
            }
        }
    }

    @FXML public void prevPage(ActionEvent actionEvent) {
        if (this.currentPageable.getPageNumber() <= 1) return;
        this.currentPageable.decrementPageNumber();
        this.refreshTables.fire();
    }

    @FXML public void nextPage(ActionEvent actionEvent) {
        if ((long) this.currentPageable.getPageSize() * this.currentPageable.getPageNumber() >= this.totalManufacturers) return;
        this.currentPageable.incrementPageNumber();
        this.refreshTables.fire();
    }
}
