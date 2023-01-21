package com.onuraltuntas.springblog.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {
    private Long id;
    private String header;
    private String content;
    private Date createDate;
    private Date lastUpdateDate;
    private int likeCount;
    private int dislikeCount;
    private double popular;
}
