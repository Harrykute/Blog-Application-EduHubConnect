package com.collage.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collage.blog.entities.Comment;
import com.collage.blog.entities.Post;
import com.collage.blog.exception.ResourceNotFoundException;
import com.collage.blog.payloads.CommentDto;
import com.collage.blog.repositories.CommentRepo;
import com.collage.blog.repositories.PostRepo;
import com.collage.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		
        Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
		
        Comment comment = this.modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        Comment save = this.commentRepo.save(comment);
        
        CommentDto createdComment = this.modelMapper.map(save,CommentDto.class);
        
		return createdComment;
	}

	@Override
	public void deleteComment(Integer commentId) {
		
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","commentId",commentId));
		this.commentRepo.delete(comment);
	}

}
