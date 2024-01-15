import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    private static final Scanner sc = new Scanner(System.in);

    public static void createAccount() {
        try {
            Connection connection = Connections.makeConnection();
            if (connection != null) {
                String firstname, lastname, email, password, gender, dob;
                long mobileno, adharno;

                System.out.println("Enter first name");
                firstname = sc.next();
                System.out.println("Enter Last name");
                lastname = sc.next();
                mobileno = validateMobileNumber();
                email = validateEmail();
                adharno = validateAdharNumber();
                System.out.println("Enter Password");
                password = sc.next();
                gender = (sc.nextByte() == 1) ? "male" : "female";
                System.out.println("Enter date of birth (yyyy-mm-dd)");
                dob = sc.next();

                String query = SQLQueries.INSERT_USER;
                try (PreparedStatement p = connection.prepareStatement(query)) {
                    p.setString(1, firstname);
                    p.setString(2, lastname);
                    p.setLong(7, mobileno);
                    p.setString(5, email);
                    p.setLong(3, adharno);
                    int accountNo = generateRandomAccountNumber();
                    p.setLong(4, accountNo);
                    p.setString(6, password);
                    p.setString(9, dob);
                    p.setString(8, gender);
                    p.execute();
                    System.out.println("Please Note your Account Number=" + accountNo);
                }

                System.out.println("Your Account created Successfully");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong. Please check your input. Error: " + e.getMessage());
            createAccount();
        }
    }

    public static void closeAccount() {
        System.out.println("Are you sure you want to close your account? (Y/N)");
        String confirmation = sc.next().trim().toLowerCase();

        if (confirmation.equals("y")) {
            try {
                Connection connection = Connections.makeConnection();
                if (connection != null) {
                    System.out.println("Enter user id");
                    int userId = sc.nextInt();

                    String query = SQLQueries.DELETE_USER;
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, userId);
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Account closed successfully. Thank you for being with us.");
                            System.exit(0);
                        } else {
                            System.out.println("Failed to close account. Please try again.");
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error closing account: " + e.getMessage());
            }
        } else {
            System.out.println("Account closure cancelled.");
        }
    }

    public static void checkData() {
        System.out.println("Enter email");
        String email = sc.next();
        System.out.println("Enter Password");
        String password = sc.next();

        try {
            Connection connection = Connections.makeConnection();
            if (connection != null) {
                String query = SQLQueries.GET_USER_ID;
                try (PreparedStatement p = connection.prepareStatement(query)) {
                    p.setString(1, email);
                    p.setString(2, password);
                    try (ResultSet resultSet = p.executeQuery()) {
                        if (resultSet.next()) {
                            int userId = resultSet.getInt("sl_no");
                            Transaction.option(userId);
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking login credentials: " + e.getMessage());
        }
    }

    private static long validateMobileNumber() {
        long mobileno = 0;
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("Enter mobile number:");
            mobileno = sc.nextLong();
            if (isValidMobileNumber(mobileno)) {
                break;
            } else {
                System.out.println("Please enter a valid mobile number.");
                attempts--;
            }
        }
        return mobileno;
    }

    private static boolean isValidMobileNumber(long mobileno) {
        return mobileno > 6111111111L && mobileno < 9999999999L;
    }

    private static String validateEmail() {
        String email = "";
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("Enter email:");
            email = sc.next();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Please enter a valid email id.");
                attempts--;
            }
        }
        return email;
    }

    private static boolean isValidEmail(String email) {
        return email.contains("@gmail.com");
    }

    private static long validateAdharNumber() {
        long adharno = 0;
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("Enter Adhar number:");
            adharno = sc.nextLong();
            if (isValidAdharNumber(adharno)) {
                break;
            } else {
                System.out.println("Please enter a valid Adhar number.");
                attempts--;
            }
        }
        return adharno;
    }

    private static boolean isValidAdharNumber(long adharno) {
        return adharno > 100000000000L && adharno < 999999999999L;
    }

    private static int generateRandomAccountNumber() {
        return 100000 + (int) (Math.random() * 900000);
    }
}
