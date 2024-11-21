package id.lariss.domain.enumeration;

/**
 * The Processor enumeration.
 */
public enum Processor {
    APPLE_M2_8CPU_8GPU("Apple M2 Chip (CPU 8-Core GPU 8-Core"),
    APPLE_M3_8CPU_8GPU("Apple M3 Chip (CPU 8-Core GPU 8-Core"),
    APPLE_M3_8CPU_10GPU("Apple M3 Chip (CPU 8-Core GPU 10-Core");

    private final String value;

    Processor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
