package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.ProductResponse;
import com.luisaq.spacetrader.model.economy.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse modelToResponse(Product product);
}
