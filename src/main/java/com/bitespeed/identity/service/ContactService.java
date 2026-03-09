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

        //contact exists
        Contact primaryContact = contacts
                .stream()
                .filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .findFirst()
                .orElseGet(() -> {
                    Long primaryId = contacts.getFirst().getLinkedId();
                    return contactRepository.findById(primaryId).orElseThrow();
                });

        List<Contact> identityContacts =contactRepository.findByLinkedIdOrId(primaryContact.getId(), primaryContact.getId());

        List<Contact> primaryContacts = contacts.stream().filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.PRIMARY).toList();

        if(primaryContacts.size() > 1){
            Contact oldestPrimary = primaryContacts.stream()
                    .min((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()))
                    .orElse(null);

            for (Contact contact : primaryContacts){
                if(!contact.getId().equals(oldestPrimary.getId())){
                    contact.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    contact.setLinkedId(oldestPrimary.getId());

                    contact.setUpdatedAt(LocalDateTime.now());
                    contactRepository.save(contact);
                }
            }

            primaryContact = oldestPrimary;
        }
        //Boolean for new info
        boolean emailExists = identityContacts.stream()
                .anyMatch(contact -> email != null  && email.equals(contact.getEmail()));

        boolean phoneExists = identityContacts.stream()
                .anyMatch(contact -> phone != null && phone.equals(contact.getPhoneNumber()));

        //If New Info  detected
        if(!phoneExists || !emailExists){
            Contact secondaryContact = Contact.builder()
                    .email(email)
                    .phoneNumber(phone)
                    .linkedId(primaryContact.getId())
                    .linkPrecedence(LinkPrecedence.SECONDARY)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Contact savedSecondary = contactRepository.save(secondaryContact);

            identityContacts.add(savedSecondary);
        }

        List<String> emails = identityContacts.stream()
                .map(Contact::getEmail)
                .distinct()
                .toList();

        List<String> phones = identityContacts.stream()
                .map(Contact::getPhoneNumber)
                .distinct()
                .toList();

        List<Long> secondaryIds = identityContacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == LinkPrecedence.SECONDARY)
                .map(Contact::getId)
                .toList();

        ContactResponseDTO contactResponse = ContactResponseDTO.builder()
                .primaryContactId(primaryContact.getId())
                .emails(emails)
                .phoneNumbers(phones)
                .secondaryContactIds(secondaryIds)
                .build();


        return IdentifyResponseDTO.builder()
                .contact(contactResponse)
                .build();
    }
}
