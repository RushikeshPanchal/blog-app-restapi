package com.blog.app.restapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;   
       
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.blog.app.restapi.dto.CommentDto;
import com.blog.app.restapi.entity.Comment;
import com.blog.app.restapi.entity.Post;
import com.blog.app.restapi.exception.BlogAPIException;
import com.blog.app.restapi.exception.ResourceNotFoundException;
import com.blog.app.restapi.repository.CommentRepository;
import com.blog.app.restapi.repository.PostRepository;
import com.blog.app.restapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired // 4.3v @Autowired annotation is no need constructor injection when there is only one constructor defined in the class
	private CommentRepository commentRepository;
	private PostRepository postRepository;
	private ModelMapper mapper;
	
	public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
		super();
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.mapper = mapper;
	}
	
	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		// assigned dto to entity
        Comment comment = mapToEntity(commentDto);
        
        // retrieve post entity by Id,    if post not found so how to comment
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));
        
        // set post to comment entity
        
        comment.setPost(post);
        
        
        Comment newComment = commentRepository.save(comment);
        // convert entity to dto, back to controller
        return mapToDto(newComment);
	}

	@Override
	public List<CommentDto> getCommentsByPostId(long postId) {
		// retrieve comments by postId
		List<Comment> comments = commentRepository.findByPostId(postId);
		// convert list of comment entity to commentdto
		return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList()) ;
	}

	@Override
	public CommentDto getCommentById(long postId, long commentId) {
		 // retrieve post entity by Id,    if post not found so how to comment
        Post post = postRepository.findById(postId).orElseThrow(
        		    ()-> new ResourceNotFoundException("Post","Id",postId));
        
        // retrieve comments By Id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
        		()-> new ResourceNotFoundException("Comment", "Id", commentId));
        
        if(!comment.getPost().getId().equals(post.getId())) {
        	throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
		return mapToDto(comment);
	}

	@Override
	public CommentDto updateComment(long postId, long commentId, CommentDto commentRequest) {
		 // retrieve post entity by Id,    if post not found so how to comment
        Post post = postRepository.findById(postId).orElseThrow(
        		    ()-> new ResourceNotFoundException("Post","Id",postId));
        
        // retrieve comments By Id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
        		()-> new ResourceNotFoundException("Comment", "Id", commentId));
        
        if(!comment.getPost().getId().equals(post.getId())) {
        	throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        
        // updated comment jpa entity by commentdto
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        
        Comment updatedComment = commentRepository.save(comment);
        // send back comment dto to controller
		return mapToDto(updatedComment);
	}

	@Override
	public void deleteComment(long postId, long commentId) {
		 // retrieve post entity by Id,  
        Post post = postRepository.findById(postId).orElseThrow(
        		    ()-> new ResourceNotFoundException("Post","Id",postId));
        
        // retrieve comments By Id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
        		()-> new ResourceNotFoundException("Comment", "Id", commentId));
        
        if(!comment.getPost().getId().equals(post.getId())) {
        	throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        
        commentRepository.delete(comment);
       
	}
	
	// converting CommentDto to entity mapping methods created for all Http methos to reuse the same

	private CommentDto mapToDto(Comment comment) {
		CommentDto commentDto = mapper.map(comment, CommentDto.class);
		/*
		 * CommentDto commentDto = new CommentDto(); commentDto.setId(comment.getId());
		 * commentDto.setName(comment.getName());
		 * commentDto.setEmail(comment.getEmail());
		 * commentDto.setBody(comment.getBody());
		 */
		return commentDto;
	}
	
	//creating comment entity to commentDto mapping method to send back to controller (Code reusability), not exposing entity to outside

	private Comment mapToEntity(CommentDto commentDto) {
		Comment comment = mapper.map(commentDto, Comment.class);
		/*
		 * Comment comment = new Comment(); comment.setId(commentDto.getId());
		 * comment.setName(commentDto.getName());
		 * comment.setEmail(commentDto.getEmail());
		 * comment.setBody(commentDto.getBody());
		 */
		return comment;
	}

}
