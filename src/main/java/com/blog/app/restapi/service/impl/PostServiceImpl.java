package com.blog.app.restapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.restapi.dto.PostDto;
import com.blog.app.restapi.dto.PostResponse;
import com.blog.app.restapi.entity.Post;
import com.blog.app.restapi.exception.ResourceNotFoundException;
import com.blog.app.restapi.repository.PostRepository;
import com.blog.app.restapi.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	private PostRepository postRepository;
	
	private ModelMapper mapper;
	
	@Autowired  //(spring 4.3 onwards if class configured as spring bean class(@Service) has 1 constructor argument then no need to use @Autowired
	public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
		super();
		this.postRepository = postRepository;
		this.mapper = mapper;
	}

	

	@Override
	public PostDto createPost(PostDto postDto) {
		
		// converting DTO to entity object for db table mapping 
		Post post = mapToEntity(postDto);
	
		Post newPost = postRepository.save(post);
		
		// convert entity to dto for send back to controller
		PostDto postResponse = mapToDTO(newPost);		
		return postResponse;  // return postDto object to frontend controller
	}

/*
	@Override
	public List<PostDto> getAllPosts() {
		// getting data from db by postrepository
	   List<Post> posts = postRepository.findAll();
	   
	   // converting this post entity to postdto (java 8 stream map)
	  return  posts.stream().map(post ->mapToDTO(post)).collect(Collectors.toList());
	}
*/
	
	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		// 80 line use like type casting :: Pageable pageable = (Pageable) PageRequest.of(pageNo, pageSize);

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		// create pageable Instance
		
		PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);  // class - Pageequest implement Pageable interface   // PageRequest pageable = PageRequest.of(pageNo, pageSize,Sort.by(sortBy)); before abve code
		
		Page<Post> posts = postRepository.findAll(pageable);
		
		// get content for page object 
		List<Post> listPosts = posts.getContent(); 
		List<PostDto> content =   listPosts.stream().map(post ->mapToDTO(post)).collect(Collectors.toList());   // just dummay paging and after customized paging & sort by response class
	    
	   PostResponse postResponse = new PostResponse();
	   postResponse.setContent(content);
	   postResponse.setPageNo(posts.getNumber());
	   postResponse.setPageSize(posts.getSize());
	   postResponse.setTotalElements(posts.getTotalElements());
	   postResponse.setTotalPages(posts.getTotalPages());
	   postResponse.setLast(posts.isLast());
	   
	   return postResponse;
	}
	// get post by id , to fetch specific resource from database as per id as input 
	@Override
	public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id",id));  // findbyid from repo return optional class it has vrious static method or elsethrow is static method
		return mapToDTO(post);
	}
	
	// update by Id, to update existin records in the resorce 
	@Override
	public PostDto updatePost(PostDto postDto, long id) {
		// get post by id from db
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id",id));  
        
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        
        Post updatedPost = postRepository.save(post);
		return mapToDTO(updatedPost);  // updated data sesnd back to controller
	}

	@Override
	public void deletePostById(long id) {
		// get post by id from db
		Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post", "id",id)); 
		
		 postRepository.delete(post);
		
	}
	
	
	// converting PostDto to entity mapping methods created for all Http methos to reuse the same
	
		private PostDto mapToDTO(Post post) {
			PostDto postDto = mapper.map(post, PostDto.class);
			/*
			 * PostDto postDto = new PostDto(); postDto.setId(post.getId());
			 * postDto.setTitle(post.getTitle());
			 * postDto.setDescription(post.getDescription());
			 * postDto.setContent(post.getContent());
			 */
			return postDto;
		}
		
		// creating post entity to postDto mapping method to send back to controller (Code reusability)
		private Post mapToEntity(PostDto postDto) {
			Post post = mapper.map(postDto, Post.class);
			/*
			 * Post post = new Post(); post.setId(postDto.getId());
			 * post.setTitle(postDto.getTitle());
			 * post.setDescription(postDto.getDescription());
			 * post.setContent(postDto.getContent());
			 */
			return post;
			
		}

}
