package com.algaworks.algashop.product.catalog.application.category.service.query;

import com.algaworks.algashop.product.catalog.application.utility.SortablePageFilter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
public class CategoryFilter extends SortablePageFilter<CategoryFilter.SortType> {

    private Boolean enabled;
    private String name;

    @Override
    public SortType getSortByPropertyOrDefault() {
        return getSortByProperty() == null ? CategoryFilter.SortType.NAME : getSortByProperty();
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
    }

    public boolean isCacheable() {
        return isDefaultFilter();
    }

    public static CategoryFilter defaultFilter() {
        return CategoryFilter.builder()
                .name(null)
                .enabled(true)
                .page(0)
                .size(15)
                .sortDirection(Sort.Direction.ASC)
                .sortByProperty(SortType.NAME)
                .build();
    }

    private boolean isDefaultFilter() {
        return this.equals(defaultFilter());
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        NAME("name");
        private final String propertyName;

    }
}
