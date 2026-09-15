package com.example.busreservation.repository;

import com.example.busreservation.model.SavedCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedCardRepository extends JpaRepository<SavedCard, Long> {
    List<SavedCard> findByUserId(Long userId);
    Optional<SavedCard> findByIdAndUserId(Long id, Long userId);
    void deleteByUserId(Long userId);
}
