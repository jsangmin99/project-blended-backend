package com.gdsc.blended.comment.replies.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepliesResponseDto {
    private Long repliesId;
    private String content;
    private AuthorDto author;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    private LocalDateTime modifiedDate;
}