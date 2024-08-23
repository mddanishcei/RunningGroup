package com.springmvc.springmvc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springmvc.springmvc.models.Club;

@Repository
public interface ClubRepository extends BaseRepository<Club,Long> {
    Optional<Club> findByTitle(String url);

    @Query("SELECT c FROM Club c WHERE c.title LIKE CONCAT('%',:query,'%')")
    List<Club> searchClub(String query);
}
