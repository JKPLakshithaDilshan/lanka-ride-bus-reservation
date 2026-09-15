package com.example.busreservation.controller;

import com.example.busreservation.model.SavedCard;
import com.example.busreservation.security.AuthenticatedUser;
import com.example.busreservation.service.SavedCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SavedCardController - REST API endpoints for managing passenger saved payment cards (CRUD)
 */
@RestController
@RequestMapping("/api/saved-cards")
@CrossOrigin(origins = "*")
public class SavedCardController {

    @Autowired
    private SavedCardService savedCardService;

    // Get saved cards for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavedCard>> getSavedCardsByUserId(@PathVariable Long userId) {
        try {
            List<SavedCard> cards = savedCardService.getSavedCardsByUserId(userId);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Save a new card
    @PostMapping
    public ResponseEntity<?> saveCard(@RequestBody Map<String, Object> requestData, Authentication auth) {
        try {
            Long userId = null;
            if (requestData.containsKey("userId") && requestData.get("userId") != null) {
                userId = Long.valueOf(requestData.get("userId").toString());
            } else if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser user) {
                userId = user.id();
            }

            if (userId == null) {
                return ResponseEntity.badRequest().body("User ID is required");
            }

            String cardHolderName = (String) requestData.get("cardHolderName");
            String cardNumber = (String) requestData.get("cardNumber");
            String cardType = (String) requestData.get("cardType");
            String expiryDate = (String) requestData.get("expiryDate");

            if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Cardholder name is required");
            }
            if (cardNumber == null || cardNumber.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Card number is required");
            }
            if (cardType == null || cardType.trim().isEmpty()) {
                cardType = "Visa";
            }
            if (expiryDate == null || expiryDate.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Expiry date is required");
            }

            SavedCard savedCard = savedCardService.saveCard(userId, cardHolderName.trim(), cardNumber.trim(), cardType.trim(), expiryDate.trim());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving payment card: " + e.getMessage());
        }
    }

    // Update an existing card
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        try {
            Long userId = requestData.containsKey("userId") && requestData.get("userId") != null 
                    ? Long.valueOf(requestData.get("userId").toString()) : null;

            String cardHolderName = (String) requestData.get("cardHolderName");
            String expiryDate = (String) requestData.get("expiryDate");
            String cardType = (String) requestData.get("cardType");

            SavedCard updatedCard;
            if (userId != null) {
                updatedCard = savedCardService.updateCard(id, userId, cardHolderName, expiryDate, cardType);
            } else {
                Optional<SavedCard> existingOpt = savedCardService.getSavedCardById(id);
                if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();
                SavedCard card = existingOpt.get();
                updatedCard = savedCardService.updateCard(id, card.getUserId(), cardHolderName, expiryDate, cardType);
            }

            return ResponseEntity.ok(updatedCard);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating payment card: " + e.getMessage());
        }
    }

    // Delete a saved card
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        try {
            if (userId != null) {
                savedCardService.deleteCard(id, userId);
            } else {
                savedCardService.deleteCardById(id);
            }
            return ResponseEntity.ok().body("Card deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting card: " + e.getMessage());
        }
    }
}
