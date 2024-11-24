package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ColorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Color getColorSample1() {
        return new Color()
            .id(1L)
            .attributeLabel("attributeLabel1")
            .attributeName("attributeName1")
            .optionLabel("optionLabel1")
            .optionValue("optionValue1");
    }

    public static Color getColorSample2() {
        return new Color()
            .id(2L)
            .attributeLabel("attributeLabel2")
            .attributeName("attributeName2")
            .optionLabel("optionLabel2")
            .optionValue("optionValue2");
    }

    public static Color getColorRandomSampleGenerator() {
        return new Color()
            .id(longCount.incrementAndGet())
            .attributeLabel(UUID.randomUUID().toString())
            .attributeName(UUID.randomUUID().toString())
            .optionLabel(UUID.randomUUID().toString())
            .optionValue(UUID.randomUUID().toString());
    }
}
