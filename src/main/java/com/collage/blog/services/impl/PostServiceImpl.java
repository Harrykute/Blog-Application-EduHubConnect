package com.collage.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.collage.blog.entities.Category;
import com.collage.blog.entities.Post;
import com.collage.blog.entities.User;
import com.collage.blog.exception.ResourceNotFoundException;
import com.collage.blog.payloads.CategoryDto;
import com.collage.blog.payloads.PostDto;
import com.collage.blog.payloads.PostResponse;
import com.collage.blog.payloads.UserDto;
import com.collage.blog.repositories.CategoryRepo;
import com.collage.blog.repositories.PostRepo;
import com.collage.blog.repositories.UserRepo;
import com.collage.blog.services.PostService;

import jakarta.annotation.PostConstruct;

@Service
public class PostServiceImpl implements PostService{
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	

	@Override
	public PostDto createPost(PostDto postDto , Integer userId , Integer categoryId) {

		 User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","UserId", userId));
		 
		 Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));
		 
		 
//		 UserService userService = new UserServiceImpl();
//		 Post post = this.postDtoToPost(postDto,user,category);
//		 Post createdPost = this.postRepo.save(post);
//		 PostDto postDtoRes = this.postToPostDto(createdPost,user,category);
		 
//		 Post post = this.modelMapper.map(postDto,Post.class);
//		 post.setImageName("default.png");
//		 post.setAddedDate(new Date());
//		 post.setUser(user);
//		 post.setCategory(category);
//		 
//		 Post newPost = this.postRepo.save(post);
//		 
//		 PostDto createdPostDto = this.modelMapper.map(newPost,PostDto.class);
//		 CategoryDto categaryDto = this.modelMapper.map(category,CategoryDto.class);
//		 createdPostDto.setCategoryDto(categaryDto);
//		 UserDto userDto = this.modelMapper.map(user,UserDto.class);
//		 createdPostDto.setUserDto(userDto);
//		 
//		return createdPostDto;

		 Post post = this.modelMapper.map(postDto, Post.class);
	        post.setImageName("default.png");
	        post.setAddedDate(new Date());
	        post.setContent(postDto.getContent());
	        post.setUser(user);
	        post.setCategory(category);
	        Post newPost = this.postRepo.save(post);
	        return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto,Integer postId) {
		
        Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));

        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        
        if(postDto.getImageName()!= null) {
        	post.setImageName(postDto.getImageName());
        }
        Post updatePost = this.postRepo.save(post);
        
        PostDto updatedPost = this.modelMapper.map(updatePost,PostDto.class);

		return updatedPost;
	}

	@Override
	public void deletePost(Integer postId) {
		
        Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));

        this.postRepo.delete(post);
	}

	@Override
	public PostDto getSinglePost(Integer postId) {

        Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
		
        PostDto postDto = this.modelMapper.map(post,PostDto.class);
		
		return postDto;
	}

	@Override
	public List<PostDto> getPosts(Integer pageNumber,Integer pageSize) {
		
//		List<Post> posts = this.postRepo.findAll();
//		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

		Pageable p = PageRequest.of(pageNumber-1,pageSize);
		
		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allPost = pagePost.getContent();
		
        List<PostDto> postDtos = allPost.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

		return postDtos;
	}
	
	
	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId));
		
		List<Post> posts = this.postRepo.findByCategory(category);
		
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		
		 User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","UserId", userId));
		  
		 List<Post> posts = this.postRepo.findByUser(user);
		 
		  List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
	    
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}
	
	public PostDto postToPostDto(Post post,User user , Category category) {
		
		PostDto postDto = new PostDto();
		postDto.setPostId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setContent(post.getContent());
		postDto.setImageName("defualt.png");
		 if (post.getCategory() != null) {
		        CategoryDto categoryDto = new CategoryDto();
		        categoryDto.setCategoryId(category.getCategoryId());
		        categoryDto.setCategoryTitle(category.getCategoryTitle());
		        categoryDto.setCategoryDescription(category.getCategoryDescription());
		        postDto.setCategoryDto(categoryDto);
		    }

		    // Map User to UserDto
		    if (post.getUser() != null) {
		        UserDto userDto = new UserDto();
		        userDto.setId(user.getId());
		        userDto.setName(user.getName());
		        userDto.setEmail(user.getEmail());
		        userDto.setAbout(user.getPassword());
		        postDto.setUserDto(userDto);
		    }
		    
		    post.setAddedDate(new Date());
		    
		return postDto;
	}
	
	public Post postDtoToPost(PostDto postDto,User user,Category category) {
		
		Post post = new Post();
		post.setId(postDto.getPostId());
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName("defualt.png");
		post.setCategory(category);
		post.setUser(user);
		post.setAddedDate(new Date());
		return post;
	}
	
	@PostConstruct
	public void configureModelMapper() {
	    this.modelMapper.typeMap(Post.class, PostDto.class).addMappings(mapper -> {
	        mapper.map(src -> src.getUser(), PostDto::setUserDto);
	        mapper.map(src -> src.getCategory(), PostDto::setCategoryDto);
	    });

	    this.modelMapper.typeMap(PostDto.class, Post.class).addMappings(mapper -> {
	        mapper.skip(Post::setId); // Optional: if you want to skip mapping ID on create
	        mapper.skip(Post::setAddedDate); // We'll set this manually
	        mapper.skip(Post::setImageName); // Set default image manually
	        mapper.skip(Post::setContent);
	    });
	}

	//we are creating customized getall post method for detailed response
	
     public PostResponse getDeatilAllpost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
    	 
    	    Sort sort =null;
    	    
    	    if(sortDir.equalsIgnoreCase("asc")) {
    	    	sort = sort.by(sortBy).ascending();
    	    }else {
    	    	sort =sort.by(sortBy).descending();
    	    }
    	 
    		Pageable p = PageRequest.of(pageNumber-1,pageSize,sort);
    		
    		Page<Post> pagePost = this.postRepo.findAll(p);
    		List<Post> allPost = pagePost.getContent();
    		
            List<PostDto> postDtos = allPost.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

            PostResponse postResponse = new PostResponse();
            postResponse.setContent(postDtos);
            postResponse.setPageNumber(pagePost.getNumber());
            postResponse.setPageSize(pagePost.getSize());
            postResponse.setTotalElement(pagePost.getTotalElements());
            postResponse.setTotalPages(pagePost.getTotalPages());
            postResponse.setLastPage(pagePost.isLast());
    		return  postResponse;
     }

	
}
