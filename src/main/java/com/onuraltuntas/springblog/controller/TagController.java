package com.onuraltuntas.springblog.controller;

import com.onuraltuntas.springblog.entity.Post;
import com.onuraltuntas.springblog.entity.Tag;
import com.onuraltuntas.springblog.model.dto.PostDTO;
import com.onuraltuntas.springblog.model.payload.request.PostRequest;
import com.onuraltuntas.springblog.repository.TagRepository;
import com.onuraltuntas.springblog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;
    private final TagService tagService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTag(@Valid @RequestBody Tag tag, @RequestParam(value = "user-id")Long userId){

        if(userId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(tagRepository.save(tag));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTag(@Valid @RequestBody Tag tag, @RequestParam(value = "tag-id")Long tagId
            , @RequestParam(value = "user-id")Long userId){

        if(userId == null || tagId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        return ResponseEntity.ok().body(tagService.updateTag(tag,tagId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTag(@RequestParam(value = "tag-id")Long tagId){

        if(tagId == null){
            return ResponseEntity.badRequest().body("Bad request!");
        }

        tagService.deleteTag(tagId);

        return ResponseEntity.ok().body("success");

    }


}
