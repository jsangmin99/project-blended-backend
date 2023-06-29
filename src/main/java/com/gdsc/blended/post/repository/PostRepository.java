package com.gdsc.blended.post.repository;

import com.gdsc.blended.post.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p ORDER BY p.id DESC")
    List<PostEntity> findAllDesc();

    Page<PostEntity> findAllByOrderByLikeCountDesc(Pageable pageable);

    //@Query("SELECT p FROM PostEntity p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<PostEntity> findByTitleOrContentContaining(String titleKeyword, String contentKeyword);
}
