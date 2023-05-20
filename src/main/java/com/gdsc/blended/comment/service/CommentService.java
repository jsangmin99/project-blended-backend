package com.gdsc.blended.comment.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentEntity findById(Long id){
        return commentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid Comment ID"));
    }
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long postId, Long userId){
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 POST를 찾을 수 없습니다."));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));

        CommentEntity comment = CommentEntity.builder()
                .content(requestDto.getContent())
                .post(post)
                .user(user)
                .build();
        CommentEntity savedComment = (CommentEntity) commentRepository.save(comment);

        // Comment 엔티티 저장 로직

        return CommentResponseDto.builder()
                .commentId(savedComment.getId())
                .content(savedComment.getContent())
                .user(savedComment.getUser())
                .modifiedDate(savedComment.getModifiedDate())
                .build();
    }

    public CommentResponseDto getComments(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    public List<CommentResponseDto> getCommentListByPost(Long postId) {
        List<CommentEntity> comments = commentRepository.findByPostId(postId);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (CommentEntity comment : comments) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
            commentResponseDtos.add(commentResponseDto);
        }

        return commentResponseDtos;
    }

    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));

        if(!comment.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }{
            comment.updateContent(requestDto.getContent());
            CommentEntity updatedComment = commentRepository.save(comment);
            return CommentResponseDto.builder()
                    .commentId(updatedComment.getId())
                    .content(updatedComment.getContent())
                    .modifiedDate(updatedComment.getModifiedDate())
                    .build();
        }
    }

    public CommentResponseDto deleteComment(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if(!comment.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }
        {
            comment.deletComment(comment.getContent());

            CommentEntity updatedComment = commentRepository.save(comment);

            return CommentResponseDto.builder()
                    .commentId(updatedComment.getId())
                    .content(updatedComment.getContent())
                    .modifiedDate(updatedComment.getModifiedDate())
                    .build();
        }
    }

    public void realDeleteComment(Long commentId, Long userId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Incalid comment id"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if(!comment.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }
        {
            commentRepository.delete(comment);
        }
    }
}
