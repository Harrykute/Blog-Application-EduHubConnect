package com.collage.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collage.blog.entities.Category;
import com.collage.blog.exception.ResourceNotFoundException;
import com.collage.blog.payloads.CategoryDto;
import com.collage.blog.repositories.CategoryRepo;
import com.collage.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		Category category = this.categoryDtoToCategory(categoryDto);
		
		Category createdCategory = this.categoryRepo.save(category);

		return this.categoryToCategoryDto(createdCategory);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));
		
		
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
	
		Category category1 = this.categoryRepo.save(category);
		
		CategoryDto updatedCategory = this.categoryToCategoryDto(category1);
		
		return updatedCategory;
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
		this.categoryRepo.delete(category);
	}

	@Override
	public CategoryDto getSingleCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
		CategoryDto categoryDto = this.categoryToCategoryDto(category);
		return categoryDto;
	}

	@Override
	public List<CategoryDto> getCategories() {
		
	  
		List<Category> allCategories = this.categoryRepo.findAll();
		
		List<CategoryDto> allCategoriesDto = allCategories.stream().map(category->
			this.categoryToCategoryDto(category)
		).collect(Collectors.toList());
		
		return allCategoriesDto;
	}
	
	public CategoryDto categoryToCategoryDto(Category category) {
		
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryId(category.getCategoryId());
		categoryDto.setCategoryTitle(category.getCategoryTitle());
		categoryDto.setCategoryDescription(category.getCategoryDescription());
		return categoryDto;
	}
	
	public Category categoryDtoToCategory(CategoryDto categoryDto) {
		
		Category category = new Category();
		category.setCategoryId(categoryDto.getCategoryId());
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDescription(categoryDto.getCategoryDescription());
		return category;
	}
	

}
