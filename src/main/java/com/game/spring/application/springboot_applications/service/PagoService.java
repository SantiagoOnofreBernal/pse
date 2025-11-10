package com.game.spring.application.springboot_applications.service;

import com.game.spring.application.springboot_applications.model.pago;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PagoService {


    private final List<pago> pagos = new CopyOnWriteArrayList<>();

    public void registrarPago(pago pago) {
        pagos.add(0, pago); 
    }

    public List<pago> listarPagos() {
        return pagos;
    }

    public void clearAll() {
        pagos.clear();
    }
}