package id.lariss.domain.enumeration;

/**
 * The Memory enumeration.
 */
public enum Memory {
    GB_16("16 GB"),
    GB_24("24 GB");

    private final String value;

    Memory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
