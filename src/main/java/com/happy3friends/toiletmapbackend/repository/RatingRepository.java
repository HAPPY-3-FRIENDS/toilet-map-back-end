package com.happy3friends.toiletmapbackend.repository;

import com.happy3friends.toiletmapbackend.dto.CustomRatingDetailsDTO;
import com.happy3friends.toiletmapbackend.entity.RatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    @Query(value = "SELECT r.Id, ui.FullName, r.Star, r.Comment, r.DateTime, ri.ImageSource " +
            "FROM Rating r " +
            "         JOIN Account a " +
            "              ON r.AccountId = a.Id " +
            "         JOIN UserInfo ui ON a.Id = ui.AccountId " +
            "         LEFT JOIN RatingImage ri ON ri.RatingId = r.Id", nativeQuery = true)
    List<CustomRatingDetailsDTO> getAllRatings(Pageable pageable);
}
