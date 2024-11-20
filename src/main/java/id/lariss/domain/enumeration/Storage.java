package id.lariss.domain.enumeration;

/**
 * The Storage enumeration.
 */
public enum Storage {
    GB_256("256 GB"),
    GB_512("512 GB");

    private final String value;

    Storage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
