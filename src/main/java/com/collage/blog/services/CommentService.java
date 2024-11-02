package com.collage.blog.services;

import com.collage.blog.payloads.CommentDto;

public interface CommentService {

	public CommentDto createComment(CommentDto commentDto,Integer postId);
	
	public void deleteComment(Integer commentId);
}
