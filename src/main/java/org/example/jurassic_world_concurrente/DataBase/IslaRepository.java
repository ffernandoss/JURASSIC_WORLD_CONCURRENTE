package org.example.jurassic_world_concurrente.DataBase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslaRepository extends JpaRepository<IslaEnti, Long> {
}