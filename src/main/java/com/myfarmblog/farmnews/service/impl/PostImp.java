package com.myfarmblog.farmnews.service.impl;

import com.myfarmblog.farmnews.entity.model.Post;
import com.myfarmblog.farmnews.exceptions.ResourceNotFoundException;
import com.myfarmblog.farmnews.payload.requests.PostRequest;
import com.myfarmblog.farmnews.payload.response.PostResponse;
import com.myfarmblog.farmnews.repository.PostRepository;
import com.myfarmblog.farmnews.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostImp implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    @Override
    public PostRequest createBlogPost(PostRequest request) {
        /*create post with the post dto, map it to the entity to be able to save it to the DB and then map the saved
        post back to postRequest while exposing it to the endpoint. This is done so that the DB won't be exposed to the endpoint
        */
        Post createPost = mapToEntity(request);
        Post savePost = postRepository.save(createPost);
        return mapToDto(savePost);
    }

    @Override
    public PostResponse getAllBlogPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        //for sorting
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //for pagination
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        //step 1: get all the blog posts on the database
        Page<Post> allBlogPostInDb = postRepository.findAll(pageable );
        //to retrieve the list of content in the page object, use the get content method
        List<Post> postList = allBlogPostInDb.getContent();
        //step 2: convert posts gotten from the db to dto(remember you don't ever expose your db to the endpoints)
        //returned using method reference Or Lambda
        List<PostRequest> content= postList.stream().map(this::mapToDto).toList();
        // return allBlogPostInDb.stream().map(post -> mapToDto(post)).collect(Collectors.toList()); using lambda
        PostResponse response = new PostResponse();
        response.setContent(content);
        response.setPageNo(allBlogPostInDb.getNumber());
        response.setPageSize(allBlogPostInDb.getSize());
        response.setTotalPage(allBlogPostInDb.getTotalPages());
        response.setTotalElement(allBlogPostInDb.getTotalElements());
        response.setLast(allBlogPostInDb.isLast());
        return response;

    }

    @Override
    public PostRequest getPostById(long id) {
        //get post by id,if post doesn't exist throw exception.
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        //map post from the Db to dto,Remember!! never expose your DB directly to the end
        return mapToDto(post);
    }

    @Override
    public PostRequest updatePost(PostRequest postRequest, long id) {
        //to update post, find the post in the DB using the post's id. If post doesn't exist throw exception
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        //update by setting new values for posts
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setContent(postRequest.getContent());
        //save updated posts to the DB
        Post updatedPost = postRepository.save(post);
        //map back to dto to avoid direct db exposure to the endpoint.
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    private  PostRequest mapToDto (Post post){
        return modelMapper.map(post,PostRequest.class);
    }
    private Post mapToEntity(PostRequest request){
        return modelMapper.map(request, Post.class);
    }
}

