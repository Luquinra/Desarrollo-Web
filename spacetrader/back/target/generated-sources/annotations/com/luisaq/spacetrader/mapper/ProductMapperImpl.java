package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.ProductResponse;
import com.luisaq.spacetrader.model.economy.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse modelToResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.img( product.getImg() );
        productResponse.name( product.getName() );
        productResponse.volume( product.getVolume() );

        return productResponse.build();
    }
}
