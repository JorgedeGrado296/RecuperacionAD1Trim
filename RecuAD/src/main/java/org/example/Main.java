package org.example;

import org.example.models.Reseña;
import org.example.models.Restaurante;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        // Inicializa Hibernate
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        DataService dataService = new DataService(sessionFactory);

        // 1️ Registrar un nuevo restaurante
        Restaurante restaurante = new Restaurante();
        //restaurante.setNombre("O MammaMia");
        //restaurante.setCiudad("Málaga");
        //dataService.saveRestaurante(restaurante);

        // 2️ Importar restaurantes desde CSV
        //dataService.importRestaurantesFromCSV("src/main/resources/restaurantes.csv");

        // 3️ Obtener reseñas de un usuario específico
        //System.out.println(dataService.getReseñasByUsuario("usuario@mail.com"));

        // 4️ Añadir una reseña a un restaurante
        //Reseña reseña = new Reseña();
        //reseña.setComentario("Comida excelente!");
        //reseña.setUsuario("usuario@mail.com");
        //reseña.setValoracion(5);

        //dataService.addReseñaToRestaurante(restaurante, reseña);
        //System.out.println("Reseña agregada exitosamente.");

        // 5️ Listado de restaurantes con baja valoración
        //System.out.println(dataService.getRestaurantesBajaValoracion());

        // 6️ Detectar usuarios sospechosos
        //System.out.println(dataService.detectarUsuariosSospechosos());

        // 7️ Exportar reseñas de una ciudad a CSV
        dataService.exportarReseñasPorCiudad("Madrid", "src/main/resources/reseñas_madrid.csv");
    }

}
