package id.lariss.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product()
            .id(1L)
            .name("name1")
            .sku("sku1")
            .currency("currency1")
            .description("description1")
            .feature("feature1")
            .boxContents("boxContents1")
            .warranty("warranty1");
    }

    public static Product getProductSample2() {
        return new Product()
            .id(2L)
            .name("name2")
            .sku("sku2")
            .currency("currency2")
            .description("description2")
            .feature("feature2")
            .boxContents("boxContents2")
            .warranty("warranty2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .feature(UUID.randomUUID().toString())
            .boxContents(UUID.randomUUID().toString())
            .warranty(UUID.randomUUID().toString());
    }
}
