package com.algaworks.algashop.product.catalog.application.product.service.management;

import com.algaworks.algashop.product.catalog.application.product.input.ImageInput;
import com.algaworks.algashop.product.catalog.application.product.output.ImageOutput;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.product.Image;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageManagementApplicationService {

    private final ProductRepository productRepository;
    private final StorageProvider storageProvider;
    private final Mapper mapper;

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public ImageOutput create(UUID productId, ImageInput imageInput) {
        Objects.requireNonNull(productId, "productId is required");
        Objects.requireNonNull(imageInput);

        Product product = getProduct(productId);

        if (!storageProvider.fileExists(imageInput.getRemoteFileName())) {
            throw new DomainException(String.format("Image name %s was not found on storage provider",
                    imageInput.getRemoteFileName()));
        }

        if (productRepository.existsByImagesName(imageInput.getRemoteFileName())) {
            throw new DomainException(String.format("Image name %s is already in use", imageInput.getRemoteFileName()));
        }

        UUID imageId = product.addImage(imageInput.getRemoteFileName());
        productRepository.save(product);

        Image image = product.getImage(imageId).orElseThrow();
        return mapper.convert(image, ImageOutput.class);
    }

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public void delete(UUID productId, UUID imageId) {
        Objects.requireNonNull(productId, "productId is required");
        Objects.requireNonNull(imageId, "imageId is required");

        Product product = getProduct(productId);
        Image image = product.getImage(imageId).orElseThrow(
                () -> new DomainException(
                        String.format("Image of id %s was not found on product %s", imageId, product.getId()))
        );

        product.removeImage(image.getId());
        storageProvider.deleteFile(image.getName());
        productRepository.save(product);
    }

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public void primary(UUID productId, UUID imageId) {
        Objects.requireNonNull(productId, "productId is required");
        Objects.requireNonNull(imageId, "imageId is required");

        Product product = getProduct(productId);
        product.changeMainImage(imageId);

        productRepository.save(product);
    }

    private @NonNull Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
