package com.peluqueria.mipeluqueriavirtual.repository;
 
import com.peluqueria.mipeluqueriavirtual.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {}
 
