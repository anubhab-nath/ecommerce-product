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

        return convertToProductDto(product);
    }

    /* todo:
        - based on category with limit and order
        - return only titles if param 'onlyTitles' is true
    * */
    @Override
    public List<ProductDto> getAllProducts(String category, Integer limit, String sort) throws NotFoundException {
        List<Product> products;
        if(category != null) {
            Category storedCategory = categoryRepository.findByName(category)
                    .orElseThrow(() -> new NotFoundException("Category with name '" + category + "' is not found."));
            products = productRepository.findByCategory(storedCategory.getId());
        }
        else products = productRepository.findAll();

        return products.stream()
                .map(this::convertToProductDto)
                .toList();
    }

    @Override
    public ProductDto createProduct(ProductDto requestDto) {
        Product product = new Product();
        product.setTitle(requestDto.getTitle());
        product.setDescription(requestDto.getDescription());

        PriceDto priceDto = requestDto.getPrice();
        Price price = new Price(priceDto.getCurrency(), priceDto.getAmount());
        Price savedPrice = priceRepository.save(price);
        product.setPrice(savedPrice);

        Category savedCategory = getCategory(requestDto.getCategory());
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);

        return convertToProductDto(savedProduct);
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

        PriceDto cost = requestDto.getPrice();
        Price price = product.getPrice();
        price.setCurrency(cost.getCurrency());
        price.setAmount(cost.getAmount());
        Price savedPrice = priceRepository.save(price);
        product.setPrice(savedPrice);

        Category category = product.getCategory();
        category.setName(category.getName());
        Category savedCategory = categoryRepository.save(category);
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);
        return convertToProductDto(savedProduct);
    }

    @Override
    public ProductDto deleteProduct(String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        Product product  = productRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Product with id: " + id + " not found"));
        productRepository.delete(product);
        return ProductDto.builder().build();
    }

    private ProductDto convertToProductDto(Product product) {
        Price price = product.getPrice();
        return ProductDto.builder()
                .id(product.getId().toString())
                .title(product.getTitle())
                .category(product.getCategory().getName())
                .price(PriceDto.builder()
                        .currency(price.getCurrency())
                        .amount(price.getAmount()).build()
                )
                .description(product.getDescription())
                .build();
    }
}
