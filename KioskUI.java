import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
public class KioskUI {
    public static final int APP_WIDTH = 1220;
    public static final int APP_HEIGHT = 760;
    private static final String WALLPAPER_FILE = System.getProperty("user.dir") + "/kiosk-wallpaper.png";
    public static VBox root(String titleText) {
        VBox r = new VBox(18);
        r.setPadding(new Insets(34));
        r.setAlignment(Pos.TOP_CENTER);
        r.setMinHeight(APP_HEIGHT - 2);
        r.setStyle("-fx-background-image: url('file:" + WALLPAPER_FILE + "');"
                + " -fx-background-size: cover;"
                + " -fx-background-position: center center;"
                + " -fx-background-repeat: no-repeat;");
        Label title = new Label(titleText);
        title.setStyle("-fx-font-family: Arial; -fx-font-size: 32px; -fx-font-weight: bold;"
                + " -fx-text-fill: #172554;"
                + " -fx-background-color: rgba(255, 255, 255, 0.82);"
                + " -fx-background-radius: 12px;"
                + " -fx-padding: 10px 24px;"
                + " -fx-effect: dropshadow(gaussian, rgba(15, 23, 42, 0.34), 10, 0.22, 0, 3);");
        title.setWrapText(true);
        title.setMaxWidth(APP_WIDTH - 80);
        title.setAlignment(Pos.CENTER);
        r.getChildren().add(title);
        return r;
    }
    public static Scene scene(VBox r) {
        ScrollPane scrollPane = new ScrollPane(r);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setContent(r);
        return new Scene(scrollPane, APP_WIDTH, APP_HEIGHT);
    }
    public static Button menuButton(String text) {
        Button b = new Button(text);
        b.setMinHeight(42);
        b.setMaxWidth(360);
        b.setAlignment(Pos.CENTER);
        b.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: bold;"
                + " -fx-background-color: #1d4ed8; -fx-text-fill: white;"
                + " -fx-background-radius: 8px; -fx-cursor: hand;");
        return b;
    }
    public static Button secondaryButton(String text) {
        Button b = new Button(text);
        b.setMinHeight(38);
        b.setMaxWidth(360);
        b.setAlignment(Pos.CENTER);
        b.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold;"
                + " -fx-background-color: #334155; -fx-text-fill: white;"
                + " -fx-background-radius: 8px; -fx-cursor: hand;");
        return b;
    }
    public static Label bodyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(APP_WIDTH - 90);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-font-family: Arial; -fx-font-size: 16px; -fx-font-weight: bold;"
                + " -fx-text-fill: #f8fafc; -fx-line-spacing: 3px;"
                + " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.65), 5, 0.2, 0, 1);");
        return label;
    }
    public static VBox card() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(22));
        box.setMaxWidth(680);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(15, 23, 42, 0.78); -fx-border-color: rgba(255, 255, 255, 0.65);"
                + " -fx-border-radius: 10px; -fx-background-radius: 10px;"
                + " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.34), 18, 0.2, 0, 5);");
        return box;
    }
    public static void styleInput(TextField input) {
        input.setMinHeight(44);
        input.setAlignment(Pos.CENTER);
        input.setStyle("-fx-font-family: Arial; -fx-font-size: 15px; -fx-font-weight: bold;"
                + " -fx-background-color: rgba(255, 255, 255, 0.94); -fx-border-color: #1d4ed8;"
                + " -fx-text-fill: #0f172a; -fx-prompt-text-fill: #64748b;"
                + " -fx-border-radius: 8px; -fx-background-radius: 8px;"
                + " -fx-padding: 8px;");
    }
    public static void styleArea(TextArea area) {
        area.setStyle("-fx-font-family: Arial; -fx-font-size: 14px; -fx-font-weight: bold;"
                + " -fx-control-inner-background: #f8fafc;"
                + " -fx-text-fill: #0f172a;"
                + " -fx-border-color: #1d4ed8; -fx-border-radius: 8px;"
                + " -fx-background-radius: 8px;");
    }
    public static GridPane productGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxWidth(780);
        return grid;
    }
    public static TextArea readOnlyArea(String text) {
        TextArea area = new TextArea(text);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(10);
        styleArea(area);
        area.setMaxWidth(620);
        VBox.setVgrow(area, Priority.ALWAYS);
        return area;
    }
}
