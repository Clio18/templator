package com.obolonyk.templator.processor;

import com.obolonyk.templator.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ForEachTemplateProcessorTest {

    @Test
    @DisplayName("test ForEachTemplateProcessor")
    void testForEachTemplateProcessor() {
        String template = """
                  <#foreach product in products>
                            <tr>
                                <td>${product.name}</td>
                                <td>${product.price}</td>
                                <td>${product.creationDate}</td>
                                <td>${product.description}</td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <div style="margin-right: 20px">
                                            <form action="/products/update" method="get">
                                                <input type="hidden" name="id" value="${product.id}">
                                                <button type="submit" class="btn btn btn-warning" style="width: 80px">Update</button>
                                            </form>
                                        </div>
                                </td>
                             </tr>    
                  </#foreach>       
                """;

        String str = "2016-03-04 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        String expectedTemplate = """              
                            <tr>
                                <td>Teddy Bear</td>
                                <td>10.99</td>
                                <td>2016-03-04T11:30</td>
                                <td>Good toy</td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <div style="margin-right: 20px">
                                            <form action="/products/update" method="get">
                                                <input type="hidden" name="id" value="1">
                                                <button type="submit" class="btn btn btn-warning" style="width: 80px">Update</button>
                                            </form>
                                        </div>
                                </td>
                             </tr>
                             <tr>
                                <td>Batman</td>
                                <td>100.55</td>
                                <td>2016-03-04T11:30</td>
                                <td>Best toy</td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <div style="margin-right: 20px">
                                            <form action="/products/update" method="get">
                                                <input type="hidden" name="id" value="2">
                                                <button type="submit" class="btn btn btn-warning" style="width: 80px">Update</button>
                                            </form>
                                        </div>
                                </td>
                             </tr>     
                """;

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

        List<Product> products = List.of(product1, product2);
        Map<String, Object> params = new HashMap<>();
        params.put("products", products);

        ForEachTemplateProcessor forEachTemplateProcessor = new ForEachTemplateProcessor();
        String resultTemplate = forEachTemplateProcessor.process(template, params).replace("\n", "").replace(" ", "");
        expectedTemplate = expectedTemplate.replace("\n", "").replace(" ", "");
        assertEquals(expectedTemplate, resultTemplate);
    }
}