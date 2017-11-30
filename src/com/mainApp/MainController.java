package com.mainApp;

import com.DBUtils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableHashMap;

public class MainController implements Initializable {

    public ObservableList<BorrowerData> borrowerData;
    public ObservableList<MaterialData> materialData;
    public ObservableMap<String, MaterialData> empruntedMaterial;
    public ObservableList<EmpruntData> empruntData;
    public ObservableList<EmpruntData> deletedEmpruntData;
    public Map<String, String> borrowersFields;
    public Map<String, String> materialsFields;
    public DropShadow errorShadow;

    @FXML
    public TabPane tabPane;

    @FXML
    public Tab empruntTab;

    @FXML
    public Tab ongletEmprunt;

    @FXML
    public ComboBox<borrowersFilterBy> comboBoxFilterBy;

    @FXML
    public ComboBox<job> workCombox;

    @FXML
    public Button searchBtn;

    @FXML
    public TextField searchValue;

    @FXML
    public TableView<BorrowerData> tableBorrowers;

    @FXML
    public TableColumn<BorrowerData, String> idColumn;

    @FXML
    public TableColumn<BorrowerData, String> fnameColumn;

    @FXML
    public TableColumn<BorrowerData, String> lnameColumn;

    @FXML
    public TableColumn<BorrowerData, String> workColumn;

    @FXML
    public TableColumn<BorrowerData, Integer> qtColumn;

    @FXML
    public TextField textID;

    @FXML
    public TextField textFName;

    @FXML
    public TextField textLName;

    @FXML
    public TextField textEmail;

    @FXML
    public TextField textTel;

    @FXML
    public ComboBox<job> textWork;

    @FXML
    public Button addBtn;

    @FXML
    public Button editBtn;

    @FXML
    public Button deleteBtn;

    @FXML
    public TableView<MaterialData> tabMaterials;

    @FXML
    public TableColumn<MaterialData, String> matIDColumn;

    @FXML
    public TableColumn<MaterialData, String> matNameColumn;

    @FXML
    public TableColumn<MaterialData, String> matTypeColumn;

    @FXML
    public TableColumn<MaterialData, String> matReferenceColumn;

    @FXML
    public TableColumn<MaterialData, Integer> matQtColumn;

    /************************************************************************************************************/

    @FXML
    public ComboBox<materialsFilterBy> matComboboxFilterBy;

    @FXML
    public ComboBox<materialTypes> matTypeCombobox;

    @FXML
    public TextField matSearchValue;

    @FXML
    public TableView<MaterialData> tableMaterials;

    @FXML
    public TableColumn<MaterialData, String> materialIDColumn;

    @FXML
    public TableColumn<MaterialData, String> materialNameColumn;

    @FXML
    public TableColumn<MaterialData, String> materialTypeColumn;

    @FXML
    public TableColumn<MaterialData, Integer> materialInitQtColumn;

    @FXML
    public TableColumn<MaterialData, Integer> materialAvQtColumn;

    @FXML
    public TextField textMatID;

    @FXML
    public TextField textMatName;

    @FXML
    public Spinner<Integer> textMatQt;

    @FXML
    public TextArea textMatDescription;

    @FXML
    public ComboBox<materialTypes> textMatType;

    @FXML
    public Button matSearchBtn;

    @FXML
    public TableView<BorrowerData> tabBorrowers;

    @FXML
    public TableColumn<BorrowerData, String> berrIDColumn;

    @FXML
    public TableColumn<BorrowerData, String> berrFnameColumn;

    @FXML
    public TableColumn<BorrowerData, String> berrLnameColumn;

    @FXML
    public TableColumn<BorrowerData, String> berrWorkColumn;

    @FXML
    public TableColumn<BorrowerData, String> berrReferenceColumn;

