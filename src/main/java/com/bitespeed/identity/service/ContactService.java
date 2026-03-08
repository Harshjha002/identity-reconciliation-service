package com.bitespeed.identity.service;

import com.bitespeed.identity.dto.ContactResponseDTO;
import com.bitespeed.identity.dto.IdentifyRequestDTO;
import com.bitespeed.identity.dto.IdentifyResponseDTO;
import com.bitespeed.identity.entity.Contact;
import com.bitespeed.identity.enums.LinkPrecedence;
import com.bitespeed.identity.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService implements IContactService {

    private  final ContactRepository contactRepository;

    @Override
    public IdentifyResponseDTO identifyContact(IdentifyRequestDTO request) {
        String email = request.getEmail();
        String phone = request.getPhoneNumber();

        //Find the Contacts
        List<Contact> contacts  = contactRepository.findByEmailOrPhoneNumber(email , phone);

        //No Contact Exists
        if(contacts.isEmpty()){
            Contact newContact = Contact.builder()
                    .email(email)
                    .phoneNumber(phone)
                    .linkPrecedence(LinkPrecedence.PRIMARY)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Contact savedContact = contactRepository.save(newContact);

            ContactResponseDTO contactResponse = ContactResponseDTO.builder()
                    .primaryContactId(savedContact.getId())
                    .emails(List.of(savedContact.getEmail()))
                    .phoneNumbers(List.of(savedContact.getPhoneNumber()))
                    .secondaryContactIds(Collections.emptyList())
                    .build();

            return IdentifyResponseDTO.builder()
                    .contact(contactResponse)
                    .build();

        }

        return IdentifyResponseDTO.builder().build();
    }
}
