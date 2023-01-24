package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Tag;

public interface TagService {

    Tag updateTag(Tag tag, Long tagId);
    void deleteTag(Long tagId);



}
