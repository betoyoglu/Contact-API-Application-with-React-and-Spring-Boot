package io.getarrays.contactapi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.getarrays.contactapi.domain.Contact;


@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {
	Optional<Contact> findById(String id);

}
