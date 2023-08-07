package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.entity.SensitiveWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensitiveWordRepository extends JpaRepository<SensitiveWordEntity, Integer> {

    @Query(value = "SELECT [KEY] " +
            "FROM FREETEXTTABLE(SensitiveWord, Word, " +
            "                   :content)", nativeQuery = true)
    List<String> getListSensitiveWordsFromContent(@Param("content") String content);
}
