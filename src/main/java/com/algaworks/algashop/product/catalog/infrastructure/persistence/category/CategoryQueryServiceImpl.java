package com.algaworks.algashop.product.catalog.infrastructure.persistence.category;

import com.algaworks.algashop.product.catalog.application.category.output.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.service.query.CategoryFilter;
import com.algaworks.algashop.product.catalog.application.category.service.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.output.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.presentation.model.PageModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;
    private final MongoOperations mongoOperations;

    @Override
    public PageModel<CategoryDetailOutput> filter(CategoryFilter categoryFilter) {
        Query query = queryWith(categoryFilter);

        long totalItems = mongoOperations.count(query, Category.class);

        Sort sort = sortWith(categoryFilter);

        PageRequest pageRequest = PageRequest.of(categoryFilter.getPage(), categoryFilter.getSize(), sort);
        Query pagedQuery = query.with(pageRequest);
        List<Category> categories;
        int totalPages = 0;
        if (totalItems > 0) {
            categories = mongoOperations.find(pagedQuery, Category.class);
            totalPages = (int) Math.ceil((double) totalItems / (double) categories.size());
        } else {
            categories = new ArrayList<>();
        }

        List<CategoryDetailOutput> categoriesSummary =
                categories.stream().map(p -> mapper.convert(p, CategoryDetailOutput.class)).toList();
        return PageModel.<CategoryDetailOutput>builder()
                .content(categoriesSummary)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalElements(totalItems)
                .totalPages(totalPages)
                .build();
    }

    private Sort sortWith(CategoryFilter categoryFilter) {
        return Sort.by(categoryFilter.getSortDirectionOrDefault(),
                categoryFilter.getSortByPropertyOrDefault().getPropertyName());
    }

    private Query queryWith(CategoryFilter filter) {
        Query query = new Query();
        if (filter.getEnabled() != null) {
            query.addCriteria(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (StringUtils.isNotBlank(filter.getName())) {
            query.addCriteria(Criteria.where("name").regex(filter.getName().trim(), "i"));
        }

        return query;
    }

    @Override
    public CategoryDetailOutput findById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(categoryId)
        );
        return mapper.convert(category, CategoryDetailOutput.class);
    }
}
