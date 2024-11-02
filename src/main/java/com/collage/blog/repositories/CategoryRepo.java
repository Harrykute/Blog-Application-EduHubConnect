package com.collage.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
