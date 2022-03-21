package com.exercise.blogpost.controller;

import java.util.concurrent.CompletableFuture;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.exercise.blogpost.model.Blog;
import com.exercise.blogpost.service.BlogService;

@RestController
@RequestMapping("/api")
public class BlogController {
	private static final Logger logger = LoggerFactory.getLogger(BlogController.class);
	@Autowired
	BlogService blogService;

	/**
	 * Fetches the blog post associated with the id
	 * 
	 * @param id the id to fetch blog post for
	 * @return the blog post
	 */
	@GetMapping("/blog/{id}")
	public ResponseEntity<Blog> getBlogById(@PathVariable("id") Long id) {
		final Blog blogData = blogService.getBlogById(id);
		if (blogData == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(blogData, HttpStatus.OK);

	}

	/**
	 * As a good REST practice put api as suggested below could be used for
	 * modifying a blog.
	 * 
	 * Project asks for single API for both Update and create so following
	 * assumptions are made. 1) If there is no Id in the blog it is considered as
	 * blog creation request. Response will have id for future updates 3) Blog must
	 * have
	 * 
	 * @param blog the blog details for creation or updation
	 * @return the blog with id and details after update.
	 */
	// TODO: Remove Exception
	@PostMapping("/blog")
	public ResponseEntity<Blog> createOrUpdateBlog(@Valid @RequestBody Blog blog) throws Exception {
		logger.info("Post request blog post " + blog);
		final CompletableFuture<Blog> createTask = blogService.createOrUpdateBlog(blog);
		logger.info("Waiting for async Post task to complete " + blog);
		/**
		 * ASSUMPTION: This api design is that consumer needs result of blog creation (
		 * blog id the identifier) so wait. However, if id is always supplied by the
		 * caller and consumer doesn't need the status than we can return immediately
		 * without waiting for the async thread to complete.
		 */
		CompletableFuture.allOf(createTask).join();
		try {
			final Blog blogData = createTask.get();
			if (blogData == null) {
				logger.info("Async post task complete with error for request blog: " + blog);
				return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
			}
			logger.info("Async post task complete for  blog: " + blogData);
			return new ResponseEntity<>(blogData, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

	}

	// Not used but could be considered for modifying a blog.
	@PutMapping("/blog/{id}")
	public ResponseEntity<Blog> updateBlog(@PathVariable("id") long id, @Valid @RequestBody Blog blog) {
		Blog blogData = blogService.updateBlog(blog);
		if (blogData == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(blogData, HttpStatus.OK);
	}

}