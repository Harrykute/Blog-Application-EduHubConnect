package com.collage.blog.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//creating this class for return details of page ex. totalElement toalcount etc.

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

	private List<PostDto> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElement;
	private Integer totalPages;
	private boolean lastPage;
	
}
