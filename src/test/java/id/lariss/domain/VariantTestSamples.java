package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VariantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Variant getVariantSample1() {
        return new Variant().id(1L).label("label1").sku("sku1");
    }

    public static Variant getVariantSample2() {
        return new Variant().id(2L).label("label2").sku("sku2");
    }

    public static Variant getVariantRandomSampleGenerator() {
        return new Variant().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).sku(UUID.randomUUID().toString());
    }
}
