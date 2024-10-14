package src.main.java.currencyconverter;
import java.io.IOException;

import java.net.http.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;


public class App {
    Properties env = new Properties();
    private static final Map<Integer, String> CURRENCIES = new HashMap<>();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final JsonObject JsonObject = new JsonObject();

    static {
        CURRENCIES.put(1, "USD");
        CURRENCIES.put(2, "EUR");
        CURRENCIES.put(3, "GBP");
        CURRENCIES.put(4, "JPY");
        CURRENCIES.put(5, "CAD");
        CURRENCIES.put(6, "AUD");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiKeyManager apiKeyManager = new ApiKeyManager();
        CurrencyConverter currencyConverter = new CurrencyConverter(apiKeyManager.getApiKey());

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
    private static boolean initializeApiKey() {
        try {
            // First, try to load from .env_file file
            Properties env = new Properties();
            env.load(Files.newBufferedReader(Paths.get(".env_file")));
            String apiKey = env.getProperty("API_KEY");

            // If not found in .env_file, try system environment
            if (apiKey == null || apiKey.isEmpty()) {
                apiKey = System.getenv("API_KEY");
            }

            // If still not found, prompt user
            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("API key not found in .env_file file or environment variables.");
                System.out.println("Please enter your API key:");
                Scanner scanner = new Scanner(System.in);
                apiKey = scanner.nextLine().trim();
            }

            if (apiKey.isEmpty()) {
                System.out.println("No API key provided. Cannot proceed.");
                return false;
            }

            System.out.println("API Key loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error reading .env_file file: " + e.getMessage());
            return false;
        }
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