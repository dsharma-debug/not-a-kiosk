import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class NotAKioskApp extends Application {
    private Stage mainStage;
    private ArrayList<Department> departments = new ArrayList<>();
    private Cart cart = new Cart();
    private ArrayList<String> feedbackRecords = new ArrayList<>();
    private ArrayList<OrderRecord> orderHistory = new ArrayList<>();
    private ArrayList<String> assistanceLogs = new ArrayList<>();
    private String managerPassword = "admin123";
    private String welcomeMessage = "Welcome to Not a Kiosk";
    private int nextOrderNumber = 1001;
    private OrderRecord lastOrder = null;
    private long screenStartTime = 0;
    private final long STUCK_LIMIT = 15000;
    private final int LOW_STOCK_LIMIT = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Not a Kiosk");
        loadSampleData();
        showWelcomeScreen();
        mainStage.show();
    }

    private void loadSampleData() {
        departments = SampleCatalog.createDepartments();
    }

    private void startScreenTimer() {
        screenStartTime = System.currentTimeMillis();
    }

    private boolean checkForStuckUser(String screen) {
        long time = System.currentTimeMillis() - screenStartTime;
        if (time >= STUCK_LIMIT) {
            assistanceLogs.add("User may have been stuck on " + screen + " for " + (time / 1000) + " seconds.");
            return true;
        }
        return false;
    }

    private String stuckHelpText(String screen) {
        return "Need help?\n\n"
                + "You spent more than 15 seconds on " + screen + ".\n\n"
                + "Try the search box, choose a department, check product details, go back, or press HELP for the full kiosk guide.";
    }

    private void setScene(VBox r) {
        mainStage.setScene(KioskUI.scene(r));
    }

    private void addBackButton(VBox r, Runnable action) {
        Button back = KioskUI.secondaryButton("BACK");
        back.setOnAction(e -> action.run());
        r.getChildren().add(back);
    }

    private void addHelpButton(VBox r, Runnable returnAction) {
        Button help = KioskUI.secondaryButton("HELP");
        help.setOnAction(e -> showHelpScreen(returnAction));
        r.getChildren().add(help);
    }

    private void showHelpScreen(Runnable returnAction) {
        VBox r = KioskUI.root("Kiosk Help");
        VBox details = KioskUI.card();
        details.setMaxWidth(660);
        details.getChildren().add(KioskUI.readOnlyArea(getKioskHelpText()));
        Button back = KioskUI.menuButton("BACK");
        back.setOnAction(e -> returnAction.run());
        details.getChildren().add(back);
        r.getChildren().add(details);
        setScene(r);
    }

    private String getKioskHelpText() {
        String text = "How to use this kiosk\n\n";
        text += "1. Use the search box if you know what you want.\n";
        text += "2. Search can match product names or descriptions.\n";
        text += "3. Try words like vegan, spicy, cheap, drink, cola, jacket, coffee, salad, or fries.\n";
        text += "4. You can also browse by department and choose a product tile.\n";
        text += "5. Open a product to see its price, stock, and description.\n";
        text += "6. Enter a quantity, add it to the cart, then checkout.\n";
        text += "7. Checkout shows subtotal, tax, and total before payment.\n\n";
        text += "Available products\n\n";
        for (Department d : departments) {
            text += d.getDepartmentName() + "\n";
            for (Product p : d.getProducts()) {
                text += "- " + p.getName()
                        + " | " + p.getDescription()
                        + " | $" + String.format("%.2f", p.getPrice())
                        + " | Stock: " + p.getInventoryText();
                if (!p.isAvailable()) {
                    text += " | Unavailable";
                }
                text += "\n";
            }
            text += "\n";
        }
        return text;
    }

    private void showWelcomeScreen() {
        VBox r = KioskUI.root(welcomeMessage);
        VBox options = KioskUI.card();
        Button guest = KioskUI.menuButton("GUEST ORDER");
        Button manager = KioskUI.secondaryButton("MANAGER");
        Button exit = KioskUI.secondaryButton("EXIT");
        guest.setOnAction(e -> showCustomerStartScreen());
        manager.setOnAction(e -> showManagerLoginScreen());
        exit.setOnAction(e -> Platform.exit());
        options.getChildren().addAll(
                KioskUI.bodyLabel("Start an order, search items, browse departments, or sign in as manager."),
                guest,
                manager,
                exit);
        r.getChildren().add(options);
        addHelpButton(r, this::showWelcomeScreen);
        setScene(r);
    }

    private void showCustomerStartScreen() {
        VBox r = KioskUI.root("Customer Menu");
        VBox options = KioskUI.card();
        TextField search = new TextField();
        search.setPromptText("Try: oily, vegan, spicy, cheap, school, dry hair, cleaning");
        KioskUI.styleInput(search);
        Button searchBtn = KioskUI.menuButton("SEARCH");
        searchBtn.setOnAction(e -> showAssistantSearchResults(search.getText()));
        Button browse = KioskUI.secondaryButton("BROWSE DEPARTMENTS");
        browse.setOnAction(e -> showDepartmentSelection());
        Button checkout = KioskUI.secondaryButton("CHECKOUT");
        checkout.setOnAction(e -> showCheckoutPage());
        options.getChildren().addAll(search, searchBtn, browse, checkout);
        r.getChildren().add(options);
        addHelpButton(r, this::showCustomerStartScreen);
        addBackButton(r, this::showWelcomeScreen);
        setScene(r);
    }

    private void showAssistantSearchResults(String text) {
        startScreenTimer();
        VBox r = KioskUI.root("Search Results");
        GridPane grid = KioskUI.productGrid();
        String query = text == null ? "" : text.trim().toLowerCase();
        int index = 0;
        for (Department d : departments) {
            for (Product p : d.getProducts()) {
                if (KioskSearch.matches(p, query)) {
                    grid.add(productButton(d, p, "Search"), index % 3, index / 3);
                    index++;
                }
            }
        }
        if (index == 0) {
            r.getChildren().add(KioskUI.bodyLabel("No products matched your search."));
        } else {
            r.getChildren().add(grid);
        }
        addBackButton(r, this::showCustomerStartScreen);
        addHelpButton(r, () -> showAssistantSearchResults(text));
        setScene(r);
    }

    private void showDepartmentSelection() {
        startScreenTimer();
        VBox r = KioskUI.root("Choose a Department");
        VBox options = KioskUI.card();
        for (Department d : departments) {
            Button b = KioskUI.menuButton(d.getDepartmentName());
            b.setOnAction(e -> {
                if (checkForStuckUser("Department Selection")) {
                    showMessage(stuckHelpText("Department Selection"), () -> showProductGrid(d));
                    return;
                }
                showProductGrid(d);
            });
            options.getChildren().add(b);
        }
        r.getChildren().add(options);
        addHelpButton(r, this::showDepartmentSelection);
        addBackButton(r, this::showCustomerStartScreen);
        setScene(r);
    }

    private void showProductGrid(Department d) {
        startScreenTimer();
        VBox r = KioskUI.root(d.getDepartmentName());
        GridPane grid = KioskUI.productGrid();
        for (int i = 0; i < d.getProducts().size(); i++) {
            Product p = d.getProducts().get(i);
            grid.add(productButton(d, p, "Product Grid"), i % 3, i / 3);
        }
        r.getChildren().add(grid);
        addHelpButton(r, () -> showProductGrid(d));
        addBackButton(r, this::showDepartmentSelection);
        setScene(r);
    }

    private Button productButton(Department d, Product p, String screenName) {
        String status = p.isAvailable() ? "$" + String.format("%.2f", p.getPrice()) : "Unavailable";
        Button b = KioskUI.menuButton(p.getName() + "\n" + status);
        b.setPrefSize(230, 72);
        b.setOnAction(e -> {
            if (checkForStuckUser(screenName)) {
                showMessage(stuckHelpText(screenName), () -> showProductDetailPage(d, p));
                return;
            }
            showProductDetailPage(d, p);
        });
        return b;
    }

    private void showProductDetailPage(Department d, Product p) {
        startScreenTimer();
        VBox r = KioskUI.root(p.getName());
        VBox details = KioskUI.card();
        Label productInfo = KioskUI.bodyLabel(
                p.getDescription() + "\n\nPrice: $" + String.format("%.2f", p.getPrice())
                        + "\nStock: " + p.getInventoryText());
        TextField qty = new TextField("1");
        qty.setPromptText("Quantity");
        qty.setMaxWidth(160);
        KioskUI.styleInput(qty);
        Button add = KioskUI.menuButton("ADD TO CART");
        Button checkout = KioskUI.secondaryButton("CHECKOUT");
        Button back = KioskUI.secondaryButton("BACK TO PRODUCTS");
        add.setOnAction(e -> {
            if (checkForStuckUser("Product Detail")) {
                showMessage(stuckHelpText("Product Detail"), () -> showProductDetailPage(d, p));
                return;
            }
            try {
                int q = Integer.parseInt(qty.getText());
                p.validateOrderQuantity(q);
                cart.addItem(p, q);
                showMessage("Added to cart:\n" + p.getName() + " x" + q, () -> showProductGrid(d));
            } catch (Exception ex) {
                showMessage("Invalid quantity. Please enter a number within the available stock.",
                        () -> showProductDetailPage(d, p));
            }
        });
        checkout.setOnAction(e -> {
            if (checkForStuckUser("Product Detail")) {
                showMessage(stuckHelpText("Product Detail"), this::showCheckoutPage);
                return;
            }
            showCheckoutPage();
        });
        back.setOnAction(e -> showProductGrid(d));
        details.getChildren().addAll(productInfo, qty, add, checkout, back);
        r.getChildren().add(details);
        addHelpButton(r, () -> showProductDetailPage(d, p));
        setScene(r);
    }

    private void showCheckoutPage() {
        startScreenTimer();
        VBox r = KioskUI.root("Checkout");
        VBox details = KioskUI.card();
        String checkoutText = cart.getReceipt();
        if (!cart.isEmpty()) {
            checkoutText = "Review before payment:\n\n" + checkoutText;
        }
        TextArea receipt = KioskUI.readOnlyArea(checkoutText);
        Button pay = KioskUI.menuButton("PAY");
        Button back = KioskUI.secondaryButton("CONTINUE SHOPPING");
        pay.setOnAction(e -> {
            if (checkForStuckUser("Checkout")) {
                showMessage(stuckHelpText("Checkout"), this::showCheckoutPage);
                return;
            }
            if (cart.isEmpty()) {
                showMessage("Cart empty. Add an item before paying.", this::showCustomerStartScreen);
                return;
            }
            int id = nextOrderNumber++;
            lastOrder = new OrderRecord(id, cart.getReceipt(), cart.getTotal());
            orderHistory.add(lastOrder);
            cart.completeOrder();
            showOrderConfirmationPage(lastOrder);
        });
        back.setOnAction(e -> showCustomerStartScreen());
        details.getChildren().addAll(receipt, pay, back);
        r.getChildren().add(details);
        addHelpButton(r, this::showCheckoutPage);
        setScene(r);
    }

    private void showOrderConfirmationPage(OrderRecord o) {
        VBox r = KioskUI.root("Order Confirmed");
        VBox details = KioskUI.card();
        Button review = KioskUI.menuButton("LEAVE REVIEW");
        Button finish = KioskUI.secondaryButton("FINISH");
        review.setOnAction(e -> showReviewPage());
        finish.setOnAction(e -> {
            cart = new Cart();
            showWelcomeScreen();
        });
        details.getChildren().addAll(
                KioskUI.bodyLabel("Payment Complete\nYour Order Number: " + o.getOrderNumber()
                        + "\nPlease save this number or wait for it to be called."),
                KioskUI.readOnlyArea(o.getReportText()),
                review,
                finish);
        r.getChildren().add(details);
        setScene(r);
    }

    private void showReviewPage() {
        VBox r = KioskUI.root("Review Your Order");
        VBox details = KioskUI.card();
        TextArea input = new TextArea();
        input.setPromptText("Tell us how we did");
        input.setPrefRowCount(5);
        KioskUI.styleArea(input);
        Button submit = KioskUI.menuButton("SUBMIT REVIEW");
        submit.setOnAction(e -> {
            String f = input.getText().trim();
            if (f.length() == 0) {
                showMessage("Review cannot be empty.", this::showReviewPage);
                return;
            }
            feedbackRecords.add(f);
            if (lastOrder != null) {
                lastOrder.setFeedback(f);
            }
            showWelcomeScreen();
        });
        details.getChildren().addAll(input, submit);
        r.getChildren().add(details);
        setScene(r);
    }

    private void showManagerLoginScreen() {
        VBox r = KioskUI.root("Manager Login");
        VBox details = KioskUI.card();
        PasswordField p = new PasswordField();
        p.setPromptText("Password");
        KioskUI.styleInput(p);
        Button login = KioskUI.menuButton("LOGIN");
        Label hint = KioskUI.bodyLabel("Hint: password is " + managerPassword);
        login.setOnAction(e -> {
            if (p.getText().equals(managerPassword)) {
                showManagerMenu();
            } else {
                showMessage("Wrong password", this::showManagerLoginScreen);
            }
        });
        details.getChildren().addAll(p, login, hint);
        r.getChildren().add(details);
        addBackButton(r, this::showWelcomeScreen);
        setScene(r);
    }

    private void showManagerMenu() {
        VBox r = KioskUI.root("Manager Menu");
        VBox options = KioskUI.card();
        Button report = KioskUI.menuButton("REPORT");
        Button inventory = KioskUI.secondaryButton("INVENTORY");
        Button editWelcome = KioskUI.secondaryButton("EDIT HOME MESSAGE");
        Button changePassword = KioskUI.secondaryButton("CHANGE PASSWORD");
        Button more = KioskUI.secondaryButton("MORE FUNCTIONS");
        Button logout = KioskUI.secondaryButton("LOG OUT");
        report.setOnAction(e -> showReport());
        inventory.setOnAction(e -> showInventoryScreen());
        editWelcome.setOnAction(e -> showEditWelcomeMessage());
        changePassword.setOnAction(e -> showChangePasswordScreen());
        more.setOnAction(e -> showMoreFunctions());
        logout.setOnAction(e -> showWelcomeScreen());
        options.getChildren().addAll(report, inventory, editWelcome, changePassword, more, logout);
        r.getChildren().add(options);
        setScene(r);
    }

    private void showReport() {
        showMessage(
                KioskReports.mainReport(departments, orderHistory, feedbackRecords, assistanceLogs),
                this::showManagerMenu);
    }

    private void showInventoryScreen() {
        startScreenTimer();
        VBox r = KioskUI.root("Inventory");
        VBox list = KioskUI.card();
        list.setMaxWidth(820);
        for (Department d : departments) {
            Label departmentLabel = KioskUI.bodyLabel(d.getDepartmentName());
            departmentLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 18px; -fx-font-weight: bold;"
                    + " -fx-text-fill: #facc15;"
                    + " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 5, 0.2, 0, 1);");
            list.getChildren().add(departmentLabel);
            for (Product p : d.getProducts()) {
                Button product = KioskUI.secondaryButton(KioskReports.inventoryLine(p, LOW_STOCK_LIMIT));
                product.setOnAction(e -> {
                    if (checkForStuckUser("Inventory")) {
                        showMessage(stuckHelpText("Inventory"), () -> showEditProductScreen(d, p));
                        return;
                    }
                    showEditProductScreen(d, p);
                });
                list.getChildren().add(product);
            }
        }
        r.getChildren().add(list);
        addBackButton(r, this::showManagerMenu);
        setScene(r);
    }

    private void showEditProductScreen(Department d, Product p) {
        startScreenTimer();
        VBox r = KioskUI.root("Edit Product");
        VBox form = KioskUI.card();
        TextField name = new TextField(p.getName());
        TextField description = new TextField(p.getDescription());
        TextField price = new TextField("" + p.getPrice());
        TextField stock = new TextField("" + p.getQuantityInStock());
        name.setPromptText("Product name");
        description.setPromptText("Description");
        price.setPromptText("Price");
        stock.setPromptText("Stock");
        KioskUI.styleInput(name);
        KioskUI.styleInput(description);
        KioskUI.styleInput(price);
        KioskUI.styleInput(stock);
        Button save = KioskUI.menuButton("SAVE PRODUCT");
        Button toggle = KioskUI.secondaryButton(p.isAvailable() ? "MARK UNAVAILABLE" : "MARK AVAILABLE");
        Button cancel = KioskUI.secondaryButton("CANCEL");
        save.setOnAction(e -> {
            if (checkForStuckUser("Manager Edit Product")) {
                showMessage(stuckHelpText("Manager Edit Product"), () -> showEditProductScreen(d, p));
                return;
            }
            try {
                String newName = name.getText().trim();
                String newDescription = description.getText().trim();
                double newPrice = Double.parseDouble(price.getText().trim());
                int newStock = Integer.parseInt(stock.getText().trim());
                if (newName.length() == 0) {
                    showMessage("Product name cannot be empty.", () -> showEditProductScreen(d, p));
                    return;
                }
                if (newDescription.length() == 0) {
                    showMessage("Description cannot be empty.", () -> showEditProductScreen(d, p));
                    return;
                }
                if (newPrice < 0) {
                    showMessage("Price cannot be below 0.", () -> showEditProductScreen(d, p));
                    return;
                }
                if (newStock < 0) {
                    showMessage("Stock cannot be below 0.", () -> showEditProductScreen(d, p));
                    return;
                }
                p.setName(newName);
                p.setDescription(newDescription);
                p.setPrice(newPrice);
                p.setQuantityInStock(newStock);
                showMessage("Product updated and saved.", this::showInventoryScreen);
            } catch (Exception ex) {
                showMessage("Use a valid number for price and stock.", () -> showEditProductScreen(d, p));
            }
        });
        toggle.setOnAction(e -> {
            if (checkForStuckUser("Manager Edit Product")) {
                showMessage(stuckHelpText("Manager Edit Product"), () -> showEditProductScreen(d, p));
                return;
            }
            p.toggleAvailable();
            showEditProductScreen(d, p);
        });
        cancel.setOnAction(e -> showInventoryScreen());
        form.getChildren().addAll(
                KioskUI.bodyLabel("Department: " + d.getDepartmentName()),
                name,
                description,
                price,
                stock,
                save,
                toggle,
                cancel);
        r.getChildren().add(form);
        setScene(r);
    }

    private void showEditWelcomeMessage() {
        VBox r = KioskUI.root("Edit Home Message");
        VBox form = KioskUI.card();
        TextArea message = new TextArea(welcomeMessage);
        message.setWrapText(true);
        message.setPrefRowCount(4);
        KioskUI.styleArea(message);
        Button save = KioskUI.menuButton("SAVE MESSAGE");
        Button cancel = KioskUI.secondaryButton("CANCEL");
        save.setOnAction(e -> {
            String newMessage = message.getText().trim();
            if (newMessage.length() == 0) {
                showMessage("Home message cannot be empty.", this::showEditWelcomeMessage);
                return;
            }
            welcomeMessage = newMessage;
            showMessage("Home message updated and saved.", this::showManagerMenu);
        });
        cancel.setOnAction(e -> showManagerMenu());
        form.getChildren().addAll(message, save, cancel);
        r.getChildren().add(form);
        setScene(r);
    }

    private void showChangePasswordScreen() {
        VBox r = KioskUI.root("Change Password");
        VBox form = KioskUI.card();
        PasswordField current = new PasswordField();
        PasswordField newPassword = new PasswordField();
        PasswordField confirm = new PasswordField();
        current.setPromptText("Current password");
        newPassword.setPromptText("New password");
        confirm.setPromptText("Confirm new password");
        KioskUI.styleInput(current);
        KioskUI.styleInput(newPassword);
        KioskUI.styleInput(confirm);
        Button save = KioskUI.menuButton("SAVE PASSWORD");
        Button cancel = KioskUI.secondaryButton("CANCEL");
        save.setOnAction(e -> {
            String currentText = current.getText();
            String newText = newPassword.getText();
            String confirmText = confirm.getText();
            if (!currentText.equals(managerPassword)) {
                showMessage("Current password is incorrect.", this::showChangePasswordScreen);
                return;
            }
            if (newText.length() < 4) {
                showMessage("New password must be at least 4 characters.", this::showChangePasswordScreen);
                return;
            }
            if (!newText.equals(confirmText)) {
                showMessage("New passwords do not match.", this::showChangePasswordScreen);
                return;
            }
            managerPassword = newText;
            showMessage("Manager password changed.", this::showManagerMenu);
        });
        cancel.setOnAction(e -> showManagerMenu());
        form.getChildren().addAll(
                KioskUI.bodyLabel("Use this screen to change the manager login password."),
                current,
                newPassword,
                confirm,
                save,
                cancel);
        r.getChildren().add(form);
        setScene(r);
    }

    private void showMoreFunctions() {
        VBox r = KioskUI.root("More Functions");
        VBox options = KioskUI.card();
        Button history = KioskUI.secondaryButton("ORDER HISTORY");
        Button feedback = KioskUI.secondaryButton("CUSTOMER FEEDBACK");
        Button logs = KioskUI.secondaryButton("ASSISTANCE LOGS");
        Button lowStock = KioskUI.secondaryButton("LOW STOCK");
        Button back = KioskUI.secondaryButton("BACK");
        history.setOnAction(e -> showOrderHistory());
        feedback.setOnAction(e -> showFeedbackRecords());
        logs.setOnAction(e -> showAssistanceLogs());
        lowStock.setOnAction(e -> showLowStockReport());
        back.setOnAction(e -> showManagerMenu());
        options.getChildren().addAll(history, feedback, logs, lowStock, back);
        r.getChildren().add(options);
        setScene(r);
    }

    private void showOrderHistory() {
        showMessage(KioskReports.orderHistory(orderHistory), this::showMoreFunctions);
    }

    private void showAssistanceLogs() {
        showMessage(KioskReports.assistanceLogs(assistanceLogs), this::showMoreFunctions);
    }

    private void showFeedbackRecords() {
        showMessage(KioskReports.feedback(feedbackRecords), this::showMoreFunctions);
    }

    private void showLowStockReport() {
        showMessage(KioskReports.lowStock(departments, LOW_STOCK_LIMIT), this::showMoreFunctions);
    }

    private void showMessage(String msg) {
        showMessage(msg, this::showWelcomeScreen);
    }

    private void showMessage(String msg, Runnable nextAction) {
        VBox r = KioskUI.root("Message");
        VBox details = KioskUI.card();
        Button ok = KioskUI.menuButton("OK");
        ok.setOnAction(e -> nextAction.run());
        details.getChildren().addAll(KioskUI.readOnlyArea(msg), ok);
        r.getChildren().add(details);
        setScene(r);
    }

}
