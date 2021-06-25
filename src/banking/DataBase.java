package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DataBase {
    String db;

    public DataBase(String db) {
        this.db = db;
        createBD();
    }

    /**
     * Метод для получения источника БД
     * База данных создается автоматически, если ее еще не существует
     * @return - SQLiteDataSource
     */
    public SQLiteDataSource getDataSource() {
        String url = "jdbc:sqlite:" + db;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    /**
     * Метод создает таблицу card в базе данных (если ее еще нет)
     * и назначает названия столбцов:
     * id, number, pin, balance
     */
    public void createBD() {
        String sql = "CREATE TABLE IF NOT EXISTS card(id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT NOT NULL, pin TEXT NOT NULL, balance INTEGER DEFAULT 0)";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement pStatement = con.prepareStatement(sql)) {
            pStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод заносит информацию о карте в базу данных
     * @param card - Объект класса Card, содержащий информацию number, pin, balance
     */
    public void addCardToDB(Card card) {
        try (Connection con = getDataSource().getConnection();
             PreparedStatement pStatement =
                     con.prepareStatement("INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)")) {
            pStatement.setString(1, card.getNumber());
            pStatement.setString(2, card.getPin());
            pStatement.setInt(3, card.getBalance());
            pStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Данные не были добавлены");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param cardNumber номер карты, на которую нужно внести деньги
     * @param amount сумма, которую нужно внести на счет
     * @return получилось ли совершить операцию
     */
    public void addIncome(String cardNumber, int amount) {
        try (Connection con = getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pStatement = con.prepareStatement("UPDATE card SET balance = balance + ? WHERE number = ?")) {
                pStatement.setInt(1, amount);
                pStatement.setString(2, cardNumber);
                pStatement.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param cardNumberFrom номер карты, c которой нужно совершить перевод
     * @param cardNumberTo номер карты, на которую нужно совершить перевод
     * @param amount сумма, которую нужно перевести
     * @return результат операции
     */
    public String doTransfer(String cardNumberFrom, String cardNumberTo, int amount) {
        String result = "";
        if (getCardBalance(cardNumberFrom) < amount) {
            result = "Not enough money!";
        } else if (isCardExist(cardNumberTo)) {
            try (Connection con = getDataSource().getConnection()) {
                con.setAutoCommit(false);

                try (PreparedStatement pStatementFrom = con.prepareStatement("UPDATE card SET balance = balance - ? WHERE number = ?");
                     PreparedStatement pStatementTo = con.prepareStatement("UPDATE card SET balance = balance + ? WHERE number = ?")) {
                    pStatementFrom.setInt(1, amount);
                    pStatementFrom.setString(2, cardNumberFrom);
                    pStatementFrom.executeUpdate();

                    pStatementTo.setInt(1, amount);
                    pStatementTo.setString(2, cardNumberTo);
                    pStatementTo.executeUpdate();
                    con.commit();
                    result = "Success!";
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     *
     * @param cardNumber
     * @return result of checking the existence a card in the database
     */
    public boolean isCardExist(String cardNumber) {
        try (Connection con = getDataSource().getConnection();
             PreparedStatement pStatement = con.prepareStatement("SELECT * FROM card WHERE number = ?")) { // создаем соединение

            pStatement.setString(1, cardNumber);
            try (ResultSet cards = pStatement.executeQuery()) { // создаем объект ResultSet и присваиваем ему результат запроса данных из таблицы

                while (cards.next()) { // построчно  извлекаем данные из таблицы
                    String number = cards.getString("number");

                    if (cardNumber.equals(number)) { // проверяем на соответствие номер карты и если номер найден,
                        // заносим все данные этой записи в результирующий массив
                        return true;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param cardNumber номер карты
     * @return сумма на карте
     */
    public int getCardBalance(String cardNumber) {
        String data = "0";
        try (Connection con = getDataSource().getConnection();
             PreparedStatement pStatement = con.prepareStatement("SELECT * FROM card WHERE number = ?")) { // создаем соединение

            pStatement.setString(1, cardNumber);
            try (ResultSet cards = pStatement.executeQuery()) { // создаем объект ResultSet и присваиваем ему результат запроса данных из таблицы

                while (cards.next()) { // построчно  извлекаем данные из таблицы
                    String number = cards.getString("number");
                    int balance = cards.getInt("balance");

                    if (cardNumber.equals(number)) { // проверяем на соответствие номер карты и если номер найден,
                        // заосим все данные этой записи в результирующий массив
                        data = String.valueOf(balance);
                        break;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(data);
    }

    /**
     * @param cardNumber
     */
    public void deleteAccount(String cardNumber) {
        try (Connection con = getDataSource().getConnection();
             PreparedStatement pStatement = con.prepareStatement("DELETE FROM card WHERE number = ?")) {
            pStatement.setString(1, cardNumber);
            pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод принимает номер карты и пин, введенный пользователем,
     * ищет информацию об этой карте в БД и проверяет на соответствие введенных данных
     * @param cardNumber - номер карты
     * @param cardPin - пин-код карты
     * @return result - является ли введенная пользователем информация достоверной
     */
    public boolean isValidPin(String cardNumber, String cardPin) {
        boolean result = false;

        String[] card = getCardAsString(cardNumber);
        result = cardNumber.equals(card[1]) && cardPin.equals(card[2]);

        return result;
    }

    /**
     * Поиск записи в БД по номеру карты
     * @param cardNumber - номер карты
     * @return - информация о карте в виде массива строк: id, number, pin, balance
     */
    public String[] getCardAsString(String cardNumber) {
        String[] data = new String[4];
        try (Connection con = getDataSource().getConnection()) { // создаем соединение
            con.setAutoCommit(false);
            try (PreparedStatement pStatement = con.prepareStatement("SELECT * FROM card")) {
                try (ResultSet cards = pStatement.executeQuery()) { // создаем объект ResultSet и присваиваем ему результат запроса данных из таблицы

                    while (cards.next()) { // построчно  извлекаем данные из таблицы
                        int id = cards.getInt("id");
                        String number = cards.getString("number");
                        String pin = cards.getString("pin");
                        int balance = cards.getInt("balance");

                        if (cardNumber.equals(number)) { // проверяем на соответствие номер карты и если номер найден,
                            // заносим все данные этой записи в результирующий массив
                            data[0] = String.valueOf(id);
                            data[1] = number;
                            data[2] = pin;
                            data[3] = String.valueOf(balance);
                        }
                        con.commit();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}