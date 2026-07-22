package com.peluqueria.mipeluqueriavirtual.repository;
 
import com.peluqueria.mipeluqueriavirtual.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
 
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByEstilistaId(Long estilistaId);
    List<Cita> findByEstilistaIdAndFecha(Long estilistaId, LocalDate fecha);
    List<Cita> findByEstilistaIdAndFechaBetween(Long estilistaId, LocalDate inicio, LocalDate fin);
}
