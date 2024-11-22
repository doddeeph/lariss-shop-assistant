package id.lariss.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import id.lariss.service.ProductService;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Log4j2
@Configuration
public class ProductFunctionConfiguration {

    @Autowired
    private ProductService productService;

    public record ProductRequest(String category) {}

    @JsonInclude(Include.NON_NULL)
    public record ProductDetails(
        String name,
        String color,
        String processor,
        String memory,
        String storage,
        String description,
        String feature,
        String boxContent,
        String warranty
    ) {}

    @Bean
    @Description("Get product details")
    public Function<ProductRequest, ProductDetails> getProductDetails() {
        return request -> {
            try {
                return productService.findByCategory(request.category()).toFuture().get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error Function getProductDetails -> request: {}", request);
                throw new RuntimeException(e);
            }
        };
    }
}
