package tech.calaverita.WeeklyLoansReport.entities;

import lombok.Data;

@Data
public class UpdateSaldo {
    private int indice;
    private String prestamo;
    private Double saldoNuevo;
    private Double saldoAnterior;
}
