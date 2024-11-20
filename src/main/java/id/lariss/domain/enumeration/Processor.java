package id.lariss.domain.enumeration;

/**
 * The Processor enumeration.
 */
public enum Processor {
    APPLE_M2_CHIP("Apple M2 Chip (CPU 8-Core GPU 8-Core"),
    APPLE_M3_CHIP_GPU_8_CORE("Apple M3 Chip (CPU 8-Core GPU 8-Core"),
    APPLE_M3_CHIP_GPU_10_CORE("Apple M3 Chip (CPU 8-Core GPU 10-Core");

    private final String value;

    Processor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
