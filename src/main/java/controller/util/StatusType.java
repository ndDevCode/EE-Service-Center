package controller.util;

public enum StatusType {
    PENDING("Pending"),
    PROCESSING("Processing"),
    COMPLETED("Completed"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String type;
    StatusType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return type;
    }
}
