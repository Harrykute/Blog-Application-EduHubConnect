package com.collage.blog.payloads;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDto {
	
	private Integer postId;
	
	private String title;
	
	private String content;
	
	private String imageName="defualt.png";
	
	private CategoryDto categoryDto;
	
	private UserDto userDto;
	
	private Date addedDate;
	
	private Set<CommentDto> comments = new HashSet<>();
	
	private Integer likeCount;

}
