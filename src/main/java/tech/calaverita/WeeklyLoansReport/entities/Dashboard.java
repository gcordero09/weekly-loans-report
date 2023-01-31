package tech.calaverita.WeeklyLoansReport.entities;

import lombok.Data;

@Data
public class Dashboard {
    private String gerencia;
    private String agencia;
    private int anio;
    private int semana;
    private int clientes;
    private int numeroDeNoPagos;
    private int numeroDeLiquidaciones;
    private int numeroDePagosReducidos;
    private double debitoMiercoles;
    private double debitoJueves;
    private double debitoViernes;
    private double debitoTotal;
    private double rendimientoCobranza;
    private double totalDescuento;
    private double cobranzaPura;
    private double montoExcedente;
    private double liquidacionesCobranza;
    private double cobranzaTotal;
    private double montoDebitoFaltante;
}
