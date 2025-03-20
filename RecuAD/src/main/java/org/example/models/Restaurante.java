package org.example.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
public class Restaurante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reseña> reseñas = new ArrayList<>();

    public void addReseña(Reseña reseña) {
        reseña.setRestaurante(this);
        this.reseñas.add(reseña);
    }
}
