package controller.util;

public enum ValidationType {
    EMAIL("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"),
    PASSWORD("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"),
    CONTACT_NO("^0\\d{9}$"),
    TEXT_ONLY("^[a-zA-Z]+$"),
    DOUBLE_VALUE("^[+]?\\d*\\.?\\d+$");

    private final String regex;

    ValidationType(String regex){
        this.regex = regex;
    }

    @Override
    public String toString() {
        return regex;
    }
}
