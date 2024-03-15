package com.myfarmblog.farmnews.controller;

import com.myfarmblog.farmnews.payload.requests.CommentRequest;
import com.myfarmblog.farmnews.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentRequest> createComment(@PathVariable(value = "postId") long postId,
                                 @RequestBody CommentRequest commentRequest){
      return new ResponseEntity<>(commentService.createComment(postId, commentRequest),HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentRequest>getCommentsByPostId(@PathVariable(value = "postId") long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRequest>getCommentById(@PathVariable(value = "postId")Long postId,
                                                        @PathVariable(value = "id") Long commentId){
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId),HttpStatus.OK);
    }
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRequest>updateComment(@PathVariable(value = "postId") Long postId,
                                                       @PathVariable(value = "id") long commentId,
                                                       @RequestBody CommentRequest request){
        return new ResponseEntity<>(commentService.updateComment(postId, commentId, request),HttpStatus.OK);
    }
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long commentId){
      commentService.deleteComment(postId, commentId);
      return new ResponseEntity<>("comment deleted",HttpStatus.OK);
    }
}
