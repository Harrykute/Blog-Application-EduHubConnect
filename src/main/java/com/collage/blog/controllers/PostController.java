package com.collage.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.collage.blog.config.ApplicationConstant;
import com.collage.blog.payloads.ApiResponse;
import com.collage.blog.payloads.PostDto;
import com.collage.blog.payloads.PostResponse;
import com.collage.blog.services.FileService;
import com.collage.blog.services.PostService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/")
public class PostController {
	
  @Autowired
  private PostService postService;
  
  @Autowired
  private FileService fileService;
  
  @Value("${project.image}")
  private String path;
	
	
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,@PathVariable("userId") Integer userId,@PathVariable("categoryId") Integer categoryId){
		PostDto createdPostDto = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPostDto,HttpStatus.CREATED); 
	}
	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,@PathVariable("postId") Integer postId){
		PostDto post = this.postService.updatePost(postDto, postId);
	    return new ResponseEntity<PostDto>(post,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable("userId") Integer userId){
		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId){
		List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getSinglePost(@PathVariable("postId") Integer postId){
		PostDto post = this.postService.getSinglePost(postId);
		return new ResponseEntity<PostDto>(post,HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<List<PostDto>> getAllPost(@RequestParam(value="pageNumber",defaultValue="1",required = false) Integer pageNumber,@RequestParam(value="pageSize",defaultValue = "10",required = false)  Integer pageSize){
		List<PostDto> posts = this.postService.getPosts(pageNumber,pageSize);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable("postId") Integer postId){
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully",false),HttpStatus.OK);
	}
	
	@GetMapping("/postss")
	public ResponseEntity<PostResponse> getDetailedPosts(@RequestParam(value=ApplicationConstant.PAGE_NUMBER,defaultValue="1",required = false) Integer pageNumber,
			@RequestParam(value="pageSize",defaultValue = ApplicationConstant.PAGE_SIZE,required = false)  Integer pageSize,
			@RequestParam(value="sortBy",defaultValue=ApplicationConstant.SORT_BY,required = false) String sortBy ,
			@RequestParam(value="sortDir",defaultValue=ApplicationConstant.SORT_DIR,required = false) String sortDir
			){
		PostResponse postResponse = this.postService.getDeatilAllpost(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
	}
	
	@GetMapping("posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPost(@PathVariable("keyword") String keyword){
		
		List<PostDto> posts = this.postService.searchPosts(keyword);
		
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	
	//images handing methods
	
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId
			) throws IOException{
		
		PostDto post = this.postService.getSinglePost(postId);
		String fileName =  this.fileService.uploadImage(path, image);
		post.setImageName(fileName);
		
		PostDto updatedPost = this.postService.updatePost(post, postId);
		
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
		
	}
	
	@GetMapping(value="/post/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downLoadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
			) throws IOException{
		
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
		
	}

}
