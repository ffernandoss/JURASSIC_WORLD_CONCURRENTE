package org.example.jurassic_world_concurrente.DataBase;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "islas")
public class IslaEnti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;

    @OneToMany(mappedBy = "isla")
    private List<DinosaurioEnti> dinosaurios;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<DinosaurioEnti> getDinosaurios() {
        return dinosaurios;
    }

    public void setDinosaurios(List<DinosaurioEnti> dinosaurios) {
        this.dinosaurios = dinosaurios;
    }
}