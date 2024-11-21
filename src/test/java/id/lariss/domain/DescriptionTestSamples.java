package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DescriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Description getDescriptionSample1() {
        return new Description().id(1L).name("name1").descriptionEn("descriptionEn1").descriptionId("descriptionId1");
    }

    public static Description getDescriptionSample2() {
        return new Description().id(2L).name("name2").descriptionEn("descriptionEn2").descriptionId("descriptionId2");
    }

    public static Description getDescriptionRandomSampleGenerator() {
        return new Description()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .descriptionEn(UUID.randomUUID().toString())
            .descriptionId(UUID.randomUUID().toString());
    }
}
