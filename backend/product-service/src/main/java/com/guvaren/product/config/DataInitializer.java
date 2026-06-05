package com.guvaren.product.config;

import com.guvaren.product.master.entity.ProductEntity;
import com.guvaren.product.master.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ProductRepository productRepo;

    @Override
    public void run(String... args) throws Exception {
        initProducts();
    }

    private void initProducts() {
        if (!productRepo.findAll().isEmpty()) return;
        List<ProductEntity> products = List.of(
                ProductEntity.builder()
                        .id("p1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6")
                        .code("PRD-001")
                        .name("Premium Arabica Coffee")
                        .category("Beverages")
                        .description("Selected Arabica coffee from the Gayo highlands, Aceh. Features a distinctive flavor with balanced acidity.")
                        .weight(new BigDecimal("0.25"))
                        .price(new BigDecimal("75000.00"))
                        .imageUrl("https://example.com/images/arabica-coffee.jpg")
                        .status(true)
                        .build(),
                ProductEntity.builder()
                        .id("p2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7")
                        .code("PRD-002")
                        .name("Organic Green Tea")
                        .category("Beverages")
                        .description("Pesticide-free organic green tea. Rich in antioxidants and beneficial for overall health.")
                        .weight(new BigDecimal("0.10"))
                        .price(new BigDecimal("45000.00"))
                        .imageUrl("https://example.com/images/green-tea.jpg")
                        .status(true)
                        .build(),
                ProductEntity.builder()
                        .id("p3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8")
                        .code("PRD-003")
                        .name("Spicy Cassava Chips")
                        .category("Foods")
                        .description("Crunchy cassava chips coated with authentic Indonesian spicy seasoning. Perfect for everyday snacking.")
                        .weight(new BigDecimal("0.15"))
                        .price(new BigDecimal("25000.00"))
                        .imageUrl("https://example.com/images/cassava-chips.jpg")
                        .status(true)
                        .build(),
                ProductEntity.builder()
                        .id("p4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r9")
                        .code("PRD-004")
                        .name("Pure Forest Honey")
                        .category("Health")
                        .description("Pure wild honey from forest bees in Kalimantan. Known to boost the immune system naturally.")
                        .weight(new BigDecimal("0.50"))
                        .price(new BigDecimal("120000.00"))
                        .imageUrl("https://example.com/images/forest-honey.jpg")
                        .status(true)
                        .build(),
                ProductEntity.builder()
                        .id("p5e6f7g8h9i0j1k2l3m4n5o6p7q8r9s0")
                        .code("PRD-005")
                        .name("Premium Handwritten Batik")
                        .category("Fashion")
                        .description("Handmade batik fabric with elegant traditional motifs. Suitable for both formal and casual occasions.")
                        .weight(new BigDecimal("0.30"))
                        .price(new BigDecimal("350000.00"))
                        .imageUrl("https://example.com/images/batik-fabric.jpg")
                        .status(false)
                        .build()
        );
        productRepo.saveAll(products);
    }
}
