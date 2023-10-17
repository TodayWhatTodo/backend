package com.project.todayWhatToDo.post.domain;

import com.project.todayWhatToDo.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AttachFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @Builder
    public AttachFile(String origFilename, String filename, String filePath, Post post) {
        this.origFilename = origFilename;
        this.filename = filename;
        this.filePath = filePath;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
        post.getAttachFiles().add(this);
    }


//    public static AttachFile of(Post post) {
//        return AttachFile.builder()
//                .post(post)
//                .build();
//
//    }
}

