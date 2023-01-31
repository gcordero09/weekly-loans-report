package tech.calaverita.WeeklyLoansReport.entities;

import lombok.Data;

@Data
public class Pay {
    private Double amount;
//    private Double rate;
    private int week;
    private int year;
//    private boolean isFirstPay;
//    private Double open;
//    private Double close;
//    private String info;
//    private String type;
    private String createdFrom;
    private String payDate;
    private Double lat;
    private Double lng;
//    private String comment;
//    private String loanId;
    private String infoMigration;
}
