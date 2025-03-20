package org.example.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "reseña")
public class Reseña implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "valoracion", nullable = false)
    private Integer valoracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
}
