package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Memory getMemorySample1() {
        return new Memory()
            .id(1L)
            .attributeLabel("attributeLabel1")
            .attributeName("attributeName1")
            .optionLabel("optionLabel1")
            .optionValue("optionValue1");
    }

    public static Memory getMemorySample2() {
        return new Memory()
            .id(2L)
            .attributeLabel("attributeLabel2")
            .attributeName("attributeName2")
            .optionLabel("optionLabel2")
            .optionValue("optionValue2");
    }

    public static Memory getMemoryRandomSampleGenerator() {
        return new Memory()
            .id(longCount.incrementAndGet())
            .attributeLabel(UUID.randomUUID().toString())
            .attributeName(UUID.randomUUID().toString())
            .optionLabel(UUID.randomUUID().toString())
            .optionValue(UUID.randomUUID().toString());
    }
}