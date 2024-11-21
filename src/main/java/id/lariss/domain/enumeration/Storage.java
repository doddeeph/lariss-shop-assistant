package id.lariss.domain.enumeration;

/**
 * The Storage enumeration.
 */
public enum Storage {
    STORAGE_256GB("256 GB"),
    STORAGE_512GB("512 GB");

    private final String value;

    Storage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
