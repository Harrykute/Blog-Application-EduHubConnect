package com.collage.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.blog.entities.PostLike;

public interface PostLikeRepo extends JpaRepository<PostLike, Integer>  {

	 Optional<PostLike> findByPostIdAndUserId(Integer postId, Integer userId);
}
