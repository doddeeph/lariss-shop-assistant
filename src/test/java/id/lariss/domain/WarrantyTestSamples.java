package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WarrantyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Warranty getWarrantySample1() {
        return new Warranty().id(1L).name("name1").warrantyEn("warrantyEn1").warrantyId("warrantyId1");
    }

    public static Warranty getWarrantySample2() {
        return new Warranty().id(2L).name("name2").warrantyEn("warrantyEn2").warrantyId("warrantyId2");
    }

    public static Warranty getWarrantyRandomSampleGenerator() {
        return new Warranty()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .warrantyEn(UUID.randomUUID().toString())
            .warrantyId(UUID.randomUUID().toString());
    }
}
