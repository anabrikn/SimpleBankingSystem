package banking;

import java.util.Scanner;

public class Bank {
    Scanner scanner;
    Ask ask;
    DataBase dataBase;

    static String mainMenu = new StringBuilder("1. Create an account")
            .append(System.lineSeparator())
            .append("2. Log into account")
            .append(System.lineSeparator())
            .append("0. Exit")
            .append(System.lineSeparator())
            .toString();

    static String userMenu = new StringBuilder("1. Balance")
            .append(System.lineSeparator())
            .append("2. Add income")
            .append(System.lineSeparator())
            .append("3. Do transfer")
            .append(System.lineSeparator())
            .append("4. Close account")
            .append(System.lineSeparator())
            .append("5. Log out")
            .append(System.lineSeparator())
            .append("0. Exit")
            .append(System.lineSeparator())
            .toString();

    public Bank(String db) {
        scanner = new Scanner(System.in);
        ask = new Ask();
        dataBase = new DataBase(db);
    }

    /**
     * Main menu
     */
    public void run() {
        boolean mainMenu = true;

        while (mainMenu) {
            String answer = ask.askQuestion(printMainMenu());

            switch (answer) {
                case "1": // создание карты
                    createCard().printCardInfo();
                    break;
                case "2": // авторизация

                    boolean isLogged = false;
                    System.out.println();
                    String cardNumber = ask.askQuestion("Enter your card number:");
                    String cardPin = ask.askQuestion("Enter your PIN:");
                    System.out.println();

                    isLogged = dataBase.isValidPin(cardNumber, cardPin);

                    if (isLogged) {
                        System.out.println("You have successfully logged in!" + System.lineSeparator());
                        mainMenu = logInCard(cardNumber);
                        if (mainMenu == false) {
                            System.out.print("Bye!" + System.lineSeparator());
                        }
                        break; //
                    }

                    if (!isLogged) {
                        System.out.println("Wrong card number or PIN!" + System.lineSeparator());
                    }

                    break;

                case "0":
                    mainMenu = false;
                    System.out.println();
                    System.out.print("Bye!" + System.lineSeparator());
                    break;
            }

        }

    }

    /**
     * Создает новую карту
     * Добавляет информацию о карте в БД
     */
    public Card createCard() {
        Card card = new Card();
        dataBase.addCardToDB(card);
        return card;
    }

    /**
     *
     * @param number
     * @return login result
     */
    public boolean logInCard(String number) {

        boolean userMenu = true;

        while (userMenu) {
            String a = ask.askQuestion(printUserMenu());

            switch (a) {
                case "1":
                    System.out.println();
                    System.out.println("Balance: " + dataBase.getCardBalance(number) + System.lineSeparator());
                    break;
                case "2":
                    System.out.println();
                    int amount = Integer.parseInt(ask.askQuestion("Enter income:"));
                    dataBase.addIncome(number, amount);
                    System.out.println("Income was added!" + System.lineSeparator());
                    break;
                case "3":
                    System.out.println();
                    System.out.println("Transfer");
                    String receiver = ask.askQuestion("Enter card number:");
                    if (Card.isCardNumberValid(receiver)) {
                        if (dataBase.isCardExist(receiver)) {
                            int sum = Integer.parseInt(ask.askQuestion("Enter how much money you want to transfer:"));
                            System.out.println(dataBase.doTransfer(number, receiver, sum) + System.lineSeparator());
                        } else {
                            System.out.println("Such a card does not exist.");
                        }

                    } else {
                        System.out.println("Probably you made mistake in the card number. Please try again!");
                    }

                    break;
                case "4":
                    System.out.println();
                    dataBase.deleteAccount(number);
                    System.out.println("The account has been closed!" + System.lineSeparator());
                    userMenu = false;
                    break;
                case "5":
                    System.out.println("5. Log out");
                    System.out.println();
                    System.out.println("You have successfully logged out!" + System.lineSeparator());
                    userMenu = false;

                    break;
                case "0":
                    userMenu = false;
                    System.out.println();
                    return false;
            }
        }
        return true;
    }

    /**
     * Печатает главное меню
     */
    public static String printMainMenu() {
        return mainMenu;
    }

    /**
     * Печатает пользовательское меню
     */
    public static String printUserMenu() {
        return userMenu;
    }
}