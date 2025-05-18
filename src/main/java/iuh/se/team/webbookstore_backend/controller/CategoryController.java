package iuh.se.team.webbookstore_backend.controller;

import iuh.se.team.webbookstore_backend.dao.CategoryRepository;
import iuh.se.team.webbookstore_backend.dto.CategoryDTO;
import iuh.se.team.webbookstore_backend.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}