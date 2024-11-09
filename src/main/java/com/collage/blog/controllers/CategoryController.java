package com.collage.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collage.blog.payloads.ApiResponse;
import com.collage.blog.payloads.CategoryDto;
import com.collage.blog.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		
		CategoryDto createdCategoryDto = this.categoryService.createCategory(categoryDto);
		
		return new ResponseEntity<CategoryDto>(createdCategoryDto,HttpStatus.CREATED); 
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable("categoryId") Integer cateId){
		
		CategoryDto updatedcategoryDto = this.categoryService.updateCategory(categoryDto, cateId);
		
		return new ResponseEntity<CategoryDto>(updatedcategoryDto,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer categoryId){
        
		this.categoryService.deleteCategory(categoryId);
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("category deleted successfully",false),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('USER') and hasAuthority('ADMIN')")
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleCategory(@PathVariable("categoryId") Integer categoryId){
		CategoryDto categoryDto = this.categoryService.getSingleCategory(categoryId);
		
		return new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('USER') and hasAuthority('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getCategories(){
		List<CategoryDto> categories = this.categoryService.getCategories();
		return new ResponseEntity<List<CategoryDto>>(categories,HttpStatus.OK);
	}

}
