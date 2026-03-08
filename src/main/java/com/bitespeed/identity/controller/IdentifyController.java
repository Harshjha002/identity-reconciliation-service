package com.bitespeed.identity.controller;


import com.bitespeed.identity.dto.IdentifyRequestDTO;
import com.bitespeed.identity.dto.IdentifyResponseDTO;
import com.bitespeed.identity.service.IContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
@RequiredArgsConstructor
public class IdentifyController {

    private  final IContactService contactService;

    @PostMapping
    public ResponseEntity<IdentifyResponseDTO> identify(@RequestBody IdentifyRequestDTO request) {

        IdentifyResponseDTO response = contactService.identifyContact(request);

        return ResponseEntity.ok(response);
    }
}
