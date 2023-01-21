package com.onuraltuntas.springblog.model.payload.request;

import com.onuraltuntas.springblog.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostRequest {
    private String header;
    private String content;
    private Date createDate;
    private Date lastUpdateDate;
    private Set<Tag> tags;
}
