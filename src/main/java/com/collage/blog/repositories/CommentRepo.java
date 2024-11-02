package com.collage.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment,Integer>{

}
