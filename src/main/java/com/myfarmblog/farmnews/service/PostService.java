package com.myfarmblog.farmnews.service;

import com.myfarmblog.farmnews.payload.requests.PostRequest;
import com.myfarmblog.farmnews.payload.response.PostResponse;

import java.util.List;

public interface PostService {
    PostRequest createBlogPost (PostRequest request);
    PostResponse getAllBlogPost(int pageNo, int pageSize, String sortBy, String sortDir);
    PostRequest getPostById (long id);
    PostRequest updatePost(PostRequest postRequest, long id);
    void  deletePost(long id);
}
