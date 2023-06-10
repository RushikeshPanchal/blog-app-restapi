package com.blog.app.restapi.controller;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.restapi.dto.CommentDto;
import com.blog.app.restapi.service.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId")long postId,
			                                        @Valid @RequestBody CommentDto commentDto) {
		return new ResponseEntity<CommentDto> (commentService.createComment(postId, commentDto), HttpStatus.CREATED);
	}
	
	// GET = get all comments by postId
	@GetMapping("/posts/{postId}/comments")
	public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId")long postId){
		return commentService.getCommentsByPostId(postId);
	}
	
	//GET = get comment by Id if it belong to post with Id = postId
	@GetMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> getCommentById(@PathVariable (value = "postId") long postId,
			                                         @PathVariable (value = "id") long commentId){
		CommentDto commentDto = commentService.getCommentById(postId, commentId);
		return new ResponseEntity<CommentDto>(commentDto, HttpStatus.OK);
	}
	
	// update existing comment by comment Id that belongs to sp post.
	@PutMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<CommentDto> updateComment(@PathVariable (value = "postId") long postId, 
			                                        @PathVariable (value = "id") long commentId , 
			                                        @Valid @RequestBody CommentDto commentDto){
	CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
	
	return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.OK);
   }
	
	// delete comment by commentId and check condns of postId
	@DeleteMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable (value = "postId") long postId, 
                                                @PathVariable (value = "id") long commentId){
		commentService.deleteComment(postId, commentId);
		return new ResponseEntity<>("Comment deleted successfully",HttpStatus.OK);
	}
}
