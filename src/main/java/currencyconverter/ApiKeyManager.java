package src.main.java.currencyconverter;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;
import java.io.IOException;

public class ApiKeyManager {
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public boolean initializeApiKey() {
        try {
            Properties env = new Properties();
            env.load(Files.newBufferedReader(Paths.get(".env_file")));
            apiKey = env.getProperty("API_KEY");

            if (apiKey == null || apiKey.isEmpty()) {
                apiKey = System.getenv("API_KEY");
            }

            if (apiKey == null || apiKey.isEmpty()) {
                System.out.println("API key not found in .env_file or environment variables.");
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
}
