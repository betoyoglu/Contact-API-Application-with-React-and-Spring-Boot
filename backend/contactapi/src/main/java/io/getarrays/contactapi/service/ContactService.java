package io.getarrays.contactapi.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.nio.file.StandardCopyOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.getarrays.contactapi.constant.Constant;

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
	
	public String uploadPhoto(String id, MultipartFile file) {
		Contact contact = getContact(id);
		String photoUrl = photoFunction.apply(id, file);
		contact.setPhotoUrl(photoUrl);
		contactRepo.save(contact);
		
		return photoUrl;
		
	}
	
	private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name-> name.contains("."))
			.map(name -> "." + name.substring(filename.lastIndexOf(".")+ 1)).orElse(".png");
	
	//string ve multipartfile alıp string döndürecek
	private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
		
		try {
			Path fileStorageLocation = Paths.get(Constant.PHOTO_DIRECTORY).toAbsolutePath().normalize();
				if(!Files.exists(fileStorageLocation)) {
					Files.createDirectories(fileStorageLocation);
				}
			Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
			return ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/contacts/image/" + id + fileExtension.apply(image.getOriginalFilename()))
					.toUriString();
			
		}
		catch (Exception exception){
			throw new RuntimeException("Unable to save image");
		}
	};
}