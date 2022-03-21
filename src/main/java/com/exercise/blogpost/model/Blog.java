package com.exercise.blogpost.model;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "blog")
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "title")
	@NotBlank(message = "Title is mandatory")
	private String title;

	@Column(name = "author")
	@NotBlank(message = "Author is mandatory")
	private String author;

	@Column(name = "content")
	@NotBlank(message = "Content is mandatory")
	private String content;
	
	@Column(name = "postDate")
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime postDate;

	public Blog() {
	}

	public Blog(final String title, final String author, final String content, final LocalDateTime postDate) {
		this.title = title;
		this.author = author;
		this.content = content;
		this.postDate = postDate;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getPostDate() {
		return postDate;
	}

	public void setPostDate(LocalDateTime postDate) {
		this.postDate = postDate;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", author=" + author + ", content=" + content + ", postDate="
				+ postDate + "]";
	}

}