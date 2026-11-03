import java.util.ArrayList;
public class KioskReports {
    public static String mainReport(ArrayList<Department> departments,
                                    ArrayList<OrderRecord> orderHistory,
                                    ArrayList<String> feedbackRecords,
                                    ArrayList<String> assistanceLogs) {
        String text = "Orders: " + orderHistory.size() + "\n";
        double totalSales = 0;
        Product mostExpensive = null;
        Product lowestStock = null;
        int unavailableCount = 0;
        for (OrderRecord order : orderHistory) {
            totalSales += order.getTotal();
        }
        text += "Total Sales: $" + String.format("%.2f", totalSales) + "\n\n";
        text += "Feedback Count: " + feedbackRecords.size() + "\n";
        text += "Assistance Log Count: " + assistanceLogs.size() + "\n\n";
        for (Department d : departments) {
            text += d.getReportText() + "\n";
            for (Product p : d.getProducts()) {
                if (mostExpensive == null || p.getPrice() > mostExpensive.getPrice()) {
                    mostExpensive = p;
                }
                if (lowestStock == null || p.getQuantityInStock() < lowestStock.getQuantityInStock()) {
                    lowestStock = p;
                }
                if (!p.isAvailable()) {
                    unavailableCount++;
                }
            }
        }
        if (mostExpensive != null) {
            text += "Most Expensive Product: " + mostExpensive.getName()
                    + " ($" + String.format("%.2f", mostExpensive.getPrice()) + ")\n";
        }
        if (lowestStock != null) {
            text += "Lowest Stock Product: " + lowestStock.getName()
                    + " (" + lowestStock.getQuantityInStock() + " left)\n";
        }
        return text + "Unavailable Products: " + unavailableCount + "\n";
    }
    public static String inventoryLine(Product p, int lowStockLimit) {
        String line = p.getProductCode() + " | " + p.getName()
                + " | $" + String.format("%.2f", p.getPrice())
                + " | Stock: " + p.getQuantityInStock();
        if (p.getQuantityInStock() <= lowStockLimit) {
            line += " | LOW STOCK";
        }
        if (!p.isAvailable()) {
            line += " | UNAVAILABLE";
        }
        return line;
    }
    public static String orderHistory(ArrayList<OrderRecord> orderHistory) {
        String text = "";
        for (OrderRecord o : orderHistory) {
            text += o.getReportText() + "\n";
        }
        return text.length() == 0 ? "No orders yet." : text;
    }
    public static String assistanceLogs(ArrayList<String> assistanceLogs) {
        String text = "";
        for (String s : assistanceLogs) {
            text += s + "\n";
        }
        return text.length() == 0 ? "No assistance logs yet." : text;
    }
    public static String feedback(ArrayList<String> feedbackRecords) {
        String text = "";
        for (String s : feedbackRecords) {
            text += "- " + s + "\n";
        }
        return text.length() == 0 ? "No feedback yet." : text;
    }
    public static String lowStock(ArrayList<Department> departments, int lowStockLimit) {
        String text = "";
        for (Department d : departments) {
            for (Product p : d.getProducts()) {
                if (p.getQuantityInStock() <= lowStockLimit) {
                    text += p.getName() + ": " + p.getQuantityInStock() + " left\n";
                }
            }
        }
        return text.length() == 0 ? "No low-stock products." : text;
    }
}
