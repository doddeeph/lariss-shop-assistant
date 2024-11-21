package id.lariss.domain.enumeration;

/**
 * The Color enumeration.
 */
public enum Color {
    MIDNIGHT("Midnight"),
    SPACE_GREY("Space Grey"),
    SILVER("Silver"),
    STARLIGHT("Starlight");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
