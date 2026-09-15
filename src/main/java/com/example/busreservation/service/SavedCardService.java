package com.example.busreservation.service;

import com.example.busreservation.model.SavedCard;
import com.example.busreservation.repository.SavedCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedCardService {

    @Autowired
    private SavedCardRepository savedCardRepository;

    public List<SavedCard> getSavedCardsByUserId(Long userId) {
        return savedCardRepository.findByUserId(userId);
    }

    public Optional<SavedCard> getSavedCardById(Long id) {
        return savedCardRepository.findById(id);
    }

    public Optional<SavedCard> getSavedCardByIdAndUserId(Long id, Long userId) {
        return savedCardRepository.findByIdAndUserId(id, userId);
    }

    public SavedCard saveCard(Long userId, String cardHolderName, String cardNumber, String cardType, String expiryDate) {
        SavedCard card = new SavedCard(userId, cardHolderName, cardNumber, cardType, expiryDate);
        return savedCardRepository.save(card);
    }

    public SavedCard updateCard(Long id, Long userId, String cardHolderName, String expiryDate, String cardType) {
        SavedCard card = savedCardRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Saved card not found with id: " + id));
        if (cardHolderName != null && !cardHolderName.trim().isEmpty()) {
            card.setCardHolderName(cardHolderName.trim());
        }
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            card.setExpiryDate(expiryDate.trim());
        }
        if (cardType != null && !cardType.trim().isEmpty()) {
            card.setCardType(cardType.trim());
        }
        return savedCardRepository.save(card);
    }

    public void deleteCard(Long id, Long userId) {
        SavedCard card = savedCardRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Saved card not found with id: " + id));
        savedCardRepository.delete(card);
    }

    public void deleteCardById(Long id) {
        savedCardRepository.deleteById(id);
    }
}
