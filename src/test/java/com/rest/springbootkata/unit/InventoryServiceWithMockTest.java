package com.rest.springbootkata.unit;

import com.rest.springbootkata.entity.Product;
import com.rest.springbootkata.repository.JpaInventoryRepository;
import com.rest.springbootkata.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InventoryServiceWithMockTest {

    /**
     * 故事1: 添加商品
     *
     * 作为 库存管理员,
     * 我想要 能够添加新的商品到库存中,
     * 以便于 我可以跟踪我们拥有的商品数量。
     *
     * 故事2: 移除商品
     * 作为 库存管理员,
     * 我想要 能够从库存中移除商品,
     * 以便于 当商品售出或损坏时我可以更新库存记录。
     *
     * 故事3: 移除商品
     * 作为 库存管理员,
     * 我想要 查看所有库存中的商品,
     * 以便于 我可以随时了解库存状况。
     *
     * **/

    @Mock
    JpaInventoryRepository jpaInventoryRepository;

    @InjectMocks
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

        Product expectedProduct = new Product();
        expectedProduct.setName("Orange");
        expectedProduct.setName("100");
        expectedProduct.setId(111);

        //when
        given(jpaInventoryRepository.save(product)).willReturn(expectedProduct);
        Product savedProduct = inventoryService.addProduct(product);

        //then
        verify(jpaInventoryRepository).save(product);
        assertThat(savedProduct.getId(), equalTo(expectedProduct.getId()));
        assertThat(savedProduct.getName(), equalTo(expectedProduct.getName()));
        assertThat(savedProduct.getQuantity(), equalTo(expectedProduct.getQuantity()));
    }


    @Test
    void should_delete_successfully_when_delete_product_given_a_product_id() {

        //given
        Product product = new Product();
        product.setName("meat");
        product.setQuantity(100);

        Product expectedProduct = new Product();
        expectedProduct.setName("Orange");
        expectedProduct.setName("100");
        expectedProduct.setId(111);

        //when
        given(jpaInventoryRepository.save(product)).willReturn(expectedProduct);
        Product savedProduct = inventoryService.addProduct(product);
        Integer productId = savedProduct.getId();
        inventoryService.deleteProduct(productId);

        //then

        verify(jpaInventoryRepository).deleteById(productId);
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

        verify(jpaInventoryRepository).findAll();
    }


    @Test
    void should_adjust_stock_to_500_successfully_when_call_adjust_stock_given_quality_is_200() {

        Product expectedProduct = new Product();
        expectedProduct.setName("Apple");
        expectedProduct.setQuantity(200);
        expectedProduct.setId(111);

        //when
        given(jpaInventoryRepository.findById(111)).willReturn(Optional.of(expectedProduct));
        inventoryService.adjustStock(111, 300);

        // Capture the argument passed to save method
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(jpaInventoryRepository,times(1)).save(productArgumentCaptor.capture());

        // Verify the captured argument
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct.getQuantity(), equalTo(500));
    }

    @Test
    void should_throw_exception_when_adjust_the_quantity_to_minus() {

        //given
        Product expectedProduct = new Product();
        expectedProduct.setName("Apple");
        expectedProduct.setQuantity(200);
        expectedProduct.setId(111);

        //when
        given(jpaInventoryRepository.findById(111)).willReturn(Optional.of(expectedProduct));

        //then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.adjustStock(111, -400));


    }
}
