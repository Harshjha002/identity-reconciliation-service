package com.bitespeed.identity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContactResponseDTO {

    private Long primaryContactId;

    private List<String> emails;

    private List<String> phoneNumbers;

    private List<Long> secondaryContactIds;
}