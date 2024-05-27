package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.PlanetaryProductResponse;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class PlanetaryProductMapperImpl implements PlanetaryProductMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PlanetaryProductResponse modelToResponse(PlanetaryProduct planetaryProduct) {
        if ( planetaryProduct == null ) {
            return null;
        }

        PlanetaryProductResponse.PlanetaryProductResponseBuilder planetaryProductResponse = PlanetaryProductResponse.builder();

        planetaryProductResponse.id( planetaryProduct.getId() );
        planetaryProductResponse.product( productMapper.modelToResponse( planetaryProduct.getProduct() ) );
        planetaryProductResponse.stock( planetaryProduct.getStock() );

        planetaryProductResponse.buyPrice( calculatePrices(planetaryProduct.getMarket().getOfferFactor(), planetaryProduct.getStock() ) );

        return planetaryProductResponse.build();
    }
}
