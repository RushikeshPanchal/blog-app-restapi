package com.blog.app.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.restapi.dto.PostDto;
import com.blog.app.restapi.dto.PostResponse;
import com.blog.app.restapi.service.PostService;
import com.blog.app.restapi.utils.AppConstants;

@RestController
@RequestMapping("api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	public PostController(PostService postService) {
		super();
		this.postService = postService;
	}
	
	// create blog post method 
	@PostMapping
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
		return new ResponseEntity<PostDto>(postService.createPost(postDto),HttpStatus.CREATED);
	}
	
	// getAll data blog method 
/*	@GetMapping("/getAll")
	public  List<PostDto> getAllPosts(){
		return postService.getAllPosts();
	}*/
	
	// getAll data blog method (localhost:8080/api/posts/getAll), providing support pagination and sorting to this method
	@GetMapping("")
	public  PostResponse getAllPosts(
	            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
	            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
	            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
	            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
	  ){
		return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
	}
	
	// get post by id, specific resource as per ID 
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable (name="id") long id ){
		return ResponseEntity.ok(postService.getPostById(id));
	}
	
	//update existing post by id in REST API 
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable (name="id") long id) {
		PostDto postResponse = postService.updatePost(postDto, id);
		return new ResponseEntity<>(postResponse, HttpStatus.OK);
	}
	
	// delete post rest api 
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable (name="id") long id) {  // its changed not based on service class(void method for delete request))
		postService.deletePostById(id);
		return new ResponseEntity<>("Post entity deleted successfully",HttpStatus.OK);
	}
}
