import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// Category entity
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}

// Product entity
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}

// Category repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

// Product repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

// Category controller
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

}

// Product controller
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    // Implement CRUD operations for products as per requirements
    //category control

        // 1. GET all the categories with pagination
        @GetMapping
        public ResponseEntity<Page<Category>> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Category> categories = categoryRepository.findAll(pageable);
            return ResponseEntity.ok(categories);
        }

        // 2. POST - create a new category
        @PostMapping
        public ResponseEntity<Category> createCategory(@RequestBody Category category) {
            Category createdCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        }

        // 3. GET category by Id
        @GetMapping("/{id}")
        public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            return categoryOptional.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        // 4. PUT - update category by id
        @PutMapping("/{id}")
        public ResponseEntity<Category> updateCategory(@PathVariable("id") Long id,
                                                       @RequestBody Category updatedCategory) {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isPresent()) {
                updatedCategory.setId(id); // Ensure ID is set for update
                Category savedCategory = categoryRepository.save(updatedCategory);
                return ResponseEntity.ok(savedCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // 5. DELETE - Delete category by id
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    // Product controller
    @RestController
    @RequestMapping("/api/products")
    public class ProductController {
        @Autowired
        private ProductRepository productRepository;

        // 1. GET all the products with pagination
        @GetMapping
        public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productRepository.findAll(pageable);
            return ResponseEntity.ok(products);
        }

        // 2. POST - create a new product
        @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody Product product) {
            Product createdProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        }

        // 3. GET product by Id
        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
            Optional<Product> productOptional = productRepository.findById(id);
            return productOptional.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        // 4. PUT - update product by id
        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,
                                                     @RequestBody Product updatedProduct) {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                updatedProduct.setId(id); // Ensure ID is set for update
                Product savedProduct = productRepository.save(updatedProduct);
                return ResponseEntity.ok(savedProduct);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // 5. DELETE - Delete product by id
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }





// Service layer (if needed)

// CategoryService interface
public interface CategoryService {
    List<Category> getAllCategories(Pageable pageable);
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
}

// CategoryServiceImpl class
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category createCategory(Category category) {
        // You can add validation or business logic here before saving
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        // Update existing category fields
        existingCategory.setName(category.getName());
        // You can update other fields as needed
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}

// ProductService interface
public interface ProductService {
    List<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}

// ProductServiceImpl class
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).getContent();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public Product createProduct(Product product) {
        // You can add validation or business logic here before saving
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = getProductById(id);
        // Update existing product fields
        existingProduct.setName(product.getName());
        // You can update other fields as needed
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}

// Pagination utility class (for server-side pagination)

public class PaginationUtil<T> {

    public PaginationResponse<T> paginate(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasPrevious(),
                page.hasNext()
        );
    }

    public PaginationResponse<T> paginate(List<T> content, Pageable pageable, long totalElements) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PaginationResponse<>(
                content,
                pageNumber,
                pageSize,
                totalElements,
                totalPages,
                pageNumber > 0,
                pageNumber < totalPages - 1
        );
    }
}



@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    public PaginationResponse(List<T> content,
                              int pageNumber,
                              int pageSize,
                              long totalElements,
                              int totalPages,
                              boolean b,
                              boolean b1) {
    }
}


@RestController
public class YourController {

    @Autowired
    private YourService yourService;

    @GetMapping("/api/categories")
    public PaginationResponse<Category> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoriesPage = yourService.getAllCategories(pageable);
        PaginationUtil<Category> paginationUtil = new PaginationUtil<>();
        return paginationUtil.paginate(categoriesPage);
    }

    @GetMapping("/api/products")
    public PaginationResponse<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = yourService.getAllProducts(pageable);
        PaginationUtil<Product> paginationUtil = new PaginationUtil<>();
        return paginationUtil.paginate(productsPage);
    }
}




// Main Spring Boot application class
@SpringBootApplication
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
