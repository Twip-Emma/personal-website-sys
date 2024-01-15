package top.twip.api.constant;

public enum AdminConstants {
    CURRENCY_HEADER_NAME("Admin"),
    CURRENCY_HEADER_VALUE("F0D2015A4F16E181841D22E469E3F458");

    private final String value;

    AdminConstants(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
