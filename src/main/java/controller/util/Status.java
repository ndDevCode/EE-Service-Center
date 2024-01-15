package controller.util;

public enum Status {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CLOSED("Closed");

    private final String type;
    Status(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
