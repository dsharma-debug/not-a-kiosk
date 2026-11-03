import java.io.Serializable;
public class Product extends KioskEntity implements Sellable, Displayable, Reportable, Serializable {
    private String description;
    private double price;
    private int quantityInStock;
    private boolean available;
    public Product(String code, String name, String description, double price, int quantityInStock) {
        super(code, name);
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.available = quantityInStock > 0;
    }
    public String getProductCode() {
        return getCode();
    }
    public String getName() {
        return getNameInternal();
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantityInStock() {
        return quantityInStock;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setName(String name) {
        setNameInternal(name);
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
        if (quantityInStock <= 0) {
            this.available = false;
        }
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void toggleAvailable() {
        this.available = !this.available;
    }
    public String getInventoryText() {
        if (!available) {
            return "N/A";
        }
        return "" + quantityInStock;
    }
    public void validateOrderQuantity(int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (!available) {
            throw new InvalidQuantityException("Product is not available.");
        }
        if (quantity > quantityInStock) {
            throw new InvalidQuantityException("Quantity exceeds stock.");
        }
    }
    public String getDisplayText() {
        String text = getProductCode() + " - " + getName() + "\n";
        text += description + "\n";
        text += "Price: $" + String.format("%.2f", price) + "\n";
        text += "Stock: " + getInventoryText() + "\n";
        return text;
    }
    public String getReportText() {
        String text = "Product: " + getName() + "\n";
        text += "Code: " + getProductCode() + "\n";
        text += "Price: $" + String.format("%.2f", price) + "\n";
        text += "Stock: " + quantityInStock + "\n";
        text += "Available: " + available + "\n";
        return text;
    }
}
