package com.example.goguma.dto;

import com.example.goguma.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    private MultipartFile file;
    private Object files;
    private String title;
    private String price;
    private String date;
    private String content;
    private String address;

    public Post toEntity() {
        String title = this.getTitle();
        int price = Integer.parseInt(this.getPrice().replace(",", ""));
        String date = this.getDate();
        String content = this.getContent();
        String address = this.getAddress();

        return new Post(title, price, content, address, date);
    }
}
