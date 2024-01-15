import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Transaction {
    static Scanner scan = new Scanner(System.in);

    public static void option(int id) {
        while (true) {
            System.out.println("Please select a way of transaction");
            System.out.println("Type 1. For withdraw Amount");
            System.out.println("Type 2. For add money");
            System.out.println("Type 3. For Transfer Funds");
            System.out.println("Type 4. For Check balance");
            System.out.println("Type 5. For Transaction History");
            System.out.println("Type 6. Exit");
            System.out.print("Choice = ");
            int option = scan.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Please Enter Money to Withdraw");
                    double debit = scan.nextDouble();
                    withdraw(debit, id);
                    break;

                case 2:
                    System.out.println("Please Enter Money to Add");
                    double credit = scan.nextDouble();
                    add(credit, id);
                    break;

                case 3:
                    transferFunds(id);
                    break;

                case 4:
                    checkBalance(id);
                    break;

                case 5:
                    showTransactionHistory(id);
                    break;

                case 6:
                    System.out.println("Thank You");
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Please Try again.");
            }
        }
    }

    private static void checkBalance(int id) {
        try (Connection c = Connections.makeConnection()) {
            String query1 = SQLQueries.BALANCE_ENQUIRY;
            PreparedStatement p1 = c.prepareStatement(query1);
            p1.setInt(1, id);
            try (ResultSet rs = p1.executeQuery()) {
                while (rs.next()) {
                    System.out.println("[" + rs.getDate(1) + " " + rs.getString(2) + " " + rs.getDouble(3) + " "
                            + rs.getDouble(4) + "]");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    private static void add(double credit, int id) {
        try (Connection c = Connections.makeConnection()) {
            System.out.print("Purpose:");
            String purpose = scan.next();

            // Add credit transaction
            String query1 = SQLQueries.ADD_CREDIT;
            try (PreparedStatement p1 = c.prepareStatement(query1)) {
                p1.setInt(1, id);
                p1.setString(2, purpose);
                p1.setDouble(3, credit);
                p1.execute();
            }

            // Calculate total balance
            String query2 = SQLQueries.CALCULATE_TOTAL_BALANCE;
            try (PreparedStatement p2 = c.prepareStatement(query2)) {
                p2.setInt(1, id);
                p2.execute();
            }

            System.out.println("Amount added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding money: " + e.getMessage());
        }
    }



    private static void withdraw(double debit, int id) {
        try (Connection c = Connections.makeConnection()) {
            System.out.print("Purpose:");
            String purpose = scan.next();

            String query1 = SQLQueries.WITHDRAW;
            PreparedStatement p1 = c.prepareStatement(query1);
            p1.setInt(1, id);
            p1.setString(2, purpose);
            p1.setDouble(3, debit);
            p1.execute();

            String query2 = SQLQueries.CALCULATE_TOTAL_BALANCE;
            PreparedStatement p2 = c.prepareStatement(query2);
            p2.setInt(1, id);
            p2.execute();

            System.out.println("Amount withdrawn successfully.");
        } catch (SQLException e) {
            System.out.println("Error withdrawing money: " + e.getMessage());
        }
    }

    private static void transferFunds(int id) {
        try (Connection c = Connections.makeConnection()) {
            System.out.println("Enter the recipient's Account Number:");
            int recipientAccountNumber = scan.nextInt();
            System.out.println("Enter the amount to transfer:");
            double transferAmount = scan.nextDouble();

            // Check if recipient's account exists
            String queryCheckRecipient = "SELECT sl_no FROM user WHERE Account_No = ?;";
            PreparedStatement pstmtCheckRecipient = c.prepareStatement(queryCheckRecipient);
            pstmtCheckRecipient.setInt(1, recipientAccountNumber);
            ResultSet rsRecipient = pstmtCheckRecipient.executeQuery();

            if (rsRecipient.next()) {
                // Recipient's account found, proceed with transfer
                int recipientId = rsRecipient.getInt(1);

                // Deduct from sender's account
                String queryDeduct = SQLQueries.WITHDRAW;
                PreparedStatement pstmtDeduct = c.prepareStatement(queryDeduct);
                pstmtDeduct.setInt(1, id);
                pstmtDeduct.setString(2, "Transfer to " + recipientAccountNumber);
                pstmtDeduct.setDouble(3, transferAmount);
                pstmtDeduct.execute();

                // Add to recipient's account
                String queryAdd = SQLQueries.ADD_CREDIT;
                PreparedStatement pstmtAdd = c.prepareStatement(queryAdd);
                pstmtAdd.setInt(1, recipientId);
                pstmtAdd.setString(2, "Transfer from " + id);
                pstmtAdd.setDouble(3, transferAmount);
                pstmtAdd.execute();

                // Update total balance for both accounts
                String queryUpdateSender = SQLQueries.CALCULATE_TOTAL_BALANCE;
                PreparedStatement pstmtUpdateSender = c.prepareStatement(queryUpdateSender);
                pstmtUpdateSender.setInt(1, id);
                pstmtUpdateSender.execute();

                String queryUpdateRecipient = SQLQueries.CALCULATE_TOTAL_BALANCE;
                PreparedStatement pstmtUpdateRecipient = c.prepareStatement(queryUpdateRecipient);
                pstmtUpdateRecipient.setInt(1, recipientId);
                pstmtUpdateRecipient.execute();

                System.out.println("Funds transferred successfully.");
            } else {
                System.out.println("Recipient's account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error transferring funds: " + e.getMessage());
        }
    }

    private static void showTransactionHistory(int id) {
        try (Connection c = Connections.makeConnection()) {
            String query = SQLQueries.TRANSACTION_HISTORY;
            PreparedStatement pstmt = c.prepareStatement(query);
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(
                            "[" + rs.getDate(1) + " " + rs.getString(2) + " " + rs.getDouble(3) + " " + rs.getDouble(4) + "]");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
    }
}
