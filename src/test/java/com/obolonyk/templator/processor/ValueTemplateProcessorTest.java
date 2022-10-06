package com.obolonyk.templator.processor;

import com.obolonyk.templator.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ValueTemplateProcessorTest {
    @Test
    @DisplayName("test ValueTemplateProcessor")
    void testValueTemplateProcessor() {
        String template = """
                 <div class="container">
                            <div class="col-md-6">
                                <label class="form-label">Product name</label>
                                <input type="text" class="form-control" name="name" value="${product.name}" required>
                                <div class="form-text">Enter product name</div>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn btn-info" style="width: 80px">
                                                        Cart ${count}
                                                    </button>
                                
                        <div class="container">
                            <div class="col-md-6">
                                <label class="form-label">Product price</label>
                                <input type="number" step="0.01" class="form-control"
                                       min="0" name="price" value="${product.price}" required>
                                <div class="form-text">Enter product price</div>
                            </div>
                        </div>
                """;
        String str = "2016-03-04 11:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        Product product = Product.builder()
                .id(1L)
                .name("Teddy Bear")
                .price(10.99)
                .creationDate(dateTime)
                .description("Good toy")
                .build();
        Map<String, Object> params = new HashMap<>();
        params.put("product", product);
        int count = 7;
        params.put("count", count);

        ValueTemplateProcessor valueTemplateProcessor = new ValueTemplateProcessor();
        String resultTemplate = valueTemplateProcessor.process(template, params);
        String expectedTemplate = """
                 <div class="container">
                            <div class="col-md-6">
                                <label class="form-label">Product name</label>
                                <input type="text" class="form-control" name="name" value="Teddy Bear" required>
                                <div class="form-text">Enter product name</div>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn btn-info" style="width: 80px">
                                                        Cart 7
                                                    </button>
                                
                        <div class="container">
                            <div class="col-md-6">
                                <label class="form-label">Product price</label>
                                <input type="number" step="0.01" class="form-control"
                                       min="0" name="price" value="10.99" required>
                                <div class="form-text">Enter product price</div>
                            </div>
                        </div>
                """;
        assertEquals(expectedTemplate, resultTemplate);
    }
}