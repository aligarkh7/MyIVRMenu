package org.example;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class MainMenu {
    private static Client client;
    private static JsonObject menuConfig;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadMenuConfig();
        loadClientData();
        displayMainMenu();
    }

    private static void loadClientData() {
        String name = menuConfig.getAsJsonObject("client").get("name").getAsString();
        int balance = menuConfig.getAsJsonObject("client").get("balance").getAsInt();
        long internetTraffic = menuConfig.getAsJsonObject("client").get("internetTraffic").getAsLong();
        client = new Client(name, balance, internetTraffic);
    }

    private static void loadMenuConfig() {
        try (FileReader reader = new FileReader("menu.json")) {
            menuConfig = new Gson().fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMainMenu() {
        java.time.LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        int hour = currentDateTime.getHour();
        String dayMessage = "";
        if (hour > 3 && hour < 7) {
            dayMessage = menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("day").get("goodNight").getAsString();
        } else if (hour > 6 && hour < 12) {
            dayMessage = menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("day").get("goodMorning").getAsString();
        } else if (hour > 11 && hour < 17) {
            dayMessage = menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("day").get("goodAfternoon").getAsString();
        } else if (hour > 16 && hour < 24) {
            dayMessage = menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("day").get("goodEvening").getAsString();
        } else if (hour > 23 || hour < 4) {
            dayMessage = menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("day").get("goodNight").getAsString();
        }
        System.out.println(dayMessage);

        System.out.println(menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").get("welcomeMessage").getAsString().replace("{clientName}", client.getName()));

        for (Map.Entry<String, JsonElement> map : menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").asMap().entrySet()) {
            System.out.println(map.getKey() + " " + menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").getAsJsonObject(map.getKey()).get("name").getAsString());
        }

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").keySet().toArray()[0].toString().equals(line)) {
            displayBalance();
            return;
        }

        if (menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").keySet().toArray()[1].toString().equals(line)) {
            displayInternetTraffic();
            return;
        }

        if (menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").keySet().toArray()[2].toString().equals(line)) {
            displaySpecialOffers();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        System.out.println("Неверный ввод. Попробуйте снова.");
        displayMainMenu();
    }

    private static void displayBalance() {
        int threshold = menuConfig.getAsJsonObject("menu").getAsJsonObject("balance").get("threshold").getAsInt();
        int balance = client.getBalance();
        String message = (balance < threshold) ? menuConfig.getAsJsonObject("menu").getAsJsonObject("balance").getAsJsonObject("conditions").get("lessThan").getAsString() : menuConfig.getAsJsonObject("menu").getAsJsonObject("balance").getAsJsonObject("conditions").get("default").getAsString().replace("{balance}", String.valueOf(balance));
        System.out.println(message);

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            displayBalance();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        displayErrorMessage();
        displayBalance();
    }

    private static void displayInternetTraffic() {
        long bytes = client.getInternetTraffic();
        long gb = bytes / (1024 * 1024 * 1024);
        long mb = (bytes / (1024 * 1024)) % 1024;
        long kb = (bytes / 1024) % 1024;
        String internetTraffic = gb + " ГБ " + mb + " МБ " + kb + " КБ";
        String message = menuConfig.getAsJsonObject("menu").getAsJsonObject("internetTraffic").get("message").getAsString().replace("{internetTraffic}", internetTraffic);
        System.out.println(message);

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            displayInternetTraffic();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        displayErrorMessage();
        displayInternetTraffic();
    }

    private static void displaySpecialOffers() {
        for (Map.Entry<String, JsonElement> map : menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").getAsJsonObject("3").getAsJsonObject("options").asMap().entrySet()) {
            System.out.println(map.getKey() + " " + menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").getAsJsonObject("3").getAsJsonObject("options").getAsJsonObject(map.getKey()).get("name").getAsString());
        }

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").getAsJsonObject("3").getAsJsonObject("options").keySet().toArray()[0].toString().equals(line)) {
            displayUnlimitedInternet();
            return;
        }

        if (menuConfig.getAsJsonObject("menu").getAsJsonObject("mainMenu").getAsJsonObject("options").getAsJsonObject("3").getAsJsonObject("options").keySet().toArray()[1].toString().equals(line)) {
            Discount50();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            displaySpecialOffers();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        displayErrorMessage();
        displaySpecialOffers();
    }

    private static void displayUnlimitedInternet() {
        System.out.println(menuConfig.getAsJsonObject("menu").getAsJsonObject("specialOffers").get("unlimitedInternet").getAsString());

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            displayUnlimitedInternet();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displaySpecialOffers();
            return;
        }

        displayErrorMessage();
        displayUnlimitedInternet();
    }

    private static void Discount50() {
        System.out.println(menuConfig.getAsJsonObject("menu").getAsJsonObject("specialOffers").get("discount50").getAsString());

        String line = sc.nextLine();

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(line).getAsString());
            displayMainMenu();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(line).getAsString());
            Discount50();
            return;
        }

        if (menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString().equals(line)) {
            System.out.println(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(line).getAsString());
            displaySpecialOffers();
            return;
        }

        displayErrorMessage();
        Discount50();
    }

    private static void displayErrorMessage() {
        String message = "Неверный ввод. Попробуйте снова.\n";
        message = message + menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString() + " " + menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").get(menuConfig.getAsJsonObject("controls").getAsJsonObject("mainMenu").keySet().toArray()[0].toString()).getAsString() + "\n";
        message = message + menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString() + " " + menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").get(menuConfig.getAsJsonObject("controls").getAsJsonObject("repeatMessage").keySet().toArray()[0].toString()).getAsString() + "\n";
        message = message + menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString() + " " + menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").get(menuConfig.getAsJsonObject("controls").getAsJsonObject("previousMenu").keySet().toArray()[0].toString()).getAsString();
        System.out.println(message);
    }
}
