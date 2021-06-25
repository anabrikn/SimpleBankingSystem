package banking;

import java.util.Scanner;

public class Ask {
    Scanner scanner;

    public Ask() {
        scanner = new Scanner(System.in);
    }

    public String askQuestion(String s) {
        System.out.println(s);
        return scanner.next();
    }

    public String askQuestion() {
        return scanner.next();
    }
}