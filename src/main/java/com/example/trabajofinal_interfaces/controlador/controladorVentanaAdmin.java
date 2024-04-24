package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class controladorVentanaAdmin {

    @FXML
    private TableColumn<Usuario, String> ColumnaApellido;

    @FXML
    private TableColumn<Usuario, Integer> ColumnaEdad;

    @FXML
    private TableColumn<Usuario, String> ColumnaEstadoCivil;

    @FXML
    private TableColumn<Usuario, String> ColumnaLocalidad;

    @FXML
    private TableColumn<Usuario, String> ColumnaNombre;

    @FXML
    private TableColumn<Usuario, String> ColumnaPasswrd;

    @FXML
    private TableColumn<Usuario, String> ColumnaSexo;

    @FXML
    private TableColumn<Usuario, String> ColumnaUsuario;

    @FXML
    private Button btnVolver;

    @FXML
    private TableView<Usuario> tableViewUsuarios;

    private ObservableList lista;
    private Usuario usuarioselec;

    @FXML
    void AdministrarEventos(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorEventos.fxml"));

        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    @FXML
    void AdministrarUsuarios(ActionEvent event) throws IOException {
        usuarioselec=tableViewUsuarios.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorPersonas c = (controladorGestorPersonas) loader.getController();
        c.inicializarComboBox();
        c.setUsuario(usuarioselec);
        stage.close();
        stage.show();
    }


    @FXML
    void VolverLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }
    @FXML
    public void CargarDatos(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        inicializarTableView();
        this.lista=listAll();
        this.tableViewUsuarios.setItems(lista);
    }
    public ObservableList<Usuario> listAll() throws ClassNotFoundException, SQLException {
        ObservableList<Usuario> listUser= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName("com.mysql.jdbc.Driver");
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/etreventos", "root", "");
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT personas.nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, localidades.nombre FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id;";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            String nomb=resul.getNString(1);
            String apell=resul.getNString(2);
            String sex=resul.getNString(3);
            String estado=resul.getNString(4);
            String usu=resul.getNString(5);
            String pass= resul.getNString(6);
            int ed=resul.getInt(7);
            String locID=resul.getString(8);
            listUser.add(new Usuario(nomb,apell,sex,estado,usu,locID,pass,ed));
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return listUser;
    }
    private void inicializarTableView() {
        this.ColumnaNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
        this.ColumnaApellido.setCellValueFactory(new PropertyValueFactory<Usuario, String>("apellidos"));
        this.ColumnaSexo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("sexo"));
        this.ColumnaEstadoCivil.setCellValueFactory(new PropertyValueFactory<Usuario, String>("estadoCivil"));
        this.ColumnaUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, String>("user"));
        this.ColumnaPasswrd.setCellValueFactory(new PropertyValueFactory<Usuario, String>("passwrd"));
        this.ColumnaEdad.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("edad"));
        this.ColumnaLocalidad.setCellValueFactory(new PropertyValueFactory<Usuario, String>("localidad"));
        // Crear la animación
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), tableViewUsuarios);
        transition.setByY(-300); // Mover la tabla hacia arriba
        transition.setAutoReverse(true); // Hacer que la animación se repita en reversa
        transition.setCycleCount(2); // Repetir la animación 2 veces
        transition.play(); // Iniciar la animación
    }
}

