package com.myfarmblog.farmnews.service.impl;

import com.myfarmblog.farmnews.entity.Comment;
import com.myfarmblog.farmnews.entity.Post;
import com.myfarmblog.farmnews.exceptions.BlogAPIException;
import com.myfarmblog.farmnews.exceptions.ResourceNotFoundException;
import com.myfarmblog.farmnews.payload.requests.CommentRequest;
import com.myfarmblog.farmnews.repository.CommentRepository;
import com.myfarmblog.farmnews.repository.PostRepository;
import com.myfarmblog.farmnews.service.CommentService;
import com.myfarmblog.farmnews.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentImp implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Override
    public CommentRequest createComment(long postId, CommentRequest commentRequest) {
        //convert comments gotten in commentRequest to comment entity inorder to save to the db
        Comment comment = mapToEntity(commentRequest);
        //confirm if post exists and throw exception if it doesn't.
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post","id", postId));
        comment.setPost(post);
        //save comment
        Comment saveComment = commentRepository.save(comment);

        return mapToDto(saveComment);
    }

    @Override
    public List<CommentRequest> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentRequest getCommentById(Long postId, Long commentId) {
        //retrieve post and comment by id
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.NOT_FOUND, AppConstants.COMMENT_NOT_FOUND);
        }
        return mapToDto(comment);
    }

    @Override
    public CommentRequest updateComment(Long postId, long commentId, CommentRequest commentRequest) {
        //retrieve post and comment by id
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.NOT_FOUND,AppConstants.COMMENT_NOT_FOUND);
        }
        comment.setName(commentRequest.getName());
        comment.setBody(commentRequest.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //retrieve post and comment by id
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.NOT_FOUND,AppConstants.COMMENT_NOT_FOUND);
        }
        commentRepository.delete(comment);
    }

    private CommentRequest mapToDto(Comment comment){
        return CommentRequest.builder()
                .id(comment.getId())
                .name(comment.getName())
                .email(comment.getEmail())
                .body(comment.getBody())
                .build();
    }
    private Comment mapToEntity(CommentRequest request){
        return Comment.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .body(request.getBody())
                .build();
    }
}
