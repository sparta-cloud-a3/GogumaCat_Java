package com.example.goguma.service;

import com.example.goguma.dto.PostRequestDto;
import com.example.goguma.model.Post;
import com.example.goguma.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post registerPost(PostRequestDto postRequestDto){
        String title = postRequestDto.getTitle();
        int price = Integer.parseInt(postRequestDto.getPrice().replace(",",""));
        String date = postRequestDto.getDate();
        String content = postRequestDto.getContent();
        String address = postRequestDto.getAddress();
        Post post = new Post(title,price,content,0,address,date,false);

        postRepository.save(post);

        return post;
    }
}
