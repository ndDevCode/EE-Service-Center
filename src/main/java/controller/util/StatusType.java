package controller.util;

public enum StatusType {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CLOSED("Closed");

    private final String type;
    StatusType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
