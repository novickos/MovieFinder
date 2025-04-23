package com.example.aoim_plz_dzialaj;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class HelloController {

    @FXML
    private TextField _textfilter;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> firstNameCol;
    @FXML
    private TableColumn<Employee, String> lastNameCol;
    @FXML
    private TableColumn<Employee, EmployeeCondition> conditionCol;
    @FXML
    private TableColumn<Employee, Integer> birthYearCol;
    @FXML
    private TableColumn<Employee, Double> salaryCol;

    @FXML
    private TableView<ClassEmployee> groupTable;
    @FXML
    private TableColumn<ClassEmployee, String> groupNameCol;
    @FXML
    private TableColumn<ClassEmployee, Integer> maxEmployeesCol;

    private final ObservableList<Employee> allEmployees = FXCollections.observableArrayList();
    private final ObservableList<ClassEmployee> allGroups = FXCollections.observableArrayList();
    private final ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList();
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private ObservableList<ClassEmployee> employeeGroups = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        employeeTable.setItems(filteredEmployees);

        groupNameCol.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        maxEmployeesCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCurrentSize()).asObject() // Liczba aktualnych pracowników
        );

        groupTable.setItems(allGroups);

        loadSampleData();

        groupTable.getSelectionModel().selectedItemProperty().addListener(this::onGroupSelected);
        _textfilter.textProperty().addListener((observable, oldValue, newValue) -> filterEmployees(newValue));
    }

    private void loadSampleData() {
        Employee emp1 = new Employee("jano", "filano", EmployeeCondition.CHORY, 2006, 5000);
        Employee emp2 = new Employee("wiola", "borda", EmployeeCondition.DELEGACJA, 2003, 4500);
        Employee emp3 = new Employee("gabi", "matyjaszczyk", EmployeeCondition.OBECNY, 2002, 4000);
        Employee emp4 = new Employee("doska", "actaullynpamietam", EmployeeCondition.NIEOBECNY, 2004, 3800);

        allEmployees.addAll(emp1, emp2, emp3, emp4);

        ClassEmployee group1 = new ClassEmployee("frfr", 5);
        group1.addEmployee(emp1);
        group1.addEmployee(emp2);

        ClassEmployee group2 = new ClassEmployee("ratatata", 10);
        group2.addEmployee(emp3);
        group2.addEmployee(emp4);

        allGroups.addAll(group1, group2);
    }

    private void filterEmployees(String query) {
        ClassEmployee selectedGroup = groupTable.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            return;
        }

        if (query == null || query.isEmpty()) {
            filteredEmployees.setAll(selectedGroup.getEmployees());
        } else {
            ObservableList<Employee> results = FXCollections.observableArrayList(
                    selectedGroup.searchPartial(query)
            );

            filteredEmployees.setAll(results);
        }
    }

    private void onGroupSelected(ObservableValue<? extends ClassEmployee> observable, ClassEmployee oldGroup, ClassEmployee newGroup) {
        if (newGroup != null) {
            // aktualizujE tabele pracownikoq grupy
            filteredEmployees.setAll(newGroup.getEmployees());

            _textfilter.clear();
        }
    }


    @FXML
    private void addEmployee(ActionEvent event) {
        ClassEmployee selectedGroup = groupTable.getSelectionModel().getSelectedItem();
        if (selectedGroup == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No group selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        //matko jedyna to bedzie dlugie
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add Employee");

        VBox dialogVBox = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("First Name");
        TextField surnameField = new TextField();
        surnameField.setPromptText("Last Name");
        TextField birthYearField = new TextField();
        birthYearField.setPromptText("Birth Year");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        ComboBox<EmployeeCondition> conditionBox = new ComboBox<>();
        conditionBox.getItems().setAll(EmployeeCondition.values());
        conditionBox.setPromptText("Condition");

        dialogVBox.getChildren().addAll(nameField, surnameField, birthYearField, salaryField, conditionBox);
        dialog.getDialogPane().setContent(dialogVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    String surname = surnameField.getText();
                    int birthYear = Integer.parseInt(birthYearField.getText());
                    double salary = Double.parseDouble(salaryField.getText());
                    EmployeeCondition condition = conditionBox.getValue();

                    return new Employee(name, surname, condition, birthYear, salary);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        //dodaj pracwonika
        dialog.showAndWait().ifPresent(employee -> {
            selectedGroup.addEmployee(employee);
            filteredEmployees.setAll(selectedGroup.getEmployees());
        });
    }


    @FXML
    private void modifyEmployee(ActionEvent event) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No employee selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Modify Employee");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField nameField = new TextField(selectedEmployee.getFirstName());
        TextField surnameField = new TextField(selectedEmployee.getLastName());
        ComboBox<EmployeeCondition> conditionBox = new ComboBox<>(FXCollections.observableArrayList(EmployeeCondition.values()));
        conditionBox.setValue(selectedEmployee.getCondition());
        TextField birthYearField = new TextField(String.valueOf(selectedEmployee.getBirthYear()));
        TextField salaryField = new TextField(String.valueOf(selectedEmployee.getSalary()));

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                selectedEmployee.setName(nameField.getText());
                selectedEmployee.setSurname(surnameField.getText());
                selectedEmployee.setCondition(conditionBox.getValue());
                selectedEmployee.setBirthYear(Integer.parseInt(birthYearField.getText()));
                selectedEmployee.setSalary(Double.parseDouble(salaryField.getText()));

                employeeTable.refresh();
                dialog.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(new Label("Modify Employee"), nameField, surnameField, conditionBox, birthYearField, salaryField, saveButton);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @FXML
    private void deleteEmployee(ActionEvent event) {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No employee selected ", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        ClassEmployee selectedGroup = groupTable.getSelectionModel().getSelectedItem();
        if (selectedGroup != null) {
        //usun
            selectedGroup.removeEmployee(selectedEmployee);

            //aktuallizyj widok
            filteredEmployees.setAll(selectedGroup.getEmployees());
        }
    }



    @FXML
    private void addGroup(ActionEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Group");
        dialog.setHeaderText("Add a new employee group");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");

        TextField maxEmployeesField = new TextField();
        maxEmployeesField.setPromptText("Max Employees");

        GridPane grid = new GridPane();
        grid.add(new Label("Group Name:"), 0, 0);
        grid.add(groupNameField, 1, 0);
        grid.add(new Label("Max Employees:"), 0, 1);
        grid.add(maxEmployeesField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        //add
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(groupNameField.getText(), maxEmployeesField.getText());
            }
            return null;
        });

        // TU POBIERA DANE
        dialog.showAndWait().ifPresent(result -> {
            String groupName = result.getKey().trim();
            String maxEmployeesStr = result.getValue().trim();
            if (!groupName.isEmpty() && !maxEmployeesStr.isEmpty()) {
                try {
                    int maxEmployees = Integer.parseInt(maxEmployeesStr);

                    ClassEmployee newGroup = new ClassEmployee(groupName, maxEmployees);
                    allGroups.add(newGroup);
                } catch (NumberFormatException e) {
                    showErrorDialog("Invalid Input", "Max employees must be a number.");
                }
            } else {
                showErrorDialog("Invalid Input", "Please provide both group name and max employees.");
            }
        });
    }



    @FXML
    private void modifyGroup(ActionEvent event) {
        final ClassEmployee[] selectedGroup = {groupTable.getSelectionModel().getSelectedItem()};

        if (selectedGroup[0] != null) {
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Modify Group");
            dialog.setHeaderText("Modify the selected group");

            ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

            TextField groupNameField = new TextField(selectedGroup[0].getGroupName());
            TextField maxEmployeesField = new TextField(String.valueOf(selectedGroup[0].getMaxEmployees()));

            GridPane grid = new GridPane();
            grid.add(new Label("Group Name:"), 0, 0);
            grid.add(groupNameField, 1, 0);
            grid.add(new Label("Max Employees:"), 0, 1);
            grid.add(maxEmployeesField, 1, 1);
            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == modifyButtonType) {
                    return new Pair<>(groupNameField.getText(), maxEmployeesField.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(result -> {
                String newGroupName = result.getKey().trim();
                String newMaxEmployeesStr = result.getValue().trim();
                if (!newGroupName.isEmpty() && !newMaxEmployeesStr.isEmpty()) {
                    try {
                        int newMaxEmployees = Integer.parseInt(newMaxEmployeesStr);

                        selectedGroup[0].getEmployees().clear(); // resetuje pracowników, można dostosować
                        selectedGroup[0] = new ClassEmployee(newGroupName, newMaxEmployees);
                        groupTable.refresh();
                    } catch (NumberFormatException e) {
                        showErrorDialog("Invalid Input", "Max employees must be a number.");
                    }
                } else {
                    showErrorDialog("Invalid Input", "Please provide both group name and max employees.");
                }
            });
        } else {
            showErrorDialog("No Group Selected", "Please select a group to modify.");
        }
    }

    @FXML
    private void deleteGroup(ActionEvent event) {
        ClassEmployee selectedGroup = groupTable.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            allGroups.remove(selectedGroup);
        } else {
            showErrorDialog("No Group Selected", "Please select a group to delete.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
