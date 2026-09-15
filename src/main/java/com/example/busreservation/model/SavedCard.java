package com.example.busreservation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * SavedCard Model - Represents saved payment cards for passengers
 * Used by: Passengers (save, view, edit, delete payment cards for quick checkout)
 */
@Entity
@Table(name = "SavedCard")
public class SavedCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    @Column(name = "card_number", nullable = false)
    private String cardNumber; // Stored as masked e.g. "**** **** **** 1234"

    @Column(name = "card_type", nullable = false, length = 20)
    private String cardType; // Visa, Mastercard

    @Column(name = "expiry_date", nullable = false, length = 10)
    private String expiryDate; // MM/YY

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public SavedCard() {
        this.createdAt = LocalDateTime.now();
    }

    public SavedCard(Long userId, String cardHolderName, String cardNumber, String cardType, String expiryDate) {
        this.userId = userId;
        this.cardHolderName = cardHolderName;
        this.cardNumber = maskCardNumber(cardNumber);
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.createdAt = LocalDateTime.now();
    }

    public static String maskCardNumber(String rawNumber) {
        if (rawNumber == null) return "**** **** **** ****";
        if (rawNumber.startsWith("****")) return rawNumber;
        String digitsOnly = rawNumber.replaceAll("\\D", "");
        if (digitsOnly.length() < 4) return rawNumber;
        String lastFour = digitsOnly.substring(digitsOnly.length() - 4);
        return "**** **** **** " + lastFour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = maskCardNumber(cardNumber);
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
