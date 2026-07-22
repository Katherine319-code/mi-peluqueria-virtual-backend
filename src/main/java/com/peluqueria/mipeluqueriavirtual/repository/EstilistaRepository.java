package com.peluqueria.mipeluqueriavirtual.repository;
 
import com.peluqueria.mipeluqueriavirtual.entity.Estilista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
 
@Repository
public interface EstilistaRepository extends JpaRepository<Estilista, Long> {
    Optional<Estilista> findByUsuarioId(Long usuarioId);
}