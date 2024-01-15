import java.util.Scanner;

public class Bank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hey, welcome to Bank");
        while (true) {
            System.out.println("Please select an option:");
            System.out.println("Enter 1 for Create Account");
            System.out.println("Enter 2 for Login");
            System.out.println("Enter 3 for Close Account");
            System.out.println("Enter 4 for Exit");

            int select = sc.nextInt();

            switch (select) {
                case 1:
                    Login.createAccount();
                    break;
                case 2:
                    Login.checkData();
                    break;
                case 3:
                    Login.closeAccount();
                    break;
                case 4:
                    System.out.println("Thank you");
                    System.exit(0);
                    sc.close();
                default:
                    System.out.println("Please Enter the Right option");
            }
        }
    }
}
