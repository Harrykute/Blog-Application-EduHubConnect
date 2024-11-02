package com.collage.blog.services;

import java.util.List;

import com.collage.blog.entities.Post;
import com.collage.blog.payloads.PostDto;
import com.collage.blog.payloads.PostResponse;

public interface PostService {

	//create 
	public PostDto createPost(PostDto post,Integer userId , Integer categoryId);
	
	public PostDto updatePost(PostDto post,Integer postId);
	
	public void deletePost(Integer postId);
	
	public PostDto getSinglePost(Integer postId);
	
	public List<PostDto> getPosts(Integer pageNumber,Integer pageSize);
	
	public List<PostDto> getPostsByCategory(Integer categoryId);
	
	public List<PostDto> getPostsByUser(Integer userId);
	
	public List<PostDto> searchPosts(String keyword);
	
	public PostResponse getDeatilAllpost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
}
