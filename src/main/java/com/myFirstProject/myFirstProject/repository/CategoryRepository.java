package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
