package com.collage.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collage.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findByName(String userName);
}
