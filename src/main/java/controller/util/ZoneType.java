package controller.util;

public enum ZoneType {
    ORANGE("Orange"),
    RED("Red"),
    YELLOW("Yellow"),
    GREEN("Green");


    private final String type;
    ZoneType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
