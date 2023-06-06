package com.blog.app.restapi.service;

import com.blog.app.restapi.dto.PostDto;
import com.blog.app.restapi.dto.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto postDto);
	
	//List<PostDto> getAllPosts();
	
	PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir); // provide paging and sorting support to get request,, List<PostDto> getAllPosts(int pageNo, int pageSize);
	
	PostDto getPostById(long id);
	
	PostDto updatePost(PostDto postDto,long id);
	
	void deletePostById(long id);
	
	// default pageNo= 0 and pageSize=10 in api documentation

}
