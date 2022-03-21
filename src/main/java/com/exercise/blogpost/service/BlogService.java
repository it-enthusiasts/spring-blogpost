package com.exercise.blogpost.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exercise.blogpost.model.Blog;
import com.exercise.blogpost.repository.BlogRepository;

@Service
public class BlogService {

	private static final Logger logger = LoggerFactory.getLogger(BlogService.class);

	@Autowired
	BlogRepository blogRepository;

	public Blog getBlogById(final Long id) {
		return blogRepository.findById(id).orElse(null);
	}

	/**
	 * Async Service to update the blog
	 * 
	 * @param blog the blog data to update
	 * @return future object that will have state after completion of task.
	 */
	@Async
	public CompletableFuture<Blog> createOrUpdateBlog(final Blog blog) throws InterruptedException {
		// validate request if id is <0 invalid argument.
		logger.info("Creating/Updateing blog post " + blog);
		final Blog result = (blog.getId() != null && blog.getId() > 0) ? updateBlog(blog) : createBlog(blog);
		// Artificial delay of 1s for demonstration purposes
		Thread.sleep(3000);
		logger.info(" Blog post  updated. isSuccess:" + (result != null));
		return CompletableFuture.completedFuture(result);
	}

	public Blog createBlog(final Blog blog) {
		return blogRepository.save(new Blog(blog.getTitle(), blog.getAuthor(), blog.getContent(), blog.getPostDate()));
	}

	public Blog updateBlog(final Blog blog) {
		final Optional<Blog> blogData = blogRepository.findById(blog.getId());
		if (blogData.isPresent()) {
			final Blog _blog = blogData.get();
			_blog.setTitle(blog.getTitle());
			_blog.setAuthor(blog.getAuthor());
			_blog.setContent(blog.getContent());
			_blog.setPostDate(blog.getPostDate());
			return blogRepository.save(_blog);
		}
		return null;
	}
}
