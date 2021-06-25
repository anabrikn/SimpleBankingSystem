package banking;

import java.util.Random;


public class Card {
    private int id;
    private String number;
    private String pin;
    private int balance;

    public Card() {
        generateNumber();
        generatePin();
        balance = 0;
    }

    public Card(int id, String number, String pin, int balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    /**
     * Генерирует номер карты
     */
    private void generateNumber() {
        String str = "400000" + generateIdentifier();
        int ch = getCheckSum(str);
        number = str + ch;
    }

    /**
     * генерирует 9 цифр номера карты
     * @return
     */
    // TODO
    //  сделать проверку, что таких номеров в системе больше нет
    private String generateIdentifier() {
        StringBuilder str = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 9; i++) {
            str.append(r.nextInt(10));
        }
        return str.toString();
    }

    /**
     * применяет алгоритм Луна к 15 цифрам и возвращает контрольную цифру
     * @param s - первые 15 цифр карты
     * @return - контрольная, 16-ая цифра номера карты
     */
    private static int getCheckSum(String s) {
        // 1. умножить четные числа на 2
        // 2. вычесть 9 из всех чисел больше 9
        // 3. сложить все числа
        // 4. вычесть получившуся сумму из следующего круглого числа

        int sum = 0;
        String[] strings = s.split("");
        int[] ints = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.parseInt(strings[i]);
            if (i % 2 == 0) {
                ints[i] = ints[i] * 2;
                if (ints[i] > 9) {
                    ints[i] = ints[i] - 9;
                }
            }
            sum += ints[i];
        }

        int result = 0;
        if (sum % 10 > 0) {
            result = 10 - sum % 10;
        }

        return result;
    }

    // TODO создать метод, принимающий 16 цифр номера карты,
    //  применяющий к ним алгорит Луна и возвращающий результат -
    //  действителен номер карты или нет
    /**
     *
     * @param number - cart number
     * @return the result of checking by the Luhn algorithm and others
     */
    public static boolean isCardNumberValid(String number) {
        if (number.length() != 16) {
            System.out.println("Your line contains " + number.length() + " symbols");
            return false;
        }
        if ("\\d+".matches(number)) {
            System.out.println("Use numbers");
            return false;
        }
        String n = number.substring(0, 15);
        return getCheckSum(n) == Integer.parseInt(number.substring(15));
    }

    /**
     * Generating a pin code
     */
    private void generatePin() {
        String str = "" + (new Random().nextInt(9000) + 1000);
        pin = str;
    }

    /**
     * Prints information about the created card
     */
    public void printCardInfo() {
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(this.getNumber());
        System.out.println("Your card PIN:");
        System.out.println(this.getPin());
        System.out.println();
    }

    /**
     * @param cartNumber -
     * @param pinCode -
     * @return - логическое выражение, соответствует ли пин и номер значениям текущей карты
     */
    boolean isValidPin(String cartNumber, String pinCode) {
        return number.equals(cartNumber) && pin.equals(pinCode);
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}