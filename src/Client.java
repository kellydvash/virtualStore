import java.time.LocalDate;


public class Client extends User{

    final int START_ACCOUNT_PURCHASE = 0;


    private boolean member;
    private double totalPaid;
    private int totalPurchase;
    private LocalDate lastDateOfShop;

    public Client(String firstName, String lastName, String userName, String password, boolean member) {
        super(firstName,lastName,userName,password);
        this.member = member;
        this.totalPurchase = START_ACCOUNT_PURCHASE;

        this.lastDateOfShop = LocalDate.ofEpochDay(1 / 2012);
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public void updateTheLastDate() {
        this.lastDateOfShop = LocalDate.now();
    }



    public void updateTheSumOfThePurchase() {
        this.totalPurchase = (this.totalPurchase + 1);
    }

    public String toString() {
        StringBuilder present = new StringBuilder();
        present.append(this.getFirstName()).append(" ").append(this.getLastName()) .append(" ");
        if (this.isMember()) {
            present.append("This client is Member  \n ");
        } else
            present.append("This client is not Member  \n ");
        present.append("The amount of purchase that the client done is: ").append(this.getTotalPurchase()).append(" \n");
        present.append("The sum of all the purchase that the client done is: ").append(this.getTotalPaid()).append(" \n");
        present.append("The last time this client done sopping is: ").append(this.lastDateOfShop);
        return String.valueOf(present);
    }

    public int getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(int totalPurchase) {
        this.totalPurchase = totalPurchase;
    }




    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public void updateTotalPaid(double bill){
        this.totalPaid = this.totalPaid + bill;
    }
}
