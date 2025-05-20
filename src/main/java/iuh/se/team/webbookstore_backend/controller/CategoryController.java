package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dao.CategoryRepository;
import iuh.se.team.webbookstore_backend.dto.CategoryDTO;
import iuh.se.team.webbookstore_backend.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

//    @GetMapping
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(new CategoryDTO(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category) {
        category.setCategoryId(id);
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryRepository.deleteById(id);
    }

}