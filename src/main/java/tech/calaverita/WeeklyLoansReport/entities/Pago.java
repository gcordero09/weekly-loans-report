package tech.calaverita.WeeklyLoansReport.entities;

import lombok.Data;

@Data
public class Pago {
    private double Monto;
    private int Semana;
    private String Info;
    private String tipo;
    private String CreadoDesde;
    private String FechaPago;
    private Double Lat;
    private Double Lng;
    private String comentario;
    private String DatosMigracion;
    private String CreatedAt;
    private String UpdatedAt;
}
