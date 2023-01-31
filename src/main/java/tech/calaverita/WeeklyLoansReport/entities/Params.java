package tech.calaverita.WeeklyLoansReport.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Params {
    @SerializedName("cliente")
    Cliente ClienteObject;
    ArrayList<Object> activosCliente = new ArrayList<Object>();

    @SerializedName("aval")
    Aval AvalObject;
    ArrayList<Object> activosAval = new ArrayList<Object>();

    @SerializedName("prestamo")
    LoanLite prestamoObject;

    @SerializedName("pagos")
    ArrayList<Pago> pagos = new ArrayList<Pago>();
}
