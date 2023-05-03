package com.coderscampus.assignment13.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Address;

import com.coderscampus.assignment13.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepo;
	
	public Address save (Address address) {
		return addressRepo.save(address);
	}
	
	public Optional<Address> findById (Long id) {
		return addressRepo.findById(id);
	}
	
	public boolean existsById(Long id) {
		return addressRepo.existsById(id);
	}
	
	public List<Address> findAll() {
		return addressRepo.findAll();
	}
	
}