package io.getarrays.contactapi.service;


import javax.management.RuntimeException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.getarrays.contactapi.domain.Contact;
import io.getarrays.contactapi.repo.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j //otomatik olarak logger oluşturur. log nesnesini kullanarak log yazabilirsin
@Transactional(rollbackOn = Exception.class) //sınıfın veritabanı işlemi(transaction) içinde çalışmasını sağlar. bir hata olursa tüm işlemler geri alınır (rollback)
@RequiredArgsConstructor
public class ContactService {
	private final ContactRepo contactRepo;
	
	public Page<Contact> getAllContacts(int page, int size){
		return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name"))); 
	}
	
	public Contact getContact(String id) {
		return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("contact not found"));
	}
	
	public boolean createContact(Contact contact) {
		contactRepo.save(contact);
		return true;
	}
	
	public boolean deleteContact(Contact contact) {
		contactRepo.delete(contact);
		return true;
	}
	
	public String uploadPhoto(String id, MultipartFile file)

}
