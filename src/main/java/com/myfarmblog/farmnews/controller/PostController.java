package com.myfarmblog.farmnews.controller;


import com.myfarmblog.farmnews.payload.requests.PostRequest;
import com.myfarmblog.farmnews.payload.response.PostResponse;
import com.myfarmblog.farmnews.service.PostService;
import com.myfarmblog.farmnews.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("api/posts")

public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostRequest>createPost(@Valid @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postService.createBlogPost(postRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false)int pageNo,
            @RequestParam( value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllBlogPost(pageNo,pageSize,sortBy,sortDir);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostRequest> findPostById(@PathVariable(name = "id") long postId){
        return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PostRequest>updatePost(@Valid @RequestBody PostRequest request,
                                                 @PathVariable(name = "id") long id){
        return new ResponseEntity<>(postService.updatePost(request,id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id" )long id){
        postService.deletePost(id);
        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }
}
