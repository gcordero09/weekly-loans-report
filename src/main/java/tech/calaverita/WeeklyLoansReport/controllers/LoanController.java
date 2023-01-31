package tech.calaverita.WeeklyLoansReport.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tech.calaverita.WeeklyLoansReport.entities.*;
import tech.calaverita.WeeklyLoansReport.scripts.*;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/xpress-loans/v1")
public class LoanController {
    public static ArrayList<PrestamoMigrate> prestamoMigrates = ActionToDoToJson.getJsonFromFile();
    public static ArrayList<Dashboard> dashboards = ActionToDoToJson.getDashboardFromFile();
    //    public static ArrayList<Dashboard> dashboards = new ArrayList<>();
    public String[][] agencias = {{"AGD006", "AGD007", "AGD013", "AGD014", "AGD016", "AGD018", "AGD039", "AGD041", "AGD043"}, {}, {}};
    public String[] agenciasGer003 = agencias[0];
    public int[] anios = {2021, 2022, 2023};
    public int[][] semanas = {{}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53}, {1, 2, 3}};

    @GetMapping(path = "/loans")
    public @ResponseBody String getLoans() {

        return "Hola mundo";
    }

    @PostMapping(path = "/loan/up")
    public @ResponseBody String setLoans(@RequestBody PrestamoMigrate prestamoMigrate) {
        MyUtil.setPrestamoMigrateToJson(prestamoMigrate);

        return "Prestamo subido con éxito";
    }

    @PutMapping(path = "/dashboard-update/{agencia}/{anio}/{semana}")
    public @ResponseBody String putDashboard(@PathVariable("semana") int semana, @PathVariable("anio") int anio, @PathVariable("agencia") String agencia) {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        dashboards = ActionToDoToJson.getDashboardFromFile();

        if (MyUtil.isDashboard(agencia, anio, semana)) {
            deleteDashboard(semana, anio, agencia);
        } else {
            return "El Dashboard que Desea Actualizar no Existe";
        }

        setDashboard(semana, anio, agencia);

        return "Dashboard Actualizado";
    }

    @PostMapping(path = "/loan/{id}/pay")
    public @ResponseBody String setPay(@RequestBody Pay pago, @PathVariable("id") String id) {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        dashboards = ActionToDoToJson.getDashboardFromFile();

        MyUtil.setPagoToJson(id, pago);

        LoanLite prestamo = MyUtil.searchPrestamo(id);

        String agencia = prestamo.getAgencia();
        int anio = pago.getYear();
        int semana = pago.getWeek();

        if (MyUtil.isDashboard(agencia, anio, semana))
            deleteDashboard(semana, anio, agencia);

        setDashboard(semana, anio, agencia);

        return "Pago Realizado con Éxito";
    }

    @DeleteMapping(path = "/loan/{id}/pay-delete/{anio}/{semana}")
    public @ResponseBody String deletePay(@PathVariable("id") String id, @PathVariable("anio") int anio, @PathVariable("semana") int semana) {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        dashboards = ActionToDoToJson.getDashboardFromFile();

        MyUtil.deletePagoToJson(id, anio, semana);

        LoanLite prestamo = MyUtil.searchPrestamo(id);

        String agencia = prestamo.getAgencia();

        if (MyUtil.isDashboard(agencia, anio, semana))
            deleteDashboard(semana, anio, agencia);

        setDashboard(semana, anio, agencia);

        return "Pago Eliminado con Éxito";
    }

    @DeleteMapping(path = "/dashboard/{agencia}/{anio}/{semana}")
    public @ResponseBody String deleteDashboard(@PathVariable("semana") int semana, @PathVariable("anio") int anio, @PathVariable("agencia") String agencia) {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        dashboards = ActionToDoToJson.getDashboardFromFile();

        if (MyUtil.searchDashboard(agencia, anio, semana) == null) {
            return "El Dashboard que desea Eliminar No Existe";
        }

        dashboards.remove(MyUtil.searchDashboard(agencia, anio, semana));

        MyUtil.setDashboardToJson();

        return "Dashboard Eliminado con Éxito";
    }

    @GetMapping(path = "/dashboard/{agencia}")
    public @ResponseBody ArrayList<Dashboard> getDashboards(@PathVariable("agencia") String agencia) {


        return null;
    }

    @GetMapping(path = "/dashboard/{agencia}/{anio}")
    public @ResponseBody ArrayList<Dashboard> getDashboards(@PathVariable("anio") int anio, @PathVariable("agencia") String agencia) {


        return null;
    }

    @GetMapping(path = "/dashboard/{agencia}/{anio}/{semana}")
    public @ResponseBody Dashboard getDashboard(@PathVariable("semana") int semana, @PathVariable("anio") int anio, @PathVariable("agencia") String agencia) {
        dashboards = ActionToDoToJson.getDashboardFromFile();

        if (!MyUtil.isDashboard(agencia, anio, semana))
            return null;

        return MyUtil.searchDashboard(agencia, anio, semana);
    }

    @GetMapping(path = "/reporte-semanal/{gerencia}/{anio}/{semana}")
    public @ResponseBody ArrayList<Dashboard> getDashboards(@PathVariable("semana") int semana, @PathVariable("anio") int anio, @PathVariable("gerencia") String gerencia) {
        dashboards = ActionToDoToJson.getDashboardFromFile();

        ArrayList<Dashboard> reporteSemanal = MyUtil.getReporteSemanal(gerencia, anio, semana);

        return reporteSemanal;
    }

    @PostMapping(path = "/dashboard-create/{agencia}/{anio}/{semana}")
    public @ResponseBody String setDashboard(@PathVariable("semana") int semana, @PathVariable("anio") int anio, @PathVariable("agencia") String agencia) {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        dashboards = ActionToDoToJson.getDashboardFromFile();

        if (MyUtil.isDashboard(agencia, anio, semana)) {
            return "El Dashboard que Desea Crear ya Existe";
        }

        Reporter reporter = new Reporter(agencia, anio, semana);
        reporter.run();

        return "Dashboard Creado con Éxito";
    }

    @PostMapping(path = "/dashboards-create")
    public @ResponseBody String setDashboards() {
        prestamoMigrates = ActionToDoToJson.getJsonFromFile();
        ActionToDoToJson.createDashboardJsonFile("[]");
        for (int i = 1; i < anios.length; i++)
            for (int j = 0; j < semanas[i].length; j++)
                for (int k = 0; k < agencias[0].length; k++) {
                    dashboards = ActionToDoToJson.getDashboardFromFile();
                    if (!MyUtil.isDashboard(agencias[0][k], anios[i], semanas[i][j])) {
                        Reporter reporter = new Reporter(agencias[0][k], anios[i], semanas[i][j]);
                        reporter.run();

                    }
                }

        return "Dashboards Creados con Éxito";
    }
}
