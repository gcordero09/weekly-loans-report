package tech.calaverita.WeeklyLoansReport.entities;

import lombok.Data;

@Data
public class LoanLite {
    private String loan_loan_type;
    private String request_date;
    private String loan_id;
    private Double monto_otorgado;
    private Double cargo;
    private Double descuento;
    private Double primer_pago;
    private Double tarifa;
    private String dia_pago;
    private String gerencia;
    private String agencia;
    private Integer anio;
    private Integer semana;
}
