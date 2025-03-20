package org.example;

import org.example.models.Reseña;
import org.example.models.Restaurante;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Servicio para la gestión de datos de la aplicación.
 * Permite manejar restaurantes y reseñas en la base de datos.
 */
public class DataService {
    private final SessionFactory sessionFactory;

    /**
     * Constructor del servicio de datos.
     *
     * @param sessionFactory Instancia de SessionFactory para Hibernate.
     */
    public DataService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Registra un nuevo restaurante en la base de datos.
     *
     * @param restaurante Restaurante a guardar.
     */
    public void saveRestaurante(Restaurante restaurante) {
        sessionFactory.inTransaction(session -> session.persist(restaurante));
    }

    /**
     * Importa datos de restaurantes desde un archivo CSV.
     *
     * @param filePath Ruta del archivo CSV con los datos.
     */
    public void importRestaurantesFromCSV(String filePath) {
        try (var reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(",");
                Restaurante restaurante = new Restaurante();
                restaurante.setNombre(datos[0]);
                restaurante.setCiudad(datos[1]);
                saveRestaurante(restaurante);
            }
            System.out.println("Importación completada.");
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las reseñas realizadas por un usuario específico.
     *
     * @param usuario Correo del usuario.
     * @return Lista de reseñas del usuario.
     */
    public List<Reseña> getReseñasByUsuario(String usuario) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Reseña WHERE usuario = :usuario", Reseña.class)
                    .setParameter("usuario", usuario)
                    .list();
        }
    }

    /**
     * Añade una reseña a un restaurante ya existente en la base de datos.
     *
     * @param restaurante Restaurante al que se añadirá la reseña.
     * @param reseña      Reseña a agregar.
     */
    public void addReseñaToRestaurante(Restaurante restaurante, Reseña reseña) {
        sessionFactory.inTransaction(session -> {
            restaurante.addReseña(reseña);
            session.merge(restaurante);
        });
    }

    /**
     * Obtiene un listado de restaurantes con baja valoración (≤3 de media).
     *
     * @return Lista de restaurantes con baja puntuación.
     */
    public List<Restaurante> getRestaurantesBajaValoracion() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT r FROM Restaurante r JOIN r.reseñas res GROUP BY r.id HAVING AVG(res.valoracion) <= 3",
                    Restaurante.class).list();
        }
    }

    /**
     * Identifica usuarios con actividad sospechosa (más de 3 reseñas con puntuación de 0 o 1).
     *
     * @return Lista de usuarios sospechosos.
     */
    public List<String> detectarUsuariosSospechosos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT usuario FROM Reseña WHERE valoracion IN (0, 1) GROUP BY usuario HAVING COUNT(*) > 3",
                    String.class).list();
        }
    }

    /**
     * Exporta las reseñas de los restaurantes de una ciudad específica a un archivo CSV.
     *
     * @param ciudad   Nombre de la ciudad.
     * @param filePath Ruta donde se guardará el archivo CSV.
     */
    public void exportarReseñasPorCiudad(String ciudad, String filePath) {
        try (Session session = sessionFactory.openSession();
             FileWriter writer = new FileWriter(filePath)) {

            List<Reseña> reseñas = session.createQuery(
                            "SELECT res FROM Reseña res JOIN res.restaurante r WHERE r.ciudad = :ciudad", Reseña.class)
                    .setParameter("ciudad", ciudad)
                    .list();

            writer.write("Usuario,Comentario,Valoracion\n");
            for (Reseña reseña : reseñas) {
                writer.write(reseña.getUsuario() + "," + reseña.getComentario() + "," + reseña.getValoracion() + "\n");
            }
            System.out.println("Exportación completada.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo CSV: " + e.getMessage());
        }
    }
}
