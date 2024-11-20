package com.collage.blog.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collage.blog.entities.Post;
import com.collage.blog.entities.PostLike;
import com.collage.blog.entities.User;
import com.collage.blog.exception.ResourceNotFoundException;
import com.collage.blog.repositories.PostLikeRepo;
import com.collage.blog.repositories.PostRepo;
import com.collage.blog.repositories.UserRepo;
import com.collage.blog.services.PostLikeService;

@Service
public class PostLikeServiceImpl implements PostLikeService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PostLikeRepo postLikeRepo;
	
	
	
	@Override
	public String postLike(Integer postId, Integer userId) {
		
		 User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","UserId", userId));

	     Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
	     
			
			  Optional<PostLike> existingLike = postLikeRepo.findByPostIdAndUserId(postId,
			  userId); if (existingLike.isPresent()) { return
			  "User has already liked this post"; }

	          PostLike postLike = new PostLike();
	          postLike.setPost(post);
	          postLike.setUser(user);
	          postLikeRepo.save(postLike);
	        
	          post.incrementLikeCount();
	          postRepo.save(post);

		return "like Saved Succesfully";
	}

}
