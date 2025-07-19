package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import se233.chapter2.Launcher;
import se233.chapter2.controller.AllEventHandler;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.time.LocalDateTime;
import java.util.List;

public class TopPane extends FlowPane {
    private Button refresh;
    private Button add;
    private Label update;
    public static ComboBox<String> currencies;
    public TopPane() {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setPrefSize(640,20);
        add = new Button("Add");
        refresh = new Button("Refresh");
        currencies = new ComboBox<>();
        currencies.setPromptText("Base currency");
        currencies.getItems().addAll("THB", "USD", "JPY", "AUD", "CAD", "EUR");
        currencies.getSelectionModel().selectFirst();
        currencies.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Currency> list = Launcher.getCurrencies();
                for (int i = 0; i < list.size(); i++) {
                    if (currencies.getSelectionModel().getSelectedItem().equals(Launcher.getCurrencies().get(i).getShortCode())) {
                        currencies.getSelectionModel().selectPrevious();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "The base currency and target currency cannot be the same.");
                        alert.showAndWait();
                        throw new IllegalArgumentException("The base currency and target currency cannot be the same.");
                    }
                }
                AllEventHandler.onSetBaseCurrency(currencies.getSelectionModel().getSelectedItem().toUpperCase());
            }
        });
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandler.onRefresh();
            }
        });

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandler.onAdd();
            }
        });
        update = new Label();
        refreshPane();
        this.getChildren().addAll(refresh, add, update, currencies);
    }

    public void refreshPane() {
        update.setText(String.format("Last update: %s", LocalDateTime.now().toString()));
    }
}
