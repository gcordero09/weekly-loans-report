package tech.calaverita.WeeklyLoansReport.scripts;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import tech.calaverita.WeeklyLoansReport.controllers.LoanController;
import tech.calaverita.WeeklyLoansReport.entities.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Reporter extends Thread {
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    static ArrayList<PrestamoMigrate> prestamoMigrates = new ArrayList<>();
    static Dashboard dashboard = new Dashboard();
//    static int semanaFile = 3;
//    static int anioFile = 2023;
//    static String jsonFile = "src\\main\\java\\tech\\calaverita\\ReporterLoans\\json\\sem_" + semanaFile + "_" + anioFile + "_migrate.json";
    static String jsonFile = ActionToDoToJson.jsonFile;
    static String agencia = "AGD039";
    static int semanaAConsultar = 51;
    static int anioAConsultar = 2022;

    public Reporter(String agencia, int anio, int semana) {
        this.agencia = agencia;
        this.anioAConsultar = anio;
        this.semanaAConsultar = semana;
    }

    public static Dashboard getDashboard() {
        return dashboard;
    }

    public static ArrayList<PrestamoMigrate> getJsonFromFile() {
        ArrayList<PrestamoMigrate> prestamoMigrates = new ArrayList<>();

        Type type = new TypeToken<List<PrestamoMigrate>>() {
        }.getType();

        String readFile = null;
        try {
            readFile = readFileAsString(jsonFile);
            prestamoMigrates = new Gson().fromJson(readFile, type);

            // validatorUtil.getLoansConLiquidacion();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prestamoMigrates;
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static boolean isCancelarDescuento(int indice) {
        if (prestamoMigrates.get(indice).getParams().getPrestamo().getDescuento() > 0)
            return true;
        return false;
    }

    public static double getSaldo(int indice) {
        double montoOtorgado = prestamoMigrates.get(indice).getParams().getPrestamo().getMonto_otorgado();
        double cargo = prestamoMigrates.get(indice).getParams().getPrestamo().getCargo();

        return montoOtorgado + cargo;
    }

    public static double returnSaldoRestantePreCobro(int indice) {
        double saldoRestantePreCobro = getSaldo(indice);

        for (int i = 0; i < getUltimoPago(indice); i++) {
            saldoRestantePreCobro -= prestamoMigrates.get(indice).getParams().getPagos().get(i).getAmount();
        }

        if (isCancelarDescuento(indice))
            saldoRestantePreCobro -= prestamoMigrates.get(indice).getParams().getPrestamo().getDescuento();

        return saldoRestantePreCobro;
    }

    public static double returnSaldoRestantePostCobro(int indice) {
        double saldoRestantePostCobro = getSaldo(indice);

        for (int i = 0; i < getUltimoPago(indice) + 1; i++) {
            saldoRestantePostCobro -= prestamoMigrates.get(indice).getParams().getPagos().get(i).getAmount();
        }

        if (isCancelarDescuento(indice))
            saldoRestantePostCobro -= prestamoMigrates.get(indice).getParams().getPrestamo().getDescuento();

        return saldoRestantePostCobro;
    }

    public static double returnSaldoCobradoPreCobro(int indice) {
        double saldoCobradoPreCobro = getSaldo(indice) - returnSaldoRestantePreCobro(indice);

        return saldoCobradoPreCobro;
    }

    public static double returnSaldoCobradoPostCobro(int indice) {
        double saldoCobradoPostCobro = getSaldo(indice) - returnSaldoRestantePostCobro(indice);

        return saldoCobradoPostCobro;
    }

    public static void imprimirPrestamos(int indice) {
        String nameCliente = prestamoMigrates.get(indice).getParams().getCliente().getName();
        String apellido1Cliente = prestamoMigrates.get(indice).getParams().getCliente().getApellido1();
        String apellido2Cliente = prestamoMigrates.get(indice).getParams().getCliente().getApellido2();
        String nombreCompletoCliente = nameCliente + " " + apellido1Cliente + " " + apellido2Cliente;

        System.out.println("Cliente: " + nombreCompletoCliente);
        System.out.println("Tarifa: " + prestamoMigrates.get(indice).getParams().getPrestamo().getTarifa());
        System.out.println("Descuento: " + prestamoMigrates.get(indice).getParams().getPrestamo().getDescuento());
        ArrayList<Pay> pagos = prestamoMigrates.get(indice).getParams().getPagos();
        System.out.println("Pago: " + prestamoMigrates.get(indice).getParams().getPagos().get(getUltimoPago(indice)).getAmount());
        System.out.println("Saldo restante: " + returnSaldoRestantePostCobro(indice));
        System.out.println("Cobrado: " + returnSaldoCobradoPostCobro(indice));
        System.out.println("Saldo total: " + getSaldo(indice));
        System.out.println(" ");
    }

    public static void asignarPrestamos() {
        prestamoMigrates = getJsonFromFile();
        ArrayList<PrestamoMigrate> prestamoMigratesAux = new ArrayList<>();

        for (int i = 0; i < prestamoMigrates.size(); i++) {
            Params2 params = prestamoMigrates.get(i).getParams();
            LoanLite prestamo = params.getPrestamo();
            ArrayList<Pay> pagos = params.getPagos();

            for (int j = 0; j < pagos.size(); j++) {
                if (prestamo.getAgencia().equalsIgnoreCase(agencia))
                    if (pagos.get(j).getYear() == anioAConsultar && pagos.get(j).getWeek() == semanaAConsultar && pagos.get(j) != pagos.get(0)) {
                        prestamoMigratesAux.add(prestamoMigrates.get(i));
                    }
            }
        }

        prestamoMigrates = prestamoMigratesAux;
    }

    public static int getUltimoPago(int indice){
        ArrayList<Pay> pagos = prestamoMigrates.get(indice).getParams().getPagos();

        for (int i = 0; i < pagos.size(); i++) {
            if (pagos.get(i).getYear() == anioAConsultar && pagos.get(i).getWeek() == semanaAConsultar)
                return i;
        }
        return 0;
    }

    public static void verificarDatosSemanales() {
        for (int i = 0; i < prestamoMigrates.size(); i++) {
            LoanLite prestamo = prestamoMigrates.get(i).getParams().getPrestamo();
            ArrayList<Pay> pagos = prestamoMigrates.get(i).getParams().getPagos();

            //No. de Clientes
            dashboard.setClientes(dashboard.getClientes() + 1);

            //Ajsute de Tarifa
            if (pagos.get(getUltimoPago(i)).getAmount() == returnSaldoRestantePreCobro(i) && pagos.get(getUltimoPago(i)).getAmount() < prestamo.getTarifa())
                prestamo.setTarifa(pagos.get(getUltimoPago(i)).getAmount());
            if (returnSaldoRestantePreCobro(i) < prestamo.getTarifa())
                prestamo.setTarifa(returnSaldoRestantePreCobro(i));

            //No pagos
            if (pagos.get(getUltimoPago(i)).getAmount() == 0)
                dashboard.setNumeroDeNoPagos(dashboard.getNumeroDeNoPagos() + 1);

            //No. Liquidaciones
            if (pagos.get(getUltimoPago(i)).getAmount() > prestamo.getTarifa() && prestamo.getDescuento() > 0) {
                dashboard.setNumeroDeLiquidaciones(dashboard.getNumeroDeLiquidaciones() + 1);
                //Total de Descuento
                dashboard.setTotalDescuento(dashboard.getTotalDescuento() + prestamo.getDescuento());
                //Liquidaciones
                double liquidacion = pagos.get(getUltimoPago(i)).getAmount() - prestamo.getTarifa();
                dashboard.setLiquidacionesCobranza(dashboard.getLiquidacionesCobranza() + liquidacion);
            }

            //Pagos Reducidos
            if (pagos.get(getUltimoPago(i)).getAmount() < prestamo.getTarifa() && pagos.get(getUltimoPago(i)).getAmount() > 0)
                dashboard.setNumeroDePagosReducidos(dashboard.getNumeroDePagosReducidos() + 1);

            //Debito Miercoles
            if (prestamo.getDia_pago().equalsIgnoreCase("Miercoles"))
                dashboard.setDebitoMiercoles(dashboard.getDebitoMiercoles() + prestamo.getTarifa());

            //Debito Jueves
            if (prestamo.getDia_pago().equalsIgnoreCase("Jueves"))
                dashboard.setDebitoJueves(dashboard.getDebitoJueves() + prestamo.getTarifa());

            //Debito Viernes
            if (prestamo.getDia_pago().equalsIgnoreCase("Viernes"))
                dashboard.setDebitoViernes(dashboard.getDebitoViernes() + prestamo.getTarifa());

            //Monto Excedente
            if (pagos.get(getUltimoPago(i)).getAmount() > prestamo.getTarifa()) {
                double excedente = pagos.get(getUltimoPago(i)).getAmount() - prestamo.getTarifa();
                dashboard.setMontoExcedente(dashboard.getMontoExcedente() + excedente);
            }

            //Cobranza Total
            dashboard.setCobranzaTotal(dashboard.getCobranzaTotal() + pagos.get(getUltimoPago(i)).getAmount());

            //Monto Debito Faltante
            if (pagos.get(getUltimoPago(i)).getAmount() < prestamo.getTarifa()) {
                double faltante = prestamo.getTarifa() - pagos.get(getUltimoPago(i)).getAmount();
                dashboard.setMontoDebitoFaltante(dashboard.getMontoDebitoFaltante() + faltante);
            }

//            imprimirPrestamos(i);

            dashboard.setGerencia(prestamo.getGerencia());
            dashboard.setAgencia(prestamo.getAgencia());
            dashboard.setAnio(anioAConsultar);
            dashboard.setSemana(semanaAConsultar);
        }

        //Debito Total
        dashboard.setDebitoTotal(dashboard.getDebitoMiercoles() + dashboard.getDebitoJueves() + dashboard.getDebitoViernes());
        //Cobranza Pura
        dashboard.setCobranzaPura(dashboard.getCobranzaTotal() - dashboard.getMontoExcedente());
        //Rendimiento
        dashboard.setRendimientoCobranza(dashboard.getCobranzaPura() / dashboard.getDebitoTotal() * 100);
    }

    public static void imprimirDashboard() {
        System.out.println("No. de clientes: " + dashboard.getClientes());
        System.out.println("No pagos: " + dashboard.getNumeroDeNoPagos());
        System.out.println("No. liquidaciones: " + dashboard.getNumeroDeLiquidaciones());
        System.out.println("Pagos reducidos: " + dashboard.getNumeroDePagosReducidos());
        System.out.println("Debito Miercoles: " + dashboard.getDebitoMiercoles());
        System.out.println("Debito Jueves: " + dashboard.getDebitoJueves());
        System.out.println("Debito Viernes: " + dashboard.getDebitoViernes());
        System.out.println("Debito total: " + decfor.format(dashboard.getDebitoTotal()));
        System.out.println("Rendimiento: " + decfor.format(dashboard.getRendimientoCobranza()) + "%");
        System.out.println("Total de descuento: " + decfor.format(dashboard.getTotalDescuento()));
        System.out.println("Total cobranza pura: " + decfor.format(dashboard.getCobranzaPura()));
        System.out.println("Monto excedente:" + decfor.format(dashboard.getMontoExcedente()));
        System.out.println("Multas: ");
        System.out.println("Liquidaciones: " + decfor.format(dashboard.getLiquidacionesCobranza()));
        System.out.println("Cobranza total: " + decfor.format(dashboard.getCobranzaTotal()));
        System.out.println("Monto de Debito faltante: " + decfor.format(dashboard.getMontoDebitoFaltante()));
    }

    public void run() {
        try {
            dashboard = new Dashboard();
            asignarPrestamos();
            verificarDatosSemanales();
            LoanController.dashboards.add(getDashboard());
            MyUtil.setDashboardToJson();

        } catch (Exception e) {
        }

        System.out.println("Dashboard " + agencia + "_" + anioAConsultar + "_" + semanaAConsultar + " Creado con Éxito");
    }

    public static void main(String[] args) {
        asignarPrestamos();
        verificarDatosSemanales();
        imprimirDashboard();
    }
}