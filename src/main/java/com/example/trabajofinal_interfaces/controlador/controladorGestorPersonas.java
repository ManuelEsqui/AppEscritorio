package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class controladorGestorPersonas {
    private Stage stage;
    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private Label lbError;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEstadoCivil;

    @FXML
    private TextField txtLocalidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cbSexo;

    @FXML
    private TextField txtUsuario;
    private String nombre,apellidos,sexo,estadoCivil,user,passwrd,localidad;
    private int edad;
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        rellenarCampos();
    }

    private void rellenarCampos() {
        if(usuario!=null){
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            txtEdad.setText(usuario.getEdad()+"");
            cbSexo.setValue(usuario.getSexo());
            txtEstadoCivil.setText(usuario.getEstadoCivil());
            txtUsuario.setText(usuario.getUser());
            txtContrasenia.setText(usuario.getPasswrd());
            txtLocalidad.setText(usuario.getLocalidad());
        }
    }

    @FXML
    void editarDatos(ActionEvent event) {//metodo para editar datos de los usuarios

        try {
            // Cargar el driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/etreventos", "root", "");
            //Se recogen los datos
            nombre=this.txtNombre.getText();
            apellidos=this.txtApellidos.getText();
            sexo=this.cbSexo.getValue();
            estadoCivil=this.txtEstadoCivil.getText();
            user=this.txtUsuario.getText();
            passwrd=this.txtContrasenia.getText();
            localidad=txtLocalidad.getText();
            String sEdad=txtEdad.getText();
            try {
                edad=Integer.parseInt(sEdad);
            }catch (Exception e){
                //Crea una alerta con un aviso
                Alert alerta=new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Fallo");
                alerta.setHeaderText(null);
                alerta.setContentText("Debes de rellenar el campo edad con un numero");
                alerta.showAndWait();
            }
            int id_loc=6;
            boolean comprobador;
            if (user.length()<1 || passwrd.length()<1 || nombre.length()<1 || sexo.length()<1 || estadoCivil.length()<1 || apellidos.length()<1 || localidad.length()<1){
                comprobador=false;
            }else{
                comprobador= true;
            }

            if (comprobador){
                Statement sentencia2 = (Statement) conexion.createStatement();
                String sql2 = "SELECT * FROM Localidades;";
                ResultSet resul = sentencia2.executeQuery(sql2);

                // Recorremos el resultado para visualizar cada fila
                // Se hace un bucle mientras haya registros
                boolean encontrada=false;
                while (resul.next()) {

                    if (resul.getString(2).equalsIgnoreCase(localidad)){
                        id_loc=resul.getInt(1);
                        encontrada=true;
                    }

                }
                if (!encontrada) lbError.setText("Localidad no encontrada en la bd");

                //Se crea la consulta para actualizar los datos
                String sql = "UPDATE Personas SET nombre = ?, apellidos = ?, sexo = ?, estadoCivil = ?, edad = ?,  localidad_id = ? WHERE user = ? AND passwrd = ?;";
                PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                sentencia.setString(2, apellidos);
                sentencia.setString(3, sexo);
                sentencia.setString(4, estadoCivil);
                sentencia.setInt(5,edad);
                sentencia.setInt(6,id_loc);
                sentencia.setString(7,user);
                sentencia.setString(8,passwrd);
                sentencia.executeUpdate();
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Actualizado");
                alerta.setHeaderText(null);
                alerta.setContentText("Cuenta actualizada correctamente");
                alerta.showAndWait();


                sentencia.close();
                sentencia2.close();
                resul.close();
            }else{
                Alert alerta=new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText("Se deben añadir todos los datos");
                alerta.showAndWait();
            }
            conexion.close();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void eliminarCuenta(ActionEvent event) throws SQLException, ClassNotFoundException {//metodo para eliminar usuarios
        // Cargar el driver
        Class.forName("com.mysql.jdbc.Driver");

        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/etreventos", "root", "");
        //Se hace la consulta para eliminar segun el usuario y la contraseña
        String sql = "DELETE FROM Personas WHERE user like ? AND passwrd = ?;";
        PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
        user=this.txtUsuario.getText();
        passwrd=this.txtContrasenia.getText();
        //Se introducen los parametros en el preparedStatement
        sentencia.setString(1, user);
        sentencia.setString(2, passwrd);
        int filas=sentencia.executeUpdate();
        System.out.println(filas+" rows afected");
        if (filas<1){
            lbError.setText("No se ha eliminado ninguna cuenta");
        }else{
            lbError.setText("Cuenta eliminada");
        }


        conexion.close();
        sentencia.close();


    }

    @FXML
    void registrarse(ActionEvent event) {//metodo para introducir nuevos usuarios

        try {
            // Cargar el driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/etreventos", "root", "");
            //Se recogen los datos
            nombre=this.txtNombre.getText();
            apellidos=this.txtApellidos.getText();
            sexo=this.cbSexo.getValue();
            estadoCivil=this.txtEstadoCivil.getText();
            user=this.txtUsuario.getText();
            passwrd=this.txtContrasenia.getText();
            String regex="^.{8,}$";
            if (!passwrd.matches(regex)){
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Warning");
                alerta.setHeaderText(null);
                alerta.setContentText("La contraseña no es segura");
                alerta.showAndWait();
            }
            localidad=txtLocalidad.getText();
            String sEdad=txtEdad.getText();
            try {
                edad=Integer.parseInt(sEdad);
            }catch (Exception e){
                Alert alerta=new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Fallo");
                alerta.setHeaderText(null);
                alerta.setContentText("Debes de rellenar el campo edad con un numero");
                alerta.showAndWait();
            }

            int id_loc=6;
            boolean comprobador;
            if (user.length()<1 || passwrd.length()<1 || nombre.length()<1 || sexo==null || estadoCivil.length()<1 || apellidos.length()<1 || localidad.length()<1){
                comprobador=false;
            }else{
                comprobador= true;
            }

            if (comprobador){
                Statement sentencia2 = (Statement) conexion.createStatement();
                String sql2 = "SELECT * FROM Localidades;";
                ResultSet resul = sentencia2.executeQuery(sql2);

                // Recorremos el resultado para visualizar cada fila
                // Se hace un bucle mientras haya registros
                boolean encontrada=false;
                while (resul.next()) {

                    if (resul.getString(2).equalsIgnoreCase(localidad)){
                        id_loc=resul.getInt(1);
                        encontrada=true;
                    }

                }
                if (!encontrada) lbError.setText("Localidad no encontrada en la bd");


                String sql = "INSERT INTO personas (nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, admin, localidad_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
                //Se introducen los parametros en el preparedStatement
                sentencia.setString(1, nombre);
                sentencia.setString(2, apellidos);
                sentencia.setString(3, sexo);
                sentencia.setString(4, estadoCivil);
                sentencia.setString(5,user);
                sentencia.setString(6, passwrd);
                sentencia.setInt(7,edad);
                sentencia.setBoolean(8, false);
                sentencia.setInt(9, id_loc);
                sentencia.executeUpdate();
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Añadido");
                alerta.setHeaderText(null);
                alerta.setContentText("Persona añadida con exito");
                alerta.showAndWait();



                sentencia.close();
                sentencia2.close();
                resul.close();
            }else{
                Alert alerta=new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error");
                alerta.setHeaderText(null);
                alerta.setContentText("Se deben rellenar todos los datos");
                alerta.showAndWait();
            }
            conexion.close();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void volver(ActionEvent event) throws IOException {// metodo para volver a la ventana principal
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    public void inicializarComboBox() {
        ObservableList<String> tiposSexo = FXCollections.observableArrayList();
        tiposSexo.addAll("Hombre","Mujer","Otro");
        cbSexo.setItems(tiposSexo);
    }
}

