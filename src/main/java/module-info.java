module com.example.aoim_plz_dzialaj {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.aoim_plz_dzialaj to javafx.fxml;
    exports com.example.aoim_plz_dzialaj;
}