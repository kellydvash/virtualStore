import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int userChoice = 0;
        List<Client> clients = new ArrayList<>();
        List<Worker> workers = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        VirtualStore virtualStore = new VirtualStore(clients, workers, products);

        do {
            System.out.println("Welcome to our store!");
            System.out.println("Please choose an option:");
            System.out.println("For create a new account please enter 1 ");
            System.out.println("For login to account please enter 2 ");
            System.out.println("For exit please enter 3 ");
            try {
                userChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("PLEASE ENTER A NUMBER");
                scanner.nextLine();
            }

            switch (userChoice) {
                case 1:
                    virtualStore.createAccount();
                    break;
                case 2:
                    String accountType;
                    boolean found = false;
                    boolean inValidInput = true;
                    do {
                        do {
                            System.out.println("Do you want enter to Client account or Worker account?");
                            accountType = scanner.nextLine();
                            if (!accountType.toUpperCase().equals("W") && !accountType.toUpperCase().equals("C")) {
                                System.out.println("INVALID INPUT!");
                            } else
                                inValidInput = false;
                        } while (inValidInput);
                        System.out.println("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.println("Enter password: ");
                        String password = scanner.nextLine();

                        if (accountType.toUpperCase().equals("W")) {
                            Worker worker = virtualStore.workerLoginAccount(username, password);
                            if (worker != null) {
                                found = true;
                            }
                        }
                        if (accountType.toUpperCase().equals("C")) {
                            Client client = virtualStore.clientLoginAccount(username, password);
                            if (client != null) {
                                found = true;
                            }
                        }
                    } while (!found);
            }
        } while (userChoice != 3);
    }
}