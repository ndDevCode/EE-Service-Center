package controller.util;

public enum Zone {
    ORANGE("Orange"),
    RED("Red"),
    YELLOW("Yellow"),
    GREEN("Green");


    private final String type;
    Zone(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
