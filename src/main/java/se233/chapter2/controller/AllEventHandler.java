package se233.chapter2.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import org.json.JSONException;
import se233.chapter2.Launcher;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AllEventHandler {

    public static void onSetBaseCurrency(String baseCode) {
        try {

            List<Currency> currencies = new ArrayList<>();
            List<Currency> prevCurrencies = Launcher.getCurrencies();
            Launcher.setCurrencies(currencies);
            for (int i = 0; i < prevCurrencies.size(); i++) {
                CurrencyEntity.setBaseCurrency(baseCode);
                Currency c = new Currency(prevCurrencies.get(i).getShortCode());
                List<CurrencyEntity> cList = FetchData.fetchRange(c.getShortCode(), 30, baseCode);
                c.setHistorical(cList);
                c.setCurrent(cList.get(cList.size() - 1));
                currencies.add(c);
            }
            Launcher.setCurrencies(currencies);
            Launcher.refreshPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onRefresh() {
        try {
            Launcher.refreshPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onAdd(String code) {

    }

    public static void onAdd() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Currency");
            dialog.setContentText("Currency code: ");
            dialog.setHeaderText(null);
            dialog.setGraphic(null);
            Optional<String> code = dialog.showAndWait();
            if (code.isPresent() && code.get().length() == 3) {
                List<Currency> currencies = Launcher.getCurrencies();
                Currency c = new Currency(code.get().toUpperCase());
                List<CurrencyEntity> cList;
                if ((cList = FetchData.fetchRange(c.getShortCode(), 30, CurrencyEntity.baseCurrency)).isEmpty())
                    throw new JSONException("No currencies");
                c.setHistorical(cList);
                c.setCurrent(cList.get(cList.size() - 1));
                currencies.add(c);
                Launcher.setCurrencies(currencies);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void onDelete(String code) {
        try {
            List<Currency> currencies = Launcher.getCurrencies();
            int index = -1;

            for (int i = 0 ; i < currencies.size(); i++) {
                if (currencies.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                currencies.remove(index);
                Launcher.setCurrencies(currencies);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void onWatch(String code) {
        try {
            List<Currency> currencies = Launcher.getCurrencies();
            int index = -1;
            for (int i = 0 ; i < currencies.size(); i++) {
                if (currencies.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Watch");
                dialog.setContentText("Rate: ");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);
                Optional<String> retrievedRate = dialog.showAndWait();
                if (retrievedRate.isPresent() && retrievedRate.get().length() == 3) {
                    double rate = Double.parseDouble(retrievedRate.get());
                    currencies.get(index).setWatch(true);
                    currencies.get(index).setWatchRate(rate);
                    Launcher.setCurrencies(currencies);
                    Launcher.refreshPane();
                }
            }
            Launcher.setCurrencies(currencies);
            Launcher.refreshPane();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void onUnwatch(String code) {
        try {
            List<Currency> currencies = Launcher.getCurrencies();
            int index = -1;
            for (int i = 0; i < currencies.size(); i++) {
                if (currencies.get(i).getShortCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                currencies.get(index).setWatch(false);
                currencies.get(index).setWatchRate(null);
                Launcher.setCurrencies(currencies);
                Launcher.refreshPane();
            }
            Launcher.setCurrencies(currencies);
            Launcher.refreshPane();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
