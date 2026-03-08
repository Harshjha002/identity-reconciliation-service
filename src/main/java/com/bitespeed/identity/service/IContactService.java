package com.bitespeed.identity.service;

import com.bitespeed.identity.dto.IdentifyRequestDTO;
import com.bitespeed.identity.dto.IdentifyResponseDTO;

public interface IContactService {
    IdentifyResponseDTO identifyContact(IdentifyRequestDTO request);
}
