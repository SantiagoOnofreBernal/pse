package com.game.spring.application.springboot_applications.controller;

import com.game.spring.application.springboot_applications.model.Pago;
import com.game.spring.application.springboot_applications.service.PagoService;
import com.game.spring.application.springboot_applications.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/pse")
public class PseController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private QRService qrService;

    @GetMapping({ "", "/", "/form" })
    public String mostrarFormulario(Model model) {
        model.addAttribute("mensaje", "Simulador de Pago PSE");
        return "pse_form";
    }

    @PostMapping("/procesar")
    public String procesarPago(
            @RequestParam String banco,
            @RequestParam double valor,
            @RequestParam String referencia,
            Model model) {

        boolean aprobado = valor > 0 && valor <= 1000000;
        String codigo = "PSE-" + (int)(Math.random() * 900000 + 100000);
        LocalDateTime fecha = LocalDateTime.now();
        String fechaStr = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        Pago pago = new Pago(codigo, banco, valor, referencia, fecha, aprobado);
        pagoService.registrarPago(pago);

        String qrData = String.format("Código: %s | Banco: %s | Valor: $%.0f | Fecha: %s",
                codigo, banco, valor, fechaStr);
        String qrBase64 = qrService.generarQRBase64(qrData);

        model.addAttribute("banco", banco);
        model.addAttribute("valor", String.format("%.0f", valor));
        model.addAttribute("referencia", referencia);
        model.addAttribute("fecha", fechaStr);
        model.addAttribute("codigo", codigo);
        model.addAttribute("resultado", aprobado ? "PAGO EXITOSO" : "ERROR EN EL PAGO");
        model.addAttribute("exitoso", aprobado);
        model.addAttribute("qrImage", qrBase64);

        return "pse_comprobante";
    }

    @GetMapping("/historial")
    public String historial(Model model) {
        model.addAttribute("pagos", pagoService.listarPagos());
        return "pse_historial";
    }
}