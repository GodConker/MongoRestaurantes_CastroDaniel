/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

public class ConexionMongoDB {

    private final ConnectionString connectionString;
    private final MongoClient client;
    private final MongoDatabase database;

    public ConexionMongoDB(String host, int puerto, String usuario, String clave, String nombreBD) {
        String uri = String.format("mongodb://%s:%s@%s:%d/Asignación_20_Ejercicios_MongoDB__Java", usuario, clave, host, puerto);
        this.connectionString = new ConnectionString(uri);
        this.client = MongoClients.create(connectionString);
        this.database = client.getDatabase(nombreBD);
    }

    public boolean crearConexion() {
        try {
            database.runCommand(new Document("ping", 1));
            System.out.println("Conexión establecida exitosamente.");
            return true;
        } catch (MongoException e) {
            System.out.println("Error al verificar conexión: " + e);
            return false;
        }
    }

    public void cerrarConexion() {
        if (client != null) {
            client.close();
            System.out.println("Se ha cerrado la conexion con MongoDB.");
        }
    }

    public void insertarRestaurantes() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        // Crear fechas de inauguración con LocalDate
        Date fechaInauguracion1 = Date.from(LocalDate.of(2018, 3, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaInauguracion2 = Date.from(LocalDate.of(2015, 7, 20).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaInauguracion3 = Date.from(LocalDate.of(2000, 1, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Crear los documentos de los restaurantes
        Document restaurante1 = new Document("nombre", "La Pizzería Italiana")
                .append("rating", 5)
                .append("fechaInauguracion", fechaInauguracion1)
                .append("categorias", Arrays.asList("Pizza", "Italiana"));

        Document restaurante2 = new Document("nombre", "Sushilito")
                .append("rating", 4.2)
                .append("fechaInauguracion", fechaInauguracion2)
                .append("categorias", Arrays.asList("Sushi", "Japonesa"));

        Document restaurante3 = new Document("nombre", "Tacos Zacanta")
                .append("rating", 4)
                .append("fechaInauguracion", fechaInauguracion3)
                .append("categorias", Arrays.asList("Tacos", "Mexicana", "Carne Asada"));

        // Verificar si los restaurantes ya existen antes de insertarlos
        if (coleccion.find(new Document("nombre", "La Pizzería Italiana")).first() == null) {
            coleccion.insertOne(restaurante1);
            System.out.println("Restaurante 'La Pizzería Italiana' insertado correctamente.");
        } else {
            System.out.println("Restaurante 'La Pizzería Italiana' ya existe.");
        }

        if (coleccion.find(new Document("nombre", "Sushilito")).first() == null) {
            coleccion.insertOne(restaurante2);
            System.out.println("Restaurante 'Sushilito' insertado correctamente.");
        } else {
            System.out.println("Restaurante 'Sushilito' ya existe.");
        }

        if (coleccion.find(new Document("nombre", "Tacos Zacanta")).first() == null) {
            coleccion.insertOne(restaurante3);
            System.out.println("Restaurante 'Tacos Zacanta' insertado correctamente.");
        } else {
            System.out.println("Restaurante 'Tacos Zacanta' ya existe.");
        }
    }

    public void consultarRestaurantesConAltoRating() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.find(Filters.gt("rating", 4))
                .forEach(doc -> System.out.println(doc.toJson()));
    }

    public void consultarRestaurantesConCategoriaPizza() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.find(Filters.eq("categorias", "Pizza"))
                .forEach(doc -> System.out.println(doc.toJson()));
    }

    public void consultarRestaurantesConNombreSushi() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.find(Filters.regex("nombre", "sushi", "i"))
                .forEach(doc -> System.out.println(doc.toJson()));
    }

    public void agregarCategoriaExtra() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.updateOne(Filters.eq("nombre", "Sushilito"),
                new Document("$addToSet", new Document("categorias", "Fusión")));
        System.out.println("Categoria agregada al restaurante Sushilito.");
    }

    public void eliminarRestaurantePorId(String id) {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.deleteOne(Filters.eq("_id", new ObjectId(id)));
        System.out.println("Restaurante eliminado correctamente.");
    }

    public void eliminarRestaurantesConBajoRating() {
        MongoCollection<Document> coleccion = database.getCollection("restaurantes");

        coleccion.deleteMany(Filters.lte("rating", 3));
        System.out.println("Restaurantes con rating de 3 o menos eliminados.");
    }
}
