package com.collage.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.blog.entities.Category;
import com.collage.blog.entities.Post;
import com.collage.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer>{

	public List<Post> findByUser(User user);
	
	public List<Post> findByCategory(Category category);
	
	public List<Post> findByTitleContaining(String title);
}
