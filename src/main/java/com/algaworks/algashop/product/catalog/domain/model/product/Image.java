package com.algaworks.algashop.product.catalog.domain.model.product;


import com.algaworks.algashop.product.catalog.domain.utility.IdGenerator;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @EqualsAndHashCode.Include
    private UUID id;
    private String name;

    Image(UUID id, String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(id, "id cannot be null");
        this.id = id;
        this.name = name;
    }

    public Image(String name) {
        this(IdGenerator.generateTimeBasedUUID(), name);
    }
}
