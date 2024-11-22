package csci.ooad.polymorphia;

import java.text.DecimalFormat;

public record Food(String name, double healthValue) {
    private static final DecimalFormat formatter = new DecimalFormat("0.0");

    @Override
    public String toString() {
        return name + "(" + formatter.format(healthValue) +")";
    }
}
