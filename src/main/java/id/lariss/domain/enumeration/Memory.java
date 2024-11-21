package id.lariss.domain.enumeration;

/**
 * The Memory enumeration.
 */
public enum Memory {
    MEMORY_16GB("16 GB"),
    MEMORY_24GB("24 GB");

    private final String value;

    Memory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
