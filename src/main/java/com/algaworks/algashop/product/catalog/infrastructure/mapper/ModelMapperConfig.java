package com.algaworks.algashop.product.catalog.infrastructure.mapper;


import com.algaworks.algashop.product.catalog.application.product.output.ImageOutput;
import com.algaworks.algashop.product.catalog.application.product.output.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.output.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.product.Image;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.infrastructure.utility.ApplicationMappingProperty;
import com.algaworks.algashop.product.catalog.infrastructure.utility.Slugfier;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Autowired
    private ApplicationMappingProperty applicationMappingProperty;

    private final Converter<String, String> fromStringToSlugConverter = context -> {
        return Slugfier.slugify(context.getSource());
    };

    private final Converter<String, String> fromStringToShortStringConverter = context -> {
        return StringUtils.abbreviate(context.getSource(), 50);
    };

    private final Converter<String, String> fromFileNameToUrlConverter = context -> {
        return convertFromFileNameToUrl(context.getSource());
    };


    @Bean
    public Mapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        configuration(modelMapper);
        return modelMapper::map;
    }

    private void configuration(ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setSourceNamingConvention(NamingConventions.NONE)
                .setDestinationNamingConvention(NamingConventions.NONE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Image.class, ImageOutput.class).addMappings(
                mapping -> {
                    mapping.using(fromFileNameToUrlConverter).map(Image::getName, ImageOutput::setUrl);
                }
        );

        modelMapper.createTypeMap(Product.class, ProductSummaryOutput.class).addMappings(
                mapper ->
                        mapper.using(fromStringToShortStringConverter)
                                .map(Product::getDescription, ProductSummaryOutput::setShortDescription)
        );

        modelMapper.createTypeMap(Product.class, ProductDetailOutput.class).addMappings(
                mapping -> {
                    mapping.using(fromStringToSlugConverter).map(Product::getName, ProductDetailOutput::setSlug);
                }
        );
    }


    private String convertFromFileNameToUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        return  applicationMappingProperty.getImageStorageUrl() + "/" + fileName;
    }
}
