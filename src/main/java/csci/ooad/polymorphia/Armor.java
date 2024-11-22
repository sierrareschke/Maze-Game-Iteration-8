package csci.ooad.polymorphia;

public record Armor(String name, double strength, double movingCost) {
    @Override
    public String toString() {
        return name + "-Armor";
    }
}
