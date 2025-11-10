package com.game.spring.application.springboot_applications.model;

import java.time.LocalDateTime;

public class pago {
    private String codigo;
    private String banco;
    private double valor;
    private String referencia;
    private LocalDateTime fecha;
    private boolean exitoso;

    public pago() {}

    public pago(String codigo, String banco, double valor, String referencia, LocalDateTime fecha, boolean exitoso) {
        this.codigo = codigo;
        this.banco = banco;
        this.valor = valor;
        this.referencia = referencia;
        this.fecha = fecha;
        this.exitoso = exitoso;
    }

    // getters y setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
}