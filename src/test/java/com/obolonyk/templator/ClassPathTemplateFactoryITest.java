package com.obolonyk.templator;

import com.obolonyk.templator.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClassPathTemplateFactoryITest {

    @Test
    @DisplayName("test ClassPathTemplateFactory GetPage From Template And ParamsMap")
    void testClassPathTemplateFactoryGetPageFromTemplateAndParamsMap(){
        ClassPathTemplateFactory classPathTemplateFactory = new ClassPathTemplateFactory("src/test/resources/templates/");
        String str = "2016-03-04 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        Product product1 = Product.builder()
                .id(1L)
                .name("Teddy Bear")
                .price(10.99)
                .creationDate(dateTime)
                .description("Good toy")
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Batman")
                .price(100.55)
                .creationDate(dateTime)
                .description("Best toy")
                .build();

        Product product3 = Product.builder()
                .id(3L)
                .name("Sega")
                .price(1111.55)
                .creationDate(dateTime)
                .description("Incredible toy")
                .build();

        List<Product> products = List.of(product1, product2);
        Map<String, Object> params = new HashMap<>();
        params.put("products", products);
        params.put("count", 99099);
        params.put("product", product3);

        String page = classPathTemplateFactory.getPage("products.html", params);
        assertNotNull(page);
        assertTrue(page.contains("Teddy Bear"));
        assertTrue(page.contains("Good toy"));
        assertTrue(page.contains("Batman"));
        assertTrue(page.contains("Best toy"));
        assertTrue(page.contains("Sega"));
        assertTrue(page.contains("Incredible toy"));
        assertTrue(page.contains("99099"));
    }

    @Test
    @DisplayName("test ClassPathTemplateFactory GetPage From Template And WithOut ParamsMap")
    void testClassPathTemplateFactoryGetPageFromTemplateAndWithOutParamsMap(){
        ClassPathTemplateFactory classPathTemplateFactory = new ClassPathTemplateFactory("src/test/resources/templates/");
        String page = classPathTemplateFactory.getPage("login.html");
        assertNotNull(page);
        assertTrue(page.contains("<head>"));
        assertTrue(page.contains("</head>"));
        assertTrue(page.contains("<title>"));
        assertTrue(page.contains("</title>"));
    }


}