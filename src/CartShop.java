
import java.util.HashSet;
public class CartShop {
    private final int START_SHOP = 0;
    private HashSet <Product> productList;
    private double bill;

    public CartShop() {
        this.bill = START_SHOP;
        this.productList = new HashSet<>();

    }

    public int getSTART_SHOP() {
        return START_SHOP;
    }

    public HashSet<Product> getProductList() {
        return productList;
    }

    public void setProductList(HashSet<Product> productList) {
        this.productList = productList;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public void addForBill(double price) {
        this.bill = this.bill + price;
    }

    public void addForProductList(Product product) {
        this.productList.add(product);
    }

    public String toString() {
        StringBuilder cart = new StringBuilder();
        cart.append("YOUR CART IS: \n");
        for (Product currentProduct : this.productList) {
            cart.append(currentProduct.toString()).append(" \n");
        }
        cart.append("=======================\n");
        cart.append("YOUR BILL UNTIL NOW IS: ").append(getBill()).append("\n");
        cart.append("=======================\n");
        return cart.toString();
    }
}

