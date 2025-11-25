package com.game.spring.application.springboot_applications.service;

import com.game.spring.application.springboot_applications.model.Pago;
import com.game.spring.application.springboot_applications.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public void registrarPago(Pago pago) {
        pagoRepository.save(pago);
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }
}
