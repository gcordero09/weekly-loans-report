package tech.calaverita.WeeklyLoansReport.scripts;

import com.google.gson.Gson;
import tech.calaverita.WeeklyLoansReport.controllers.LoanController;
import tech.calaverita.WeeklyLoansReport.entities.*;

import java.util.ArrayList;

public class MyUtil {

    static Gson gson = new Gson();

    public static void setJsonToFile(String json){
        ActionToDoToJson.createJsonFile(json);
    }

    public static void setDashboardToFile(String dashboard){
        ActionToDoToJson.createDashboardJsonFile(dashboard);
    }

    public static String getJsonOfPrestamoMigrates(){
        String json = gson.toJson(LoanController.prestamoMigrates);

        return json;
    }

    public static String getJsonOfDashboard(){
        String json = gson.toJson(LoanController.dashboards);

        return json;
    }

    public static void setPrestamoMigrateToJson(PrestamoMigrate prestamoMigrate){
        LoanController.prestamoMigrates.add(prestamoMigrate);

        setJsonToFile(getJsonOfPrestamoMigrates());
    }

    public static LoanLite searchPrestamo(String id){
        for(int i = 0; i < LoanController.prestamoMigrates.size(); i++){
            String loanId = LoanController.prestamoMigrates.get(i).getParams().getPrestamo().getLoan_id();

            if(loanId.equalsIgnoreCase(id))
                return LoanController.prestamoMigrates.get(i).getParams().getPrestamo();
        }
        return null;
    }

    public static PrestamoMigrate searchPrestamoMigrate(String id){
        for(int i = 0; i < LoanController.prestamoMigrates.size(); i++){
            String loanId = LoanController.prestamoMigrates.get(i).getParams().getPrestamo().getLoan_id();

            if(loanId.equalsIgnoreCase(id))
                return LoanController.prestamoMigrates.get(i);
        }
        return null;
    }

    public static Boolean isDashboard(String agencia, int anio, int semana){
        for(int i = 0; i < LoanController.dashboards.size(); i++){
            String _agencia = LoanController.dashboards.get(i).getAgencia();
            int _anio = LoanController.dashboards.get(i).getAnio();
            int _semana = LoanController.dashboards.get(i).getSemana();

            if(_agencia.equalsIgnoreCase(agencia))
                if(_anio == anio)
                    if(_semana == semana)
                        return true;
        }
        return false;
    }

    public static Dashboard searchDashboard(String agencia, int anio, int semana){
        for(int i = 0; i < LoanController.dashboards.size(); i++){
            String _agencia = LoanController.dashboards.get(i).getAgencia();
            int _anio = LoanController.dashboards.get(i).getAnio();
            int _semana = LoanController.dashboards.get(i).getSemana();

            if(_agencia.equalsIgnoreCase(agencia))
                if(_anio == anio)
                    if(_semana == semana)
                        return LoanController.dashboards.get(i);
        }
        return null;
    }

    public static ArrayList<Dashboard> getReporteSemanal(String gerencia, int anio, int semana){
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        for(int i = 0; i < LoanController.dashboards.size(); i++){
            String _gerencia = LoanController.dashboards.get(i).getGerencia();
            int _anio = LoanController.dashboards.get(i).getAnio();
            int _semana = LoanController.dashboards.get(i).getSemana();


            if(_gerencia.equalsIgnoreCase(gerencia))
                if(_anio == anio)
                    if(_semana == semana)
                        dashboards.add(LoanController.dashboards.get(i));
        }

        return dashboards;
    }

    public static Dashboard getDashboard(String agencia, int anio, int semana){

        return searchDashboard(agencia, anio, semana);
    }

    public static ArrayList<Pay> getPagos(String id){
        ArrayList<Pay> pagos = searchPrestamoMigrate(id).params.getPagos();

        return pagos;
    }

    public static void setPagoToJson(String id, Pay pago){
        getPagos(id).add(pago);

        setJsonToFile(getJsonOfPrestamoMigrates());
    }

    public static void deletePagoToJson(String id, int anio, int semana){
        getPagos(id).remove(searchPago(id, anio, semana));

        setJsonToFile(getJsonOfPrestamoMigrates());
    }

    public static Pay searchPago(String id, int anio, int semana){
        for(int i = 0; i < getPagos(id).size(); i++){
            if(getPagos(id).get(i).getYear() == anio)
                if(getPagos(id).get(i).getWeek() == semana)
                    return getPagos(id).get(i);
        }
        return null;
    }

    public static synchronized void setDashboardToJson( ){
        setDashboardToFile(getJsonOfDashboard());
    }
}
