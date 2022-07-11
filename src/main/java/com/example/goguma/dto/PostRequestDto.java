package com.example.goguma.dto;

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

    @Override
    public String toString() {
        return "PostRequestDto{" +
                "file=" + file +
                ", files=" + files +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
