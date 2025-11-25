package com.game.spring.application.springboot_applications.repository;

import com.game.spring.application.springboot_applications.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {}