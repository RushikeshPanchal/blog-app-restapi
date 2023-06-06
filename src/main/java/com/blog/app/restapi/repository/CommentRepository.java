package com.blog.app.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.restapi.entity.Comment;

@Repository    // JpaRepo extends simple jpa and Jpa Repo alread at top of it @Repo no need to use(@Transactional in service) while class extended jpaRepo
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByPostId(long postId);

}
