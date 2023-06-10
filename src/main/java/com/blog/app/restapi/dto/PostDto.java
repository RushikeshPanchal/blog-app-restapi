package com.blog.app.restapi.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostDto {
	private Long id;
	// title should not be empty or null, title should have atleast 3 characters
	@NotEmpty
	@Size(min = 2, message = "Post title should have at least 3 characters")
	private String title;
	
	@NotEmpty
	@Size(min = 10, message = "Post description should have at least 10 characters")
	private String description;
	
	// post content should not be null or empty
	@NotEmpty
	private String content;
    private Set<CommentDto> comments;
	
}
