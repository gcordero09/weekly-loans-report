package tech.calaverita.WeeklyLoansReport.scripts;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import tech.calaverita.WeeklyLoansReport.entities.Dashboard;
import tech.calaverita.WeeklyLoansReport.entities.PrestamoMigrate;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ActionToDoToJson {
    public static int semanaJsonFile = 3;
    public static int anioJsonFile = 2023;

    public static String jsonFile = "src\\main\\java\\tech\\calaverita\\WeeklyLoansReport\\jsons\\sem_" + semanaJsonFile + "_" + anioJsonFile + "_migrate.json";
    static String dashboardFile = "src\\main\\java\\tech\\calaverita\\WeeklyLoansReport\\jsons\\dashboards.json";

    public static ArrayList<PrestamoMigrate> getJsonFromFile(){
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

    public static ArrayList<Dashboard> getDashboardFromFile(){
        ArrayList<Dashboard> dashboards = new ArrayList<>();

        Type type = new TypeToken<List<Dashboard>>() {
        }.getType();

        String readFile = null;
        try {
            readFile = readFileAsString(dashboardFile);
            dashboards = new Gson().fromJson(readFile, type);

            // validatorUtil.getLoansConLiquidacion();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashboards;
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    public static void createJsonFile(String json) {
        try {
            FileWriter file = new FileWriter(jsonFile);
            file.write(json);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDashboardJsonFile(String json) {
        try {
            FileWriter file = new FileWriter(dashboardFile);
            file.write(json);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