    @FXML
    public TableColumn<BorrowerData, Integer> berrQtColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.empruntData = observableArrayList();
        errorShadow = new DropShadow();
        errorShadow.setWidth(21);
        errorShadow.setHeight(21);
        errorShadow.setRadius(10);
        errorShadow.setSpread(0);
        errorShadow.setColor(Color.rgb(250, 0, 0));
        /************************************************************************************************************/
        borrowersFields = new HashMap<>();
        borrowersFields.put(borrowersFilterBy.Identifiant.value(), "ID");
        borrowersFields.put(borrowersFilterBy.Nom.value(), "firstName");
        borrowersFields.put(borrowersFilterBy.Prénom.value(), "lastName");
        borrowersFields.put(borrowersFilterBy.Email.value(), "email");
        borrowersFields.put(borrowersFilterBy.Téléphone.value(), "phoneNumber");
        borrowersFields.put(borrowersFilterBy.Work.value(), "work");
        borrowersFields.put(borrowersFilterBy.Tous.value(), "all");
        this.comboBoxFilterBy.setItems(observableArrayList(borrowersFilterBy.values()));
        this.comboBoxFilterBy.setValue(borrowersFilterBy.Tous);
        this.workCombox.setItems(observableArrayList(job.values()));
        this.textWork.setItems(observableArrayList(job.values()));
        this.tableBorrowers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            BorrowerData borrowerData = this.tableBorrowers.getSelectionModel().getSelectedItem();
            if (borrowerData != null) {
                this.textID.setText(borrowerData.getID());
                this.textFName.setText(borrowerData.getFirstName());
                this.textLName.setText(borrowerData.getLastName());
                this.textEmail.setText(borrowerData.getEmail());
                this.textTel.setText(borrowerData.getPhoneNumber());
                this.textWork.setValue(job.valueOf(borrowerData.getWork()));
                this.materialData = getBorrowedMaterial();
                this.matIDColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("ID"));
                this.matNameColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("name"));
                this.matTypeColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("type"));
                this.matReferenceColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("reference"));
                this.matQtColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, Integer>("availableQuantity"));
                this.tabMaterials.setItems(null);
                this.tabMaterials.setItems(this.materialData);
            }
        });
        this.searchBtn.fire();
        /************************************************************************************************************/
        materialsFields = new HashMap<>();
        materialsFields.put(materialsFilterBy.Identifiant.value(), "ID");
        materialsFields.put(materialsFilterBy.Nom.value(), "name");
        materialsFields.put(materialsFilterBy.Type.value(), "type");
        materialsFields.put(materialsFilterBy.Tous.value(), "all");
        this.matComboboxFilterBy.setItems(observableArrayList(materialsFilterBy.values()));
        this.matComboboxFilterBy.setValue(materialsFilterBy.Tous);
        this.matTypeCombobox.setItems(observableArrayList(materialTypes.values()));
        this.textMatType.setItems(observableArrayList(materialTypes.values()));
        this.textMatQt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
        this.textMatQt.setEditable(true);
        this.tableMaterials.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            MaterialData materialData = this.tableMaterials.getSelectionModel().getSelectedItem();
            if (materialData != null) {
                this.textMatID.setText(materialData.getID());
                this.textMatName.setText(materialData.getName());
                this.textMatQt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE));
                this.textMatQt.increment(materialData.getInitQuantity() - 1);
                this.textMatDescription.setText(materialData.getDescription());
                this.textMatType.setValue(materialTypes.valueOf(materialData.getType()));
                this.borrowerData = getMaterialBorrowers();
                this.berrIDColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("ID"));
                this.berrFnameColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("firstName"));
                this.berrLnameColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("lastName"));
                this.berrWorkColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("work"));
                this.berrReferenceColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("reference"));
                this.berrQtColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, Integer>("totalBorrowedMaterial"));
                this.tabBorrowers.setItems(null);
                this.tabBorrowers.setItems(this.borrowerData);
            }
        });
        this.matSearchBtn.fire();
        /************************************************************************************************************/
        this.empMatComboboxFilterBy.setItems(observableArrayList(materialsFilterBy.values()));
        this.empMatComboboxFilterBy.setValue(materialsFilterBy.Tous);
        this.empMatComboboxType.setItems(observableArrayList(materialTypes.values()));
        /************************************************************************************************************/
        this.empruntTab.setDisable(true);
        this.empruntedMaterial = observableHashMap();
        this.deletedEmpruntData = observableArrayList();

        this.emprunTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println(this.emprunTable.getSelectionModel().getSelectedItem());
        });
    }

    @FXML
    public void searchBorrowers(ActionEvent actionEvent) {
        if (this.comboBoxFilterBy.getValue() == null || this.comboBoxFilterBy.getValue().value().equals(borrowersFilterBy.Tous.value())) {
            this.borrowerData = getBorrowersByData("", "");
        } else if (this.comboBoxFilterBy.getValue().value().equals(borrowersFilterBy.Work.value()) && this.workCombox.getValue() != null) {
            this.borrowerData = getBorrowersByData(this.borrowersFields.get(this.comboBoxFilterBy.getValue().value()), this.workCombox.getValue().value());
        } else {
            this.borrowerData = getBorrowersByData(this.borrowersFields.get(this.comboBoxFilterBy.getValue().value()), this.searchValue.getText());
        }
        this.idColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("ID"));
        this.fnameColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("firstName"));
        this.lnameColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("lastName"));
        this.workColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, String>("work"));
        this.qtColumn.setCellValueFactory(new PropertyValueFactory<BorrowerData, Integer>("totalBorrowedMaterial"));
        this.tableBorrowers.setItems(null);
        this.tableBorrowers.setItems(this.borrowerData);
    }

    @FXML
    public void onFilterChanged(ActionEvent actionEvent) {
        if (this.comboBoxFilterBy.getValue() != null && this.comboBoxFilterBy.getValue().value().equals(borrowersFilterBy.Work.value())) {
            this.searchValue.setVisible(false);
            this.workCombox.setVisible(true);
        } else {
            this.searchValue.setVisible(true);
            this.workCombox.setVisible(false);
            if (this.comboBoxFilterBy.getValue() != null && this.comboBoxFilterBy.getValue().value().equals(borrowersFilterBy.Tous.value())) {
                this.searchValue.setText("");
            }
        }
        if (this.matComboboxFilterBy.getValue() != null && this.matComboboxFilterBy.getValue().value().equals(materialsFilterBy.Type.value())) {
            this.matSearchValue.setVisible(false);
            this.matTypeCombobox.setVisible(true);
        } else {
            this.matSearchValue.setVisible(true);
            this.matTypeCombobox.setVisible(false);
            if (this.matComboboxFilterBy.getValue() != null && this.matComboboxFilterBy.getValue().value().equals(materialsFilterBy.Tous.value())) {
                this.matSearchValue.setText("");
            }
        }
        if (this.empMatComboboxFilterBy.getValue() != null && this.empMatComboboxFilterBy.getValue().value().equals(materialsFilterBy.Type.value())) {
            this.empMatSearchValue.setVisible(false);
            this.empMatComboboxType.setVisible(true);
        } else {
            this.empMatSearchValue.setVisible(true);
            this.empMatComboboxType.setVisible(false);
            if (this.empMatComboboxFilterBy.getValue() != null && this.empMatComboboxFilterBy.getValue().value().equals(materialsFilterBy.Tous.value())) {
                this.empMatSearchValue.setText("");
            }
        }
    }

    @FXML
    public void exit(ActionEvent actionEvent) throws Exception {
        System.exit(0);
    }

    @FXML
    public void addBorrower(ActionEvent actionEvent) {
        String query = "INSERT INTO borrowers(ID,firstName,lastName,email,phoneNumber,work) VALUES (?,?,?,?,?,?)";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int i = 0;
            if (this.textID.getText().equals("")) {
                this.textID.setEffect(errorShadow);
            } else {
                this.textID.setEffect(null);
                preparedStatement.setString(1, this.textID.getText());
                i++;
            }
            if (this.textFName.getText().equals("")) {
                this.textFName.setEffect(errorShadow);
            } else {
                this.textFName.setEffect(null);
                preparedStatement.setString(2, this.textFName.getText());
                i++;
            }
            if (this.textLName.getText().equals("")) {
                this.textLName.setEffect(errorShadow);
            } else {
                this.textLName.setEffect(null);
                preparedStatement.setString(3, this.textLName.getText());
                i++;
            }
            if (this.textEmail.getText().equals("")) {
                this.textEmail.setEffect(errorShadow);
            } else {
                this.textEmail.setEffect(null);
                preparedStatement.setString(4, this.textEmail.getText());
                i++;
            }
            preparedStatement.setString(5, this.textTel.getText());
            if (this.textWork.getValue() == null) {
                this.textWork.setEffect(errorShadow);
            } else {
                this.textWork.setEffect(null);
                preparedStatement.setString(6, this.textWork.getValue().value());
                i++;
            }
            if (i == 5) {
                preparedStatement.execute();
                this.comboBoxFilterBy.setValue(borrowersFilterBy.Tous);
                this.searchBtn.fire();
                connection.close();
            }
        } catch (SQLException e) {
            this.textID.setEffect(errorShadow);
        }
    }

    @FXML
    public void deleteBorrower(ActionEvent actionEvent) {
        String query = "DELETE FROM borrowers WHERE ID = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (!this.textID.getText().equals("")) {
                preparedStatement.setString(1, this.textID.getText());
                preparedStatement.execute();
                this.searchBtn.fire();
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateBorrower(ActionEvent actionEvent) {
        String query = "UPDATE borrowers SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?,work = ? WHERE ID = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int i = 0;
            if (this.textID.getText().equals("")) {
                this.textID.setEffect(errorShadow);
            } else {
                this.textID.setEffect(null);
                preparedStatement.setString(6, this.textID.getText());
                i++;
            }
            if (this.textFName.getText().equals("")) {
                this.textFName.setEffect(errorShadow);
            } else {
                this.textFName.setEffect(null);
                preparedStatement.setString(1, this.textFName.getText());
                i++;
            }
            if (this.textLName.getText().equals("")) {
                this.textLName.setEffect(errorShadow);
            } else {
                this.textLName.setEffect(null);
                preparedStatement.setString(2, this.textLName.getText());
                i++;
            }
            if (this.textEmail.getText().equals("")) {
                this.textEmail.setEffect(errorShadow);
            } else {
                this.textEmail.setEffect(null);
                preparedStatement.setString(3, this.textEmail.getText());
                i++;
            }
            preparedStatement.setString(4, this.textTel.getText());
            if (this.textWork.getValue() == null) {
                this.textWork.setEffect(errorShadow);
            } else {
                this.textWork.setEffect(null);
                preparedStatement.setString(5, this.textWork.getValue().value());
                i++;
            }
            if (i == 5) {
                preparedStatement.execute();
                this.searchBtn.fire();
                connection.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<MaterialData> getBorrowedMaterial() {
        ObservableList<MaterialData> materialData = observableArrayList();
        String query = "SELECT materialID,reference,quantity FROM emprunt WHERE borrowerID = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, this.textID.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String query2 = "SELECT name,type FROM materials WHERE ID = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {
                    materialData.add(new MaterialData(resultSet.getString(1),
                            resultSet1.getString(1),
                            Integer.valueOf(resultSet.getString(3)),
                            Integer.valueOf(resultSet.getString(3)),
                            "-",
                            resultSet.getString(2),
                            resultSet1.getString(2)));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materialData;
    }

    public ObservableList<BorrowerData> getBorrowersByData(String field, String value) {
        ObservableList<BorrowerData> borrowerData = FXCollections.observableArrayList();
        String query;
        try {
            if (value.equals("")) {
                query = "SELECT * FROM borrowers";
            } else {
                query = "SELECT * FROM borrowers WHERE " + field + " = '" + value + "'";
            }
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                borrowerData.add(new BorrowerData(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getInt(7),
                        "-"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowerData;
    }

    @FXML
    public void searchMaterials(ActionEvent actionEvent) {
        if (this.matComboboxFilterBy.getValue() == null || this.matComboboxFilterBy.getValue().value().equals(materialsFilterBy.Tous.value())) {
            this.materialData = getMaterialsByData("", "");
        } else if (this.matComboboxFilterBy.getValue().value().equals(materialsFilterBy.Type.value()) && this.matTypeCombobox.getValue() != null) {
            this.materialData = getMaterialsByData(this.materialsFields.get(this.matComboboxFilterBy.getValue().value()), this.matTypeCombobox.getValue().value());
        } else {
            this.materialData = getMaterialsByData(this.materialsFields.get(this.matComboboxFilterBy.getValue().value()), this.matSearchValue.getText());
        }
        this.materialIDColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("ID"));
        this.materialNameColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("name"));
        this.materialTypeColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("type"));
        this.materialInitQtColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, Integer>("initQuantity"));
        this.materialAvQtColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, Integer>("availableQuantity"));
        this.tableMaterials.setItems(null);
        this.tableMaterials.setItems(this.materialData);
    }

    public ObservableList<MaterialData> getMaterialsByData(String field, String value) {
        ObservableList<MaterialData> materialData = FXCollections.observableArrayList();
        String query;
        try {
            if (value.equals("")) {
                query = "SELECT * FROM materials";
            } else {
                query = "SELECT * FROM materials WHERE " + field + " = '" + value + "'";
            }
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                materialData.add(new MaterialData(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getString(5),
                        "-",
                        resultSet.getString(6)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materialData;
    }

    @FXML
    public void addMaterial(ActionEvent actionEvent) {
        String query = "INSERT INTO materials(ID,name,initQuantity,availableQuantity,description,type) VALUES (?,?,?,?,?,?)";

        try {
            Connection connection = DBConnection.getConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int i = 0;
            if (this.textMatID.getText().equals("")) {
                this.textMatID.setEffect(errorShadow);
            } else {
                this.textMatID.setEffect(null);
                preparedStatement.setString(1, this.textMatID.getText());
                i++;
            }
            if (this.textMatName.getText().equals("")) {
                this.textMatName.setEffect(errorShadow);
            } else {
                this.textMatName.setEffect(null);
                preparedStatement.setString(2, this.textMatName.getText());
                i++;
            }
            this.textMatQt.setEffect(null);
            preparedStatement.setInt(3, Integer.valueOf(this.textMatQt.getValue()));
            preparedStatement.setInt(4, Integer.valueOf(this.textMatQt.getValue()));
            preparedStatement.setString(5, this.textMatDescription.getText());
            if (this.textMatType.getValue() == null) {
                this.textMatType.setEffect(errorShadow);
            } else {
                this.textMatType.setEffect(null);
                preparedStatement.setString(6, this.textMatType.getValue().value());
                i++;
            }
            if (i == 3) {
                preparedStatement.execute();
                this.matComboboxFilterBy.setValue(materialsFilterBy.Tous);
                this.matSearchBtn.fire();
                connection.close();
            }
        } catch (SQLException e) {
            this.textMatID.setEffect(errorShadow);
        }
    }

    @FXML
    public void updateMaterial(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            String query = "SELECT initQuantity,availableQuantity FROM materials WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, this.textMatID.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int initQuantity = resultSet.getInt(1);
                int availableQuantity = resultSet.getInt(2);
                query = "UPDATE materials SET name = ?, initQuantity = ?, availableQuantity = ?, description = ?, type = ? WHERE ID = ?";
                preparedStatement = connection.prepareStatement(query);
                int i = 0;
                if (this.textMatName.getText().equals("")) {
                    this.textMatName.setEffect(errorShadow);
                } else {
                    this.textMatName.setEffect(null);
                    preparedStatement.setString(1, this.textMatName.getText());
                    i++;
                }
                preparedStatement.setInt(2, this.textMatQt.getValue());
                preparedStatement.setInt(3, this.textMatQt.getValue() - initQuantity + availableQuantity);

                preparedStatement.setString(4, this.textMatDescription.getText());
                if (this.textMatType.getValue() == null) {
                    this.textMatType.setEffect(errorShadow);
                } else {
                    this.textMatType.setEffect(null);
                    preparedStatement.setString(5, this.textMatType.getValue().value());
                    i++;
                }
                if (this.textMatID.getText().equals("")) {
                    this.textMatID.setEffect(errorShadow);
                } else {
                    this.textMatID.setEffect(null);
                    preparedStatement.setString(6, this.textMatID.getText());
                    i++;
                }
                if (i == 3) {
                    preparedStatement.execute();
                    this.matSearchBtn.fire();
                    connection.close();
                }
            }
        }
    }

    @FXML
    public void deleteMaterial(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            String query = "DELETE FROM materials WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (!this.textMatID.getText().equals("")) {
                preparedStatement.setString(1, this.textMatID.getText());
                preparedStatement.execute();
                this.matSearchBtn.fire();
                connection.close();
            }
        }
    }

    public ObservableList<BorrowerData> getMaterialBorrowers() {
        ObservableList<BorrowerData> borrowerData = observableArrayList();
        String query = "SELECT borrowerID,reference,quantity FROM emprunt WHERE materialID = ?";

        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, this.textMatID.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String query2 = "SELECT firstName,lastName,work FROM borrowers WHERE ID = ?";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
                preparedStatement1.setString(1, resultSet.getString(1));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {
                    borrowerData.add(new BorrowerData(resultSet.getString(1),
                            resultSet1.getString(1),
                            resultSet1.getString(2),
                            "-@-",
                            "-",
                            resultSet1.getString(3),
                            resultSet.getInt(3),
                            resultSet.getString(2)));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowerData;
    }

    @FXML
    public Label empLabelID;

    @FXML
    public Label empLabelFirstName;

    @FXML
    public Label empLabelEmail;

    @FXML
    public Label empLabelWork;

    @FXML
    public Label empLabelLastName;

    @FXML
    public Label empLabelTel;

    @FXML
    public void switchToEmprunt(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnection.getConnection();
        this.deletedEmpruntData = observableArrayList();
        this.empruntData = observableArrayList();
        this.empruntedMaterial = observableHashMap();
        BorrowerData borrowerData = this.tableBorrowers.getSelectionModel().getSelectedItem();
        if (borrowerData != null) {
            this.empLabelID.setText(borrowerData.getID());
            this.empLabelFirstName.setText(borrowerData.getFirstName());
            this.empLabelLastName.setText(borrowerData.getLastName());
            this.empLabelEmail.setText(borrowerData.getEmail());
            this.empLabelTel.setText(borrowerData.getPhoneNumber());
            this.empLabelWork.setText(borrowerData.getWork());
            if (connection != null) {
                String query = "SELECT * FROM emprunt WHERE borrowerID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, this.textID.getText());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    this.empruntData.add(new EmpruntData(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7),
                            resultSet.getString(8),
                            resultSet.getInt(9)
                    ));
                    String query2 = "SELECT * FROM materials WHERE ID = ?";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.setString(1, resultSet.getString(3));
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    MaterialData materialData = new MaterialData(resultSet2.getString(1),
                            resultSet2.getString(2),
                            resultSet2.getInt(3),
                            resultSet2.getInt(4),
                            resultSet2.getString(5),
                            "",
                            resultSet2.getString(6));
                    this.empruntedMaterial.put(materialData.getID(), materialData);
                }
                this.empruntNameColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("matName"));
                this.empruntDEColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("dateEmprunt"));
                this.empruntDRColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("dateReturn"));
                this.empruntRefColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("reference"));
                this.empruntQtColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, Integer>("quantity"));
                this.emprunTable.setItems(null);
                this.emprunTable.setItems(this.empruntData);
                this.tabPane.getSelectionModel().select(ongletEmprunt);
                connection.close();
            }
        }
    }

    @FXML
    public ComboBox<materialTypes> empMatComboboxType;

    @FXML
    public TextField empMatSearchValue;

    @FXML
    public ComboBox<materialsFilterBy> empMatComboboxFilterBy;

    @FXML
    public Button empMatSearchBtn;

    @FXML
    public TableView<MaterialData> tabEmpMat;

    @FXML
    public TableColumn<MaterialData, String> empMatIDColumn;

    @FXML
    public TableColumn<MaterialData, String> empMatNameColumn;

    @FXML
    public TableColumn<MaterialData, String> empMatTypeColumn;

    @FXML
    public TableColumn<MaterialData, Integer> empMatQtColumn;

    @FXML
    public void searchEmpMaterials(ActionEvent actionEvent) {
        if (this.empMatComboboxFilterBy.getValue() == null || this.empMatComboboxFilterBy.getValue().value().equals(materialsFilterBy.Tous.value())) {
            this.materialData = getMaterialsByData("", "");
        } else if (this.empMatComboboxFilterBy.getValue().value().equals(materialsFilterBy.Type.value()) && this.empMatComboboxType.getValue() != null) {
            this.materialData = getMaterialsByData(this.materialsFields.get(this.empMatComboboxFilterBy.getValue().value()), this.empMatComboboxType.getValue().value());
        } else {
            this.materialData = getMaterialsByData(this.materialsFields.get(this.empMatComboboxFilterBy.getValue().value()), this.empMatSearchValue.getText());
        }
        this.empMatIDColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("ID"));
        this.empMatNameColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("name"));
        this.empMatTypeColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, String>("type"));
        this.empMatQtColumn.setCellValueFactory(new PropertyValueFactory<MaterialData, Integer>("availableQuantity"));
        this.tabEmpMat.setItems(null);
        this.tabEmpMat.setItems(this.materialData);
    }

    @FXML
    public TableView<EmpruntData> emprunTable;

    @FXML
    public TableColumn<EmpruntData, String> empruntNameColumn;

    @FXML
    public TableColumn<EmpruntData, String> empruntDEColumn;

    @FXML
    public TableColumn<EmpruntData, String> empruntDRColumn;

    @FXML
    public TableColumn<EmpruntData, String> empruntRefColumn;

    @FXML
    public TableColumn<EmpruntData, Integer> empruntQtColumn;
    public int initBorrowedQt;

    @FXML
    public void switchToDoEmprunt(ActionEvent actionEvent) throws IOException {
        MaterialData materialData = this.tabEmpMat.getSelectionModel().getSelectedItem();
        initBorrowedQt = 0;
        if (materialData != null) {
            EmpruntData empruntData = getEmpruntData(materialData);
            if (empruntData != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Voulez-vous modifier cet emprunt ?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    this.textEmpMatID.setText(materialData.getID());
                    this.textEmpMatName.setText(materialData.getName());
                    this.textEmpMatQt.setText(materialData.getAvailableQuantity() + "");
                    this.textEmpMatType.setText(materialData.getType());
                    this.textDEToEmprunt.setValue(LocalDate.parse(empruntData.getDateEmprunt()));
                    this.textDRToEmprunt.setValue(LocalDate.parse(empruntData.getDateReturn()));
                    this.textQtToEmprunt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, materialData.getAvailableQuantity()));
                    this.textQtToEmprunt.increment(empruntData.getQuantity() - 1);
                    initBorrowedQt = empruntData.getQuantity();
                    this.empruntTab.setDisable(false);
                    this.tabPane.getSelectionModel().select(empruntTab);
                }
            } else {
                this.empruntedMaterial.put(materialData.getID(), materialData);
                this.textEmpMatID.setText(materialData.getID());
                this.textEmpMatName.setText(materialData.getName());
                this.textEmpMatQt.setText(materialData.getAvailableQuantity() + "");
                this.textEmpMatType.setText(materialData.getType());
                this.textDEToEmprunt.setValue(LocalDate.now());
                this.textDRToEmprunt.setValue(LocalDate.now().plusMonths(1));
                this.textQtToEmprunt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, materialData.getAvailableQuantity()));
                this.empruntTab.setDisable(false);
                this.tabPane.getSelectionModel().select(empruntTab);
            }
        }
    }

    @FXML
    public Label textEmpMatID;

    @FXML
    public Label textEmpMatQt;

    @FXML
    public Label textEmpMatName;

    @FXML
    public Label textEmpMatType;

    @FXML
    public DatePicker textDEToEmprunt;

    @FXML
    public DatePicker textDRToEmprunt;

    @FXML
    public Spinner<Integer> textQtToEmprunt;

    @FXML
    public TextField textRefToEmprunt;

    @FXML
    public Button cancelEmpBtn;


    @FXML
    public void okEmprunt(ActionEvent actionEvent) throws SQLException, IOException {
        MaterialData materialData = this.tabEmpMat.getSelectionModel().getSelectedItem();
        BorrowerData borrowerData = this.tableBorrowers.getSelectionModel().getSelectedItem();
        String materialID = null;
        if (materialData != null) {
            EmpruntData empruntData = getEmpruntData(materialData);
            if (empruntData == null) {
                this.empruntData.add(new EmpruntData(-1,
                        this.empLabelID.getText(),
                        materialData.getID(),
                        materialData.getName(),
                        this.textDEToEmprunt.getValue().toString(),
                        this.textDRToEmprunt.getValue().toString(),
                        materialData.getType(),
                        this.textRefToEmprunt.getText(),
                        this.textQtToEmprunt.getValue()));
            } else {
                empruntData.setDateEmprunt(this.textDEToEmprunt.getValue().toString());
                empruntData.setDateReturn(this.textDRToEmprunt.getValue().toString());
                empruntData.setReference(this.textRefToEmprunt.getText());
                empruntData.setQuantity(this.textQtToEmprunt.getValue());
            }
            materialID = materialData.getID();
        } else {
            this.emprunTable.getSelectionModel().getSelectedItem().setDateEmprunt(this.textDEToEmprunt.getValue().toString());
            this.emprunTable.getSelectionModel().getSelectedItem().setDateReturn(this.textDRToEmprunt.getValue().toString());
            this.emprunTable.getSelectionModel().getSelectedItem().setReference(this.textRefToEmprunt.getText());
            this.emprunTable.getSelectionModel().getSelectedItem().setQuantity(this.textQtToEmprunt.getValue());
        }
        this.empruntNameColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("matName"));
        this.empruntDEColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("dateEmprunt"));
        this.empruntDRColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("dateReturn"));
        this.empruntRefColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, String>("reference"));
        this.empruntQtColumn.setCellValueFactory(new PropertyValueFactory<EmpruntData, Integer>("quantity"));
        if (materialID != null) {
            this.empruntedMaterial.get(materialID).setAvailableQuantity(this.empruntedMaterial.get(materialID).getAvailableQuantity() + initBorrowedQt - this.textQtToEmprunt.getValue());
        }
        this.emprunTable.setItems(null);
        this.emprunTable.setItems(this.empruntData);
        this.tabPane.getSelectionModel().select(ongletEmprunt);
        this.empruntTab.setDisable(true);
    }

    @FXML
    public void cancelEmprunt(ActionEvent actionEvent) {
        this.tabPane.getSelectionModel().select(ongletEmprunt);
        this.empruntTab.setDisable(true);
    }

    public EmpruntData getEmpruntData(MaterialData materialData) {
        for (EmpruntData empruntData : this.empruntData) {
            if (empruntData.getMaterialID().equals(materialData.getID())) {
                return empruntData;
            }
        }
        return null;
    }

    @FXML
    public void doEmprunts(ActionEvent actionEvent) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String updateQuery, insertQuery, deleteQuery;
        PreparedStatement updateStatement, insertStatement, deleteStatement;
        if (connection != null) {
            BorrowerData borrowerData = this.tableBorrowers.getSelectionModel().getSelectedItem();
            int i = 0;
            for (EmpruntData empruntData : this.deletedEmpruntData) {
                deleteQuery = "DELETE FROM emprunt WHERE borrowerID = ? AND materialID =?";
                deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, empruntData.getBorrowerID());
                deleteStatement.setString(2, empruntData.getMaterialID());
                deleteStatement.execute();
                updateQuery = "UPDATE materials SET availableQuantity = ? WHERE ID = ?";
                updateStatement = connection.prepareStatement(updateQuery);
                this.empruntedMaterial.get(empruntData.getMaterialID()).setAvailableQuantity(this.empruntedMaterial.get(empruntData.getMaterialID()).getAvailableQuantity() + empruntData.getQuantity());
                updateStatement.setInt(1,
                        this.empruntedMaterial.get(empruntData.getMaterialID()).getAvailableQuantity());
                updateStatement.setString(2, empruntData.getMaterialID());
                updateStatement.execute();
            }

            for (EmpruntData empruntData : this.empruntData) {
                i += empruntData.getQuantity();
                MaterialData materialData = this.empruntedMaterial.get(empruntData.getMaterialID());
                String query = "SELECT * FROM emprunt WHERE borrowerID = ? AND materialID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, empruntData.getBorrowerID());
                preparedStatement.setString(2, empruntData.getMaterialID());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    updateQuery = "UPDATE emprunt SET dateReturn = ?, reference = ?, quantity = ? WHERE ID = ?";
                    updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, empruntData.getDateReturn());
                    updateStatement.setString(2, empruntData.getReference());
                    updateStatement.setInt(3, empruntData.getQuantity());
                    updateStatement.setInt(4, resultSet.getInt(1));
                    updateStatement.execute();

                    updateQuery = "UPDATE materials SET availableQuantity = ? WHERE ID = ?";
                    updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, materialData.getAvailableQuantity());
                    updateStatement.setString(2, materialData.getID());
                    updateStatement.execute();
                } else {
                    insertQuery = "INSERT INTO emprunt (borrowerID,materialID, matName,dateEmprunt,dateReturn,type,reference,quantity)" +
                            "VALUES (?,?,?,?,?,?,?,?)";
                    insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, borrowerData.getID());
                    insertStatement.setString(2, materialData.getID());
                    insertStatement.setString(3, materialData.getName());
                    insertStatement.setString(4, empruntData.getDateEmprunt());
                    insertStatement.setString(5, empruntData.getDateReturn());
                    insertStatement.setString(6, materialData.getType());
                    insertStatement.setString(7, empruntData.getReference());
                    insertStatement.setInt(8, empruntData.getQuantity());
                    insertStatement.execute();

                    insertQuery = "UPDATE materials SET availableQuantity = ? WHERE ID = ?";
                    insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1, materialData.getAvailableQuantity());
                    insertStatement.setString(2, materialData.getID());
                    insertStatement.execute();
                }
            }

            updateQuery = "UPDATE borrowers SET totalBorrowedMaterial = ? WHERE ID = ?";
            updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, i);
            borrowerData.setTotalBorrowedMaterial(i);
            updateStatement.setString(2, borrowerData.getID());
            updateStatement.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Emprunt enregistré avec succes !", ButtonType.OK);
            alert.show();
            this.searchBtn.fire();
            this.matSearchBtn.fire();
            this.empMatSearchBtn.fire();
            this.tableBorrowers.getSelectionModel().select(borrowerData);
            this.switchToEmpruntBtn.fire();
            connection.close();
        }

    }

    @FXML
    public Button switchToEmpruntBtn;

    @FXML
    public void cancelEmprunts(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Voulez-vous annuler l'emprunt ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            this.empruntedMaterial = observableHashMap();
            this.empruntData = observableArrayList();
            this.emprunTable.setItems(null);
        }
    }

    @FXML
    public void deleteEmprunt(ActionEvent actionEvent) {
        EmpruntData empruntData = this.emprunTable.getSelectionModel().getSelectedItem();
        if (empruntData != null) {
            ButtonType editBtn = new ButtonType("Modifier");
            ButtonType deleteBtn = new ButtonType("Supprimer");
            ButtonType cancelBtn = new ButtonType("Annuler");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Voulez-vous supprimer ou modifier l'emprunt ?"
                    , editBtn, deleteBtn, cancelBtn);
            alert.showAndWait();
            if (alert.getResult() == editBtn) {
                this.textEmpMatID.setText(empruntData.getMaterialID());
                this.textEmpMatName.setText(empruntData.getMatName());
                this.textEmpMatQt.setText(this.empruntedMaterial.get(empruntData.getMaterialID()).getInitQuantity() + "");
                this.textEmpMatType.setText(this.empruntedMaterial.get(empruntData.getMaterialID()).getType());
                this.textDEToEmprunt.setValue(LocalDate.parse(empruntData.getDateEmprunt()));
                this.textDRToEmprunt.setValue(LocalDate.parse(empruntData.getDateReturn()));
                this.textQtToEmprunt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, this.empruntedMaterial.get(empruntData.getMaterialID()).getAvailableQuantity()));
                initBorrowedQt = empruntData.getQuantity();
                this.textQtToEmprunt.increment(empruntData.getQuantity() - 1);
                this.empruntTab.setDisable(false);
                this.tabPane.getSelectionModel().select(empruntTab);
            } else if (alert.getResult() == deleteBtn) {
                this.empruntData.remove(empruntData);
                this.deletedEmpruntData.add(empruntData);
                this.emprunTable.setItems(null);
                this.emprunTable.setItems(this.empruntData);
            }
        }
    }
}