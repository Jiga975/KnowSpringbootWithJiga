package com.myfarmblog.farmnews.service;

import com.myfarmblog.farmnews.payload.requests.CommentRequest;

import java.util.List;

public interface CommentService {
    CommentRequest createComment (long postId, CommentRequest commentRequest);
    List<CommentRequest>getCommentsByPostId(long postId);
    CommentRequest getCommentById(Long postId, Long commentId);
    CommentRequest updateComment(Long postId, long commentId, CommentRequest commentRequest);
    void deleteComment (Long postId, Long commentId);

}
