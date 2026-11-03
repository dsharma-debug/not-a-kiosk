import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private ArrayList<CartItem> items;

    public Cart() {
        items = new ArrayList<CartItem>();
    }

    public void addItem(Product product, int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (!product.isAvailable()) {
            throw new InvalidQuantityException("This product is not available.");
        }
        if (quantity > product.getQuantityInStock()) {
            throw new InvalidQuantityException("Requested quantity exceeds available stock.");
        }
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProduct().getProductCode().equals(product.getProductCode())) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > product.getQuantityInStock()) {
                    throw new InvalidQuantityException("Total quantity exceeds available stock.");
                }
                item.setQuantity(newQuantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += items.get(i).getTotalPrice();
        }
        return subtotal;
    }

    public double getTax() {
        return getSubtotal() * 0.08875; // Assuming a tax rate of 8.875% NY sales tax
    }

    public double getTotal() {
        return getSubtotal() + getTax();
    }

    public String getReceipt() {
        String receipt = "";
        if (items.size() == 0) {
            return "Cart is empty.\n";
        }
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            receipt += item.getProduct().getName()
                    + " x" + item.getQuantity()
                    + " - $" + String.format("%.2f", item.getTotalPrice()) + "\n";
        }
        receipt += "\nSubtotal: $" + String.format("%.2f", getSubtotal()) + "\n";
        receipt += "Tax: $" + String.format("%.2f", getTax()) + "\n";
        receipt += "Total: $" + String.format("%.2f", getTotal()) + "\n";
        return receipt;
    }

    public void completeOrder() {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            Product product = item.getProduct();
            int remaining = product.getQuantityInStock() - item.getQuantity();
            product.setQuantityInStock(remaining);
            if (remaining == 0) {
                product.setAvailable(false);
            }
        }
        items.clear();
    }

    public boolean isEmpty() {
        return items.size() == 0;
    }
}
