package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.PlanetaryMarketResponse;
import com.luisaq.spacetrader.dto.response.economy.PlanetaryProductResponse;
import com.luisaq.spacetrader.model.economy.PlanetaryMarket;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class PlanetaryMarketMapperImpl implements PlanetaryMarketMapper {

    @Autowired
    private PlanetaryProductMapper planetaryProductMapper;

    @Override
    public PlanetaryMarketResponse modelToResponse(PlanetaryMarket planetaryMarket) {
        if ( planetaryMarket == null ) {
            return null;
        }

        PlanetaryMarketResponse.PlanetaryMarketResponseBuilder planetaryMarketResponse = PlanetaryMarketResponse.builder();

        planetaryMarketResponse.demandFactor( planetaryMarket.getDemandFactor() );
        planetaryMarketResponse.offerFactor( planetaryMarket.getOfferFactor() );
        planetaryMarketResponse.products( planetaryProductSetToPlanetaryProductResponseSet( planetaryMarket.getProducts() ) );

        return planetaryMarketResponse.build();
    }

    protected Set<PlanetaryProductResponse> planetaryProductSetToPlanetaryProductResponseSet(Set<PlanetaryProduct> set) {
        if ( set == null ) {
            return null;
        }

        Set<PlanetaryProductResponse> set1 = new LinkedHashSet<PlanetaryProductResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PlanetaryProduct planetaryProduct : set ) {
            set1.add( planetaryProductMapper.modelToResponse( planetaryProduct ) );
        }

        return set1;
    }
}
