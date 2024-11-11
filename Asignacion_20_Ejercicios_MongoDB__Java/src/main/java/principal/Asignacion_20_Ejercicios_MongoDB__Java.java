/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package principal;

import com.mongodb.MongoException;
import conexion.ConexionMongoDB;

/**
 *
 * @author danie
 */
public class Asignacion_20_Ejercicios_MongoDB__Java {

    public static void main(String[] args) {
        // Uso de parámetros configurables
        String host = "localhost";
        int puerto = 27017;
        String usuario = "admin";
        String clave = "1234"; // Es recomendable usar un archivo de configuración para estas credenciales
        String baseDeDatos = "Asignación_20_Ejercicios_MongoDB__Java";

        ConexionMongoDB mongoDB = new ConexionMongoDB(host, puerto, usuario, clave, baseDeDatos);

        try {
            // Crear conexión
            if (mongoDB.crearConexion()) {

                // Llamada a las operaciones
                mongoDB.insertarRestaurantes();
                System.out.println("Restaurantes con alto Rating:");
                mongoDB.consultarRestaurantesConAltoRating();
                System.out.println("Restaurantes con categoría Pizza:");
                mongoDB.consultarRestaurantesConCategoriaPizza();
                System.out.println("Restaurante con Nombre Sushi");
                mongoDB.consultarRestaurantesConNombreSushi();
                mongoDB.agregarCategoriaExtra();

                // Ejemplo de ID para eliminar un restaurante
                String idRestaurante = "67318aba2837db366c8623ca";
                mongoDB.eliminarRestaurantePorId(idRestaurante);

                mongoDB.eliminarRestaurantesConBajoRating();
            } else {
                System.out.println("No se pudo establecer la conexión con MongoDB.");
            }

        } catch (MongoException e) {
            System.out.println("Error al verificar conexión: " + e);
        } finally {
            // Cerrar la conexión
            mongoDB.cerrarConexion();
        }
    }
}
