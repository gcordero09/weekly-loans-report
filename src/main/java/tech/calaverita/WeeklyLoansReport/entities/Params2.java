package tech.calaverita.WeeklyLoansReport.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Params2 {
    @SerializedName("cliente")
    Cliente cliente;
    ArrayList<Object> activosCliente = new ArrayList<Object>();

    @SerializedName("aval")
    Aval aval;
    ArrayList<Object> activosAval = new ArrayList<Object>();

    @SerializedName("prestamo")
    LoanLite prestamo;

    @SerializedName("pagos")
    ArrayList<Pay> pagos = new ArrayList<Pay>();
}
