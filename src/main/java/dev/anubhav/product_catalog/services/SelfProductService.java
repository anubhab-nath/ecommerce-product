package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.PriceDto;
import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;
import dev.anubhav.product_catalog.models.Category;
import dev.anubhav.product_catalog.models.Price;
import dev.anubhav.product_catalog.models.Product;
import dev.anubhav.product_catalog.repos.CategoryRepository;
import dev.anubhav.product_catalog.repos.PriceRepository;
import dev.anubhav.product_catalog.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Component("selfProductService")
public class SelfProductService implements ProductService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SelfProductService(
            ProductRepository productRepository,
            PriceRepository priceRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto getProductById(String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        Product product  = productRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + " not found"));

        Price cost = product.getCost();

        return ProductDto.builder()
                .id(product.getId().toString())
                .title(product.getTitle())
                .category(product.getCategory().getName())
                .cost(PriceDto.builder()
                        .currency(cost.getCurrency())
                        .amount(cost.getAmount()).build()
                )
                .description(product.getDescription())
                .build();
    }

    // todo: based on category with limit and order
    @Override
    public List<ProductDto> getAllProducts(String category, Integer limit, String sort) throws NotFoundException {
        List<Product> products;
        if(category != null) {
            Category storedCategory = categoryRepository.findByName(category)
                    .orElseThrow(() -> new NotFoundException("Category with name '" + category + "' is not found."));
            products = productRepository.findByCategory(storedCategory.getId());
        }
        else products = productRepository.findAll();

        List<ProductDto> productDtoList = new ArrayList<>();
        for(Product product: products) {
            Price cost = product.getCost();
            productDtoList.add(
                    ProductDto.builder()
                            .id(product.getId().toString())
                            .title(product.getTitle())
                            .category(product.getCategory().getName())
                            .cost(PriceDto.builder()
                                    .currency(cost.getCurrency())
                                    .amount(cost.getAmount()).build()
                            )
                            .description(product.getDescription())
                            .build()
            );
        }

        return productDtoList;
    }

    @Override
    public ProductDto createProduct(ProductDto requestDto) {
        Product product = new Product();
        product.setTitle(requestDto.getTitle());
        product.setDescription(requestDto.getDescription());

        PriceDto priceDto = requestDto.getCost();
        Price price = new Price(priceDto.getCurrency(), priceDto.getAmount());
        Price savedPrice = priceRepository.save(price);
        product.setCost(savedPrice);

        Category savedCategory = getCategory(requestDto.getCategory());
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);
        Price savedCost = savedProduct.getCost();

        return ProductDto.builder()
                .id(savedProduct.getId().toString())
                .category(savedProduct.getCategory().getName())
                .title(savedProduct.getTitle())
                .description(savedProduct.getDescription())
                .cost(PriceDto.builder()
                        .currency(savedCost.getCurrency())
                        .amount(savedCost.getAmount()).build()
                )
                .build();
    }

    private Category getCategory(String category) {
        Optional<Category> storedCategory = categoryRepository.findByName(category);
        if(storedCategory.isEmpty()) {
            Category newCategory = new Category(category);
            return categoryRepository.save(newCategory);
        }
        return storedCategory.get();

    }

    @Override
    public List<String> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    @Override
    public ProductDto updateProduct(ProductDto requestDto, String id) {
        UUID uuid = UUID.fromString(id);
        Product product = productRepository.getReferenceById(uuid);
        product.setTitle(requestDto.getTitle());
        product.setDescription(requestDto.getDescription());

        PriceDto cost = requestDto.getCost();
        Price price = product.getCost();
        price.setCurrency(cost.getCurrency());
        price.setAmount(cost.getAmount());
        Price savedPrice = priceRepository.save(price);
        product.setCost(savedPrice);

        Category category = product.getCategory();
        category.setName(category.getName());
        Category savedCategory = categoryRepository.save(category);
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);
        Price savedCost = savedProduct.getCost();
        return ProductDto.builder()
                .id(savedProduct.getId().toString())
                .category(savedProduct.getCategory().getName())
                .title(savedProduct.getTitle())
                .description(savedProduct.getDescription())
                .cost(PriceDto.builder()
                        .currency(savedCost.getCurrency())
                        .amount(savedCost.getAmount()).build()
                )
                .build();
    }

    @Override
    public ProductDto deleteProduct(String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        Product product  = productRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + " not found"));
        productRepository.delete(product);
        return ProductDto.builder().build();
    }
}
