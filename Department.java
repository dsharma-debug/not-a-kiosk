import java.io.Serializable;
import java.util.ArrayList;
public class Department extends KioskEntity implements Displayable, Reportable, Serializable {
    private ArrayList<Product> products;
    public Department(String code, String name) {
        super(code, name);
        products = new ArrayList<Product>();
    }
    public String getDepartmentCode() {
        return getCode();
    }
    public String getDepartmentName() {
        return getNameInternal();
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public void addProduct(Product product) {
        products.add(product);
    }
    public String getDisplayText() {
        String text = "Department: " + getDepartmentName() + "\n";
        for (int i = 0; i < products.size(); i++) {
            text += products.get(i).getDisplayText() + "\n";
        }
        return text;
    }
    public String getReportText() {
        String text = "Department: " + getDepartmentName() + "\n";
        text += "Total Products: " + products.size() + "\n";
        int availableCount = 0;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).isAvailable()) {
                availableCount++;
            }
        }
        text += "Available Products: " + availableCount + "\n";
        return text;
    }
}
