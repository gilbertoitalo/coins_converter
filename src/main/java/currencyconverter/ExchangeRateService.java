package src.main.java.currencyconverter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ExchangeRateService {


    private final Gson Gson = new Gson(); // Instância do Gson

    // Método para processar a resposta JSON e obter a taxa de câmbio
    public double getExchangeRate(String jsonResponse, String targetCurrency) {
        System.out.println("Resposta JSON: " + jsonResponse);  // Verifique a estrutura do JSON

        // Verifica se a resposta JSON é um objeto
        JsonElement jsonElement = Gson.fromJson(jsonResponse, JsonElement.class);

        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("Resposta JSON inválida: esperava um objeto JSON.");
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Extrai a seção "rates" que contém as taxas de câmbio
        JsonObject rates = jsonObject.getAsJsonObject("rates");

        // Verifica se a moeda de destino está no JSON
        JsonElement rateElement = rates.get(targetCurrency);
        if (rateElement == null) {
            throw new IllegalArgumentException("Currency " + targetCurrency + " not found in the response.");
        }

        // Retorna a taxa de câmbio como um double
        return rateElement.getAsDouble();
    }

    // Método para simular a chamada da API e obter o JSON de exemplo
    public String getMockApiResponse() {
        // Simulação de uma resposta JSON de uma API de taxas de câmbio
        return "{ \"rates\": { \"USD\": 1.2, \"EUR\": 0.9 }, \"base\": \"GBP\", \"date\": \"2024-01-01\" }";
    }

    public static void main(String[] args) {
        ExchangeRateService service = new ExchangeRateService();

        // Simula a resposta da API
        String jsonResponse = service.getMockApiResponse();

        // Obtém a taxa de câmbio para o USD
        double rate = service.getExchangeRate(jsonResponse, "USD");

        System.out.println("Taxa de câmbio para USD: " + rate);
    }
}

