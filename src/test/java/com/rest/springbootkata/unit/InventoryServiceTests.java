package com.rest.springbootkata.unit;

import com.rest.springbootkata.entity.Product;
import com.rest.springbootkata.repository.JpaInventoryRepository;
import com.rest.springbootkata.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InventoryServiceTests {

    /**
     * 故事1: 添加商品
     * <p>
     * 作为 库存管理员,
     * 我想要 能够添加新的商品到库存中,
     * 以便于 我可以跟踪我们拥有的商品数量。
     * <p>
     * 故事2: 移除商品
     * 作为 库存管理员,
     * 我想要 能够从库存中移除商品,
     * 以便于 当商品售出或损坏时我可以更新库存记录。
     * <p>
     * 故事3: 移除商品
     * 作为 库存管理员,
     * 我想要 查看所有库存中的商品,
     * 以便于 我可以随时了解库存状况。
     **/

    @Autowired
    JpaInventoryRepository jpaInventoryRepository;

    @Autowired
    InventoryService inventoryService;


    @BeforeEach
    void ClearData() {
        jpaInventoryRepository.deleteAll();
    }

    @Test
    void should_create_successfully_when_add_product_given_a_product() {
        //given
        Product product = new Product();
        product.setName("Orange");
        product.setQuantity(100);

        //when
        Product savedProduct = inventoryService.addProduct(product);

        //then
        assertNotNull(savedProduct);
        assertThat(savedProduct.getName(), equalTo("Orange"));
        assertThat(savedProduct.getQuantity(), equalTo(100));
    }


    @Test
    void should_delete_successfully_when_delete_product_given_a_product_id() {

        //given
        Product product = new Product();
        product.setName("meat");
        product.setQuantity(100);

        Product savedProduct = inventoryService.addProduct(product);
        Integer productId = savedProduct.getId();
        //when
        inventoryService.deleteProduct(productId);

        //then
        Optional<Product> productOptional = Optional.ofNullable(inventoryService.getProductById(productId));
        assertThat(productOptional, equalTo(Optional.empty()));
    }


    @Test
    void should_get_all_products_when_query_product_list() {
        Product product1 = new Product();
        product1.setName("Apple");
        product1.setQuantity(100);

        Product product2 = new Product();
        product2.setName("Banana");
        product2.setQuantity(200);

        InventoryService inventoryService = new InventoryService(jpaInventoryRepository);
        inventoryService.addProduct(product1);
        inventoryService.addProduct(product2);
        List<Product> products = inventoryService.getAllProducts();

        assertThat(products.size(), equalTo(2));

    }

    @Test
    void should_get_the_correct_low_stock_products_when_call_getLowerStock_given_100() {
        //given
        Product product1 = new Product();
        product1.setName("Apple");
        product1.setQuantity(500);

        Product product2 = new Product();
        product2.setName("Banana");
        product2.setQuantity(200);

        inventoryService.addProduct(product1);
        inventoryService.addProduct(product2);
        //when
        List<Product> lowerStockProducts = inventoryService.getLowStockProducts(300);

        //then
        assertThat(lowerStockProducts.size(), equalTo(1));
    }

    @Test
    void should_adjust_stock_to_500_successfully_when_call_adjust_stock_given_quality_is_200() {
        //given
        Product product1 = new Product();
        product1.setName("Apple");
        product1.setQuantity(200);

        //when
        Product savedProduct = inventoryService.addProduct(product1);
        Product adjustedProduct = inventoryService.adjustStock(savedProduct.getId(), 300);

        //then
        assertThat(adjustedProduct.getQuantity(), equalTo(500));
    }

    @Test
    void should_throw_exception_when_adjust_the_quantity_to_minus() {
        //given
        Product product1 = new Product();
        product1.setName("Apple");
        product1.setQuantity(200);

        //when
        Product savedProduct = inventoryService.addProduct(product1);

        //when & then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.adjustStock(savedProduct.getId(), -400));


    }
}
