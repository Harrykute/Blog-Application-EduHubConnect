package com.collage.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.collage.blog.config.ApplicationConstant;
import com.collage.blog.entities.Role;
import com.collage.blog.repositories.RoleRepo;

@SpringBootApplication
public class BlogAppClgApplication {

	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogAppClgApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}
	
	public void run(String... args) throws Exception{
		
		try {
			
			Role role = new Role();
			role.setId(ApplicationConstant.ROLE_ADMIN);
			role.setName("ADMIN");
			
			Role role1 = new Role();
			role1.setId(ApplicationConstant.ROLE_USER);
			role1.setName("USER");
			
			List<Role> roles = List.of(role,role1);
			
			List<Role> result = this.roleRepo.saveAll(roles);
			
			System.out.println(result);
			
		}catch(Exception ex) {
			
		}
	}

}
