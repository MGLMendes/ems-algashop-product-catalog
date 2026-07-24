package com.algaworks.algashop.product.catalog.application.storage;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.util.Objects;

@Getter
public class FileReference {

    private String fileName;
    private MediaType contentType;
    private Long contentLength;
    private Duration expiresIn;

    @Builder
    public FileReference(String fileName, MediaType contentType, Long contentLength, Duration expiresIn) {
        Objects.requireNonNull(fileName, "fileName is required");
        Objects.requireNonNull(contentType, "contentType is required");
        Objects.requireNonNull(expiresIn, "contentLength is required");
        if (contentLength <= 0 ) {
            throw new IllegalArgumentException();
        }
        this.fileName = fileName;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.expiresIn = expiresIn;

    }
}
