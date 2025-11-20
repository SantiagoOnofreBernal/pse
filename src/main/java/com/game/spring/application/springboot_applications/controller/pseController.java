package com.game.spring.application.springboot_applications.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.game.spring.application.springboot_applications.model.pago;
import com.game.spring.application.springboot_applications.service.PagoService;
import com.game.spring.application.springboot_applications.service.QRService;

@Controller
@RequestMapping("/pse")
public class pseController {

    @Autowired
    private PagoService pagoService;

    @GetMapping({"", "/", "/form"})
    public String mostrarFormulario(Model model) {
        model.addAttribute("mensaje", "Simulador de Pago PSE");
        return "pse_form";
    }

    @Autowired
    private QRService qrService;
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

    pago pago = new pago(codigo, banco, valor, referencia, fecha, aprobado);
    pagoService.registrarPago(pago);

    // Generar QR
    String qrData = String.format("Código: %s | Banco: %s | Valor: $%.0f | Fecha: %s", 
        codigo, banco, valor, fechaStr);
    String qrBase64 = qrService.generarQRBase64(qrData);

    model.addAttribute("banco", banco);
    model.addAttribute("valor", String.format("%.0f", valor));
    model.addAttribute("referencia", referencia);
    model.addAttribute("fecha", fechaStr);
    model.addAttribute("codigo", codigo);
    model.addAttribute("resultado", aprobado ? "PAGO EXITOSO ✅" : "ERROR EN EL PAGO ❌");
    model.addAttribute("exitoso", aprobado);
    model.addAttribute("qrImage", qrBase64); // QR en base64

    return "pse_comprobante";
    }
    @GetMapping("/historial")
public String historial(Model model) {
    List<pago> pagos = pagoService.listarPagos();

   
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    pagos.forEach(p -> {
        if (p.getFecha() != null) {
            
            p.setReferencia(p.getReferencia() + " "); 
        }
    });

    model.addAttribute("pagos", pagos);
    model.addAttribute("formatter", formatter);
    return "pse_historial";
}

    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv() {
        List<pago> pagos = pagoService.listarPagos();
        String header = "codigo,banco,valor,referencia,fecha,exitoso\n";
        String body = pagos.stream().map(p -> String.format("%s,%s,%.2f,%s,%s,%s",
                p.getCodigo(),
                p.getBanco().replace(",", " "),
                p.getValor(),
                p.getReferencia().replace(",", " "),
                p.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                p.isExitoso() ? "SI" : "NO"
        )).collect(Collectors.joining("\n"));

        String csv = header + body;
        byte[] bytes = csv.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "historial_pagos_pse.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}