package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BoxContentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BoxContent getBoxContentSample1() {
        return new BoxContent().id(1L).name("name1").contentEn("contentEn1").contentId("contentId1");
    }

    public static BoxContent getBoxContentSample2() {
        return new BoxContent().id(2L).name("name2").contentEn("contentEn2").contentId("contentId2");
    }

    public static BoxContent getBoxContentRandomSampleGenerator() {
        return new BoxContent()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .contentEn(UUID.randomUUID().toString())
            .contentId(UUID.randomUUID().toString());
    }
}
