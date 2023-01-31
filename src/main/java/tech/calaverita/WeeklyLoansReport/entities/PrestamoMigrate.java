package tech.calaverita.WeeklyLoansReport.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PrestamoMigrate {
    private String jsonrpc;
    private String method;
    private String id;
    @SerializedName("params")
    public Params2 params;
}

