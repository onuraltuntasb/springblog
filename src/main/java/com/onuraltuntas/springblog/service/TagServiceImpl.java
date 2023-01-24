package com.onuraltuntas.springblog.service;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.Tag;
import com.onuraltuntas.springblog.exception.ResourceNotFoundException;
import com.onuraltuntas.springblog.repository.PostRepository;
import com.onuraltuntas.springblog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Override
    public Tag updateTag(Tag tag, Long tagId) {

        Tag rTag = tagRepository.findById(tagId).orElseThrow(
                ()-> new ResourceNotFoundException("Tag not found with this id : "+ tagId)
        );

        rTag.setName(tag.getName());

        return tagRepository.save(rTag);
    }

    @Override
    public void deleteTag(Long tagId) {


        Set<Long> tags = new HashSet<>();

        tags.add(tagId);

        Set<Post> posts = postRepository.findPostsByTagsIn(tags);
        List<Tag> tagsList = tagRepository.findAll();



        for (Post post:posts) {
            log.info("Posts : {}",post.getHeader());
            post.removeTag(tagId);
        }

        //tagRepository.deleteById(tagId);

    }


}
