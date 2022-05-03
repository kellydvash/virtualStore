
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class VirtualStore {

    private List<Client> clientsList;
    private List<Worker> workerList;
    private List<Product> productList;
    private List<Product> productListInStock;

    public VirtualStore(List<Client> clientsList, List<Worker> workerList, List<Product> productList) {
        this.clientsList = clientsList;
        this.workerList = workerList;
        this.productList = productList;
        this.productListInStock = new ArrayList<>();
    }

    public void addClient(Client newClient) {
        this.clientsList.add(newClient);
    }

    public void addWorker(Worker newWorker) {
        this.workerList.add(newWorker);
    }

    public void createAccount() {
        String typeAccount;
        String userName;
        String lastName;
        String firstName;
        String password;
        String memberAns;
        int workerChoice = 0;
        boolean error;
        boolean member = false;
        Rank rank = null;
        Client newClient;

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Do you want to open a client account (enter C) or worker account (enter W)");
            typeAccount = scanner.nextLine();
        } while (!typeAccount.toUpperCase().equals("C") && !typeAccount.toUpperCase().equals("W"));
        do {
            System.out.println("Please enter your name");
            firstName = scanner.nextLine();
        } while (checkIfContainsNum(firstName));
        do {
            System.out.println("please enter your last name");
            lastName = scanner.nextLine();
        } while (checkIfContainsNum(lastName));
        do {
            System.out.println("Enter your user name");
            userName = scanner.nextLine();

            System.out.println("Enter your password");
            password = scanner.nextLine();
        } while ((ifPasswordValid(password) || (checkIfUserNameTaken(userName, typeAccount))));

        do {
            System.out.println("Do you a member? for YES enter - Y , for NO enter - N");
            memberAns = scanner.nextLine();
            if (memberAns.toUpperCase().equals("Y")) {
                member = true;
            }
        } while (!memberAns.toUpperCase().equals("Y") && !memberAns.toUpperCase().equals("N"));
        if (typeAccount.toUpperCase().equals("C")) {
            newClient = new Client(firstName, lastName, userName, password, member);
            addClient(newClient);
        }

        if (typeAccount.toUpperCase().equals("W")) {
            do {
                do {
                    System.out.println("If you regular worker - 1");
                    System.out.println("If you manager - 2");
                    System.out.println("If you friend in managment - 3");
                    try {
                        error = false;
                        workerChoice = scanner.nextInt();
                        scanner.nextLine();
                        if (workerChoice == 1) {
                            rank = Rank.REGULAR_WORKER;
                        } else if (workerChoice == 2) {
                            rank = Rank.MANAGER;
                        } else if (workerChoice == 3) {
                            rank = Rank.FRIEND_IN_MANAGMENT;
                        }

                        Worker newWorker = new Worker(firstName, lastName, userName, password, member, rank);
                        addWorker(newWorker);
                    } catch (Exception e) {
                        System.out.println("INVALID INPUT! PLEASE TRY AGAIN!");
                        scanner.nextLine();
                        error = true;
                    }
                } while (workerChoice > 3 || workerChoice < 1);
            } while (error);
        }
        System.out.println("YOUR ACCOUNT READY FOR YOU NOW! ");
        System.out.println("============================= \n");
    }

    public Worker workerLoginAccount(String username, String password) {
        Worker found = null;
        for (Worker currentUser : this.workerList) {
            if (currentUser.getUserName().equals(username) && currentUser.getPassword().equals(password)) {
                found = currentUser;
            }
            if (found != null) {
                workerMenu(found);
            } else {
                System.out.println("There is no such account!");
            }
        }
        return found;
    }

    public Client clientLoginAccount(String username, String password) {
        Client found = null;
        for (Client currentUser : this.clientsList) {
            if (currentUser.getUserName().equals(username) && currentUser.getPassword().equals(password)) {
                found = currentUser;
                break;
            }
        }
        if (found != null) {
            System.out.print("Hello " + found.getFirstName() + " " + found.getLastName());
            if (found.isMember())
                System.out.println("(VIP)");

            System.out.println("\n ======================== \n");
            purchase(found);
        } else {
            System.out.println("There is no such account!");
        }
        return found;
    }

    public void purchase(Client client) {
        Scanner scanner = new Scanner(System.in);
        CartShop cartShop = new CartShop();
        int productNum;
        int sumOfProduct;
        double updatedPrice;
        boolean invalidOption;
        do {
            do {
                invalidOption = false;
                printProductsInStock();
                System.out.println("Please enter the num of the product that you want to add to cart or to finish the shop enter [ -1 ]: ");
                productNum = scanner.nextInt();
                scanner.nextLine();
                if (productNum < -1 || productNum >= this.productList.size())
                    invalidOption = true;
            } while (invalidOption);

            if (productNum != -1) {
                do {
                    System.out.println("Please enter the sum of the product that you want: ");
                    sumOfProduct = scanner.nextInt();
                    if (sumOfProduct > 0) {
                        Product chosenProduct = this.productListInStock.get(productNum);
                        cartShop.addForProductList(chosenProduct);
                        if (client.isMember()) {
                            updatedPrice = updatedPriceForMemberClients(client, chosenProduct);
                        } else
                            updatedPrice = chosenProduct.getPrice();
                        cartShop.addForBill((updatedPrice * sumOfProduct));
                        System.out.println("The product added !");
                        System.out.println(cartShop.toString());
                    }else
                        System.out.println("PLEASE CHOOSE NUMBER UP TO 0");
                } while (sumOfProduct <= 0);
            }

        } while (productNum != -1);

        if (client instanceof Worker) {
            Worker worker = (Worker) client;
            double finalBillForWorker = worker.updateTheBillOfTheWorker(cartShop.getBill());
            cartShop.setBill(finalBillForWorker);
            this.clientsList.add(client);
        }
        System.out.println("====================================");
        System.out.println("YOUR FINAL BILL IS: " + cartShop.getBill());
        System.out.println("==================================== \n");
        client.updateTotalPaid(cartShop.getBill());
        client.updateTheLastDate();
        client.updateTheSumOfThePurchase();
    }
    public boolean checkIfContainsNum(String name) {
        for (int i = 0; i < name.length(); i++) {
            char currentChar = name.charAt(i);
            if (currentChar >= '0' && currentChar <= '9') {
                System.out.println("Please enter only letters!!");
                return true;
            }
        }
        return false;
    }

    public boolean ifPasswordValid(String password) {
        if (password.length() >= 6) {
            return false;
        } else {
            System.out.println("The password is not long enough!");
            return true;
        }
    }

    public boolean checkIfUserNameTaken(String newUserName, String typeAccount) {
        boolean exist = false;
        if (typeAccount.equals("C")) {
            for (Client currentBuyer : this.clientsList) {
                if (currentBuyer != null) {
                    if (currentBuyer.getUserName().equals(newUserName)) {
                        exist = true;
                        break;
                    }
                }
            }
        }
        if (typeAccount.equals("W")) {
            for (Worker currentWorker : this.workerList) {
                if (currentWorker != null) {
                    if (currentWorker.getUserName().equals(newUserName)) {
                        exist = true;
                        break;
                    }
                }
            }
        }
        if (exist) {
            System.out.println("This user name already taken!! ");
        }
        return exist;
    }

    public void workerMenu(Worker account) {
        Scanner scanner = new Scanner(System.in);
        boolean disconnected = true;
        int chosen;
        System.out.println("Hello " + account.getFirstName() + " " + account.getLastName() + " " + "(" + account.getRank() + ")");
        System.out.println("======================== \n");
        do {
            System.out.println("For print all the costumers enter - 1");
            System.out.println("For print all the member clients - 2");
            System.out.println("For print all the clients that bought one time meanwhile - 3");
            System.out.println("For print the client with the highest bill - 4");
            System.out.println("For add a new product - 5 ");
            System.out.println("For updating status for product - 6");
            System.out.println("For purchase - 7");
            System.out.println("For disconnected - 8");
            chosen = scanner.nextInt();

            switch (chosen) {
                case 1:
                    printAllClients();
                    break;
                case 2:
                    printMemberedClients();
                    break;
                case 3:
                    printClientsThatBoughtAtLeastOnce();
                    break;
                case 4:
                    printTheHighestClientPurchase();
                    break;
                case 5:
                    addProduct();
                    break;
                case 6:
                    updateStockProduct();
                    break;
                case 7:
                    purchase(account);
                    break;
                case 8:
                    disconnected = false;
                    break;
            }
        } while (disconnected);
    }

    public void updateStockProduct() {
        Scanner scanner = new Scanner(System.in);
        printProducts();
        System.out.println("Enter the num of the product that you want to update: ");
        int numOfProduct = scanner.nextInt();
        scanner.nextLine();
        System.out.println("If this product found in stock enter Y , if not enter N");
        String update = scanner.nextLine();
        Product chosenProduct = productList.get(numOfProduct - 1);
        if (update.toUpperCase().equals("Y")) {
            if (!this.productListInStock.contains(chosenProduct)) {
                chosenProduct.setInStock(true);
                this.productListInStock.add(chosenProduct);
            }
        } else if (update.toUpperCase().equals("N")) {
            chosenProduct.setInStock(false);
            this.productListInStock.remove(chosenProduct);
        } else
            System.out.println(" INVALID INPUT! Please try again ");
    }

    public void printAllClients() {
        System.out.println("=================================");
        System.out.println("ALL THE CLIENTS ARE: ");
        for (Client currentClient : this.clientsList) {
            System.out.println(currentClient.toString());
            System.out.println("------------------------------- \n");
        }
        System.out.println("================================= \n");
    }

    public void printMemberedClients() {
        System.out.println("=================================");
        System.out.println("THE MEMBER CLIENTS ARE: ");
        for (Client currentClient : this.clientsList) {
            if (currentClient.isMember()) {
                System.out.println(currentClient.toString());
                System.out.println("------------------------------ \n");
            }
        }
        System.out.println("================================= \n");
    }

    public void printClientsThatBoughtAtLeastOnce() {
        System.out.println("=================================");
        System.out.println("THE CLIENTS THAT BOUGHT AT LIST ONCE ARE: ");
        for (Client currentClient : this.clientsList) {
            if (currentClient.getTotalPurchase() != 0) {
                System.out.println(currentClient.toString());
                System.out.println("---------------------------- \n");
            }
        }
        System.out.println("================================= \n");
    }

    public void printTheHighestClientPurchase() {
        System.out.println("=================================");
        System.out.println("THE HIGHEST BUYER IS: ");
        if (clientsList.stream().max(Comparator.comparing(Client::getTotalPaid)).isPresent()) {
            Client highestBuyClient = clientsList.stream().max(Comparator.comparing(Client::getTotalPaid)).get();
            System.out.println(highestBuyClient.toString());
            System.out.println("================================= \n");
        }
    }

    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the description of the product: ");
        String description = scanner.nextLine();
        System.out.println("Please enter the price of the product: ");
        double price = scanner.nextDouble();
        System.out.println("Please enter the discount to members clients of the product: ");
        int discount = scanner.nextInt();
        Product newProduct = new Product(description, price, discount);
        productList.add(newProduct);
        productListInStock.add(newProduct);
        System.out.println("The product added!!");
        System.out.println("------------------------------------");
    }

    public void printProducts() {
        int count = 1;
        for (Product currentProduct : this.productList) {
            System.out.println(count + ". " + currentProduct.toString());
            count++;
        }
    }

    public void printProductsInStock() {
        int count = 0;
        for (Product currentProduct : this.productListInStock) {
            if (currentProduct.isInStock()) {
                System.out.println(count + ". " + currentProduct.toString());
                count++;
            }
        }
    }

    public double updatedPriceForMemberClients(Client client, Product product) {
        double finalPrice = 0;
        double precent = (100.0 - product.getDiscount()) / 100.0;
        if (client.isMember()) {
            finalPrice = product.getPrice() * precent; // CALCULATE THE FINAL PRICE AFTER PRESENTS DISCOUNT
        }
        return finalPrice;
    }


}

