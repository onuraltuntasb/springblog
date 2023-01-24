package com.onuraltuntas.springblog.model.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PostRequest {
    private String header;
    private String content;
    private Date createDate;
    private Date lastUpdateDate;
    private List<PostListIdRequest> tags;
}
