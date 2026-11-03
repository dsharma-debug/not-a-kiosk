import java.io.Serializable;
public class OrderRecord implements Reportable, Serializable {
    private int orderNumber;
    private String receiptText;
    private double total;
    private String feedback;
    public OrderRecord(int orderNumber, String receiptText, double total) {
        this.orderNumber = orderNumber;
        this.receiptText = receiptText;
        this.total = total;
        this.feedback = "No feedback submitted";
    }
    public int getOrderNumber() {
        return orderNumber;
    }
    public String getReceiptText() {
        return receiptText;
    }
    public double getTotal() {
        return total;
    }
    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    public String getReportText() {
        String text = "Order #" + orderNumber + "\n";
        text += receiptText;
        text += "Total: $" + String.format("%.2f", total) + "\n";
        text += "Feedback: " + feedback + "\n";
        return text;
    }
}
