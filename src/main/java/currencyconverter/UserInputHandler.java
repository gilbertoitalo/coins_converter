import java.util.Scanner;

public class UserInputHandler {
    private Scanner scanner - new Scanner (System.in);

    public String getCurrencyInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().toUpperCase();
    }

    public double getAmountInput(string promt){
        System.out.print(prompt);
        return scanner.nextDouble();
    }
}