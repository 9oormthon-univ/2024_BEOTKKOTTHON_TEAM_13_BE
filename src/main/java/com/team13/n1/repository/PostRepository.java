package com.team13.n1.repository;

import com.team13.n1.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // 공동구매 탐색(타입, 키워드)
    @Query(value="select * from post where location_bcode=:bcode and title like %:keyword% and type=:type limit :n, :m", nativeQuery = true)
    List<Post> findByKeywordAndTypeWithLimit(@Param("bcode") String bcode, @Param("type") int type, @Param("keyword") String keyword,
                                        @Param("n") int n, @Param("m") int m);

    @Query(value="select * from post where title like %:keyword% and type=:type limit :n, :m", nativeQuery = true)
    List<Post> findByKeywordAndTypeWithLimit(@Param("type") int type, @Param("keyword") String keyword,
                                             @Param("n") int n, @Param("m") int m);

    // 공동구매 탐색
    @Query(value="select * from post where location_bcode=:bcode limit :n, :m", nativeQuery = true)
    List<Post> findWithLimit(@Param("bcode") String bcode, @Param("n") int n, @Param("m") int m);

    @Query(value="select * from post limit :n, :m", nativeQuery = true)
    List<Post> findWithLimit(@Param("n") int n, @Param("m") int m);
}
