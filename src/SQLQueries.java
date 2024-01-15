public class SQLQueries {
    // Insert user information
    public static final String INSERT_USER = "INSERT INTO user (first_name, last_name, Adhar_no, Account_No, email, password, phone_No, gender, Dob) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // Get user ID based on email and password
    public static final String GET_USER_ID = "SELECT sl_no FROM user WHERE email = ? AND password = ?;";

    // Delete user (close account)
    public static final String DELETE_USER = "DELETE FROM user WHERE sl_no = ?;";

    // Add a missing parameter for date in ADD_CREDIT query
    public static final String ADD_CREDIT = "INSERT INTO transaction (id, purpose, credit, date) VALUES (?, ?, ?, ?);";

    // Calculate total balance
    public static final String CALCULATE_TOTAL_BALANCE = "INSERT INTO total (id, total) VALUES (?, (SELECT COALESCE(SUM(credit), 0) - COALESCE(SUM(debit), 0) FROM transaction WHERE id = ?));";

    // Withdrawal transaction
    public static final String WITHDRAW = "INSERT INTO transaction (id, purpose, date, debit) VALUES (?, ?, NOW(), ?);";

    // Balance inquiry
    public static final String BALANCE_ENQUIRY = "SELECT date, purpose, debit, credit FROM transaction WHERE id = ?;";

    // Transfer funds
    public static final String TRANSFER_FUNDS = "INSERT INTO transaction (id, purpose, date, debit) VALUES (?, ?, NOW(), ?);";

    // Transaction history
    public static final String TRANSACTION_HISTORY = "SELECT date, purpose, debit, credit FROM transaction WHERE id = ?;";
}
