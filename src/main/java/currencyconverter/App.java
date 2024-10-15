package src.main.java.currencyconverter;
import java.io.IOException;

import java.net.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;


public class App {
    Dotenv dotenv = Dotenv.configure()
            .directory("C:/Users/Gilberto/Documents/ProgramOne/coins_converter")
            .filename(".env_file")
            .load();
    private static final Map<Integer, String> CURRENCIES = new HashMap<>();

    static {
        CURRENCIES.put(1, "USD");
        CURRENCIES.put(2, "EUR");
        CURRENCIES.put(3, "GBP");
        CURRENCIES.put(4, "JPY");
        CURRENCIES.put(5, "BRL");
        CURRENCIES.put(6, "AUD");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CurrencyConverter currencyConverter = new CurrencyConverter();

        while (true) {
            displayMenu();
            int fromCurrencyChoice = getIntInput(scanner, "Enter the number of the currency you want to convert from (0 to exit): ");

            if (fromCurrencyChoice == 0) {
                System.out.println("Thank you for using the Currency Converter. Goodbye!");
                break;
            }

            if (!CURRENCIES.containsKey(fromCurrencyChoice)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            String fromCurrency = CURRENCIES.get(fromCurrencyChoice);

            int toCurrencyChoice = getIntInput(scanner, "Enter the number of the currency you want to convert to: ");

            if (!CURRENCIES.containsKey(toCurrencyChoice)) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            String toCurrency = CURRENCIES.get(toCurrencyChoice);

            double amount = getDoubleInput(scanner, "Enter the amount in " + fromCurrency + ": ");

            try {
                JsonObject rates = currencyConverter.getExchangeRates(fromCurrency);
                if (rates != null) {
                    double result = currencyConverter.convertCurrency(amount, fromCurrency, toCurrency, rates);
                    System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, result, toCurrency);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Error fetching exchange rates: " + e.getMessage());
            }
        }

        scanner.close();
    }


    private static void displayMenu() {
        System.out.println("\nCurrency Converter");
        System.out.println("Available currencies:");
        for (Map.Entry<Integer, String> entry : CURRENCIES.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }
        System.out.println("0. Exit");
    }


    private static int getIntInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }


    private static double getDoubleInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextDouble();
    }

}