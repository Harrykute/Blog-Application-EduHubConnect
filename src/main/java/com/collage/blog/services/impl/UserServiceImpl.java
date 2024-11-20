package com.collage.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.collage.blog.config.ApplicationConstant;
import com.collage.blog.entities.Role;
import com.collage.blog.entities.User;
import com.collage.blog.exception.ResourceNotFoundException;
import com.collage.blog.payloads.UserDto;
import com.collage.blog.repositories.RoleRepo;
import com.collage.blog.repositories.UserRepo;
import com.collage.blog.services.JWTService;
import com.collage.blog.services.UserService;


//created implemented userService interface for overriding interface method in implementation class 
//it will process the bussiness logic here 
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo; // it will create the object of implemented class of userRepo and provide methods for data process
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Autowired 
	private AuthenticationManager authManger;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		user.setPassword(encoder.encode(userDto.getPassword()));
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
	
		User user = this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User"," id ",userId)));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());

		User updatedUser = this.userRepo.save(user);
		
		UserDto userDto1  = this.userToDto(updatedUser);
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow((()-> new ResourceNotFoundException("User"," id ",userId)));
		UserDto userDto  = this.userToDto(user);
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
	
		List<User> users = this.userRepo.findAll();
		
		List<UserDto> usersDto = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		
		return usersDto;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		User user =this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","userId", userId));
		
		this.userRepo.deleteById(userId);
	}
	
	//it will convert the useDto object into user beacuse user dto will come from request 
	public User dtoToUser(UserDto userDto) {
		User user =  new User();
		user.setId(userDto.getId());
		user.setName(userDto.getName());
		user.setPassword(userDto.getPassword());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		
		return user;
	}
	
	//it used to send response as DTO 
	public UserDto userToDto(User user) {
		UserDto userDto =  new UserDto();
		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setPassword(user.getPassword());
		userDto.setEmail(user.getEmail());
		return userDto;
	}
	
	public String verify(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);
		
		Authentication authentication = authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getName(),user.getPassword()));
		
		if(authentication.isAuthenticated())
		{	
			return jwtService.generateToken(user.getName());
		}else {
			return "fail";
		}

		}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);
		
		user.setPassword(encoder.encode(userDto.getPassword()));
		
		Role role = this.roleRepo.findById(ApplicationConstant.ROLE_USER).orElseThrow(()-> new ResourceNotFoundException("Role","roleId", ApplicationConstant.ROLE_USER));
		
		user.getRoles().add(role);
		
		User saveUser = this.userRepo.save(user);
		
		UserDto createdUser = this.modelMapper.map(saveUser,UserDto.class);
		
		return createdUser;
	}

}
