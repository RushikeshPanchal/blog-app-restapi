package com.blog.app.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.restapi.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
