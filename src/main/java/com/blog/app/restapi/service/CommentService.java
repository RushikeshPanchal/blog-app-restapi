package com.blog.app.restapi.service;

import java.util.List;

import com.blog.app.restapi.dto.CommentDto;

public interface CommentService {
	
	CommentDto createComment(long postId, CommentDto commentDto);
	
	List<CommentDto> getCommentsByPostId(long postId);
	
	CommentDto getCommentById(long postId, long commentId);
	
	CommentDto updateComment(long postId, long commentId, CommentDto commentDto);
	
	public void  deleteComment(long postId, long commentId);

}
