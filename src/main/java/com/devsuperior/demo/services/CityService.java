package com.devsuperior.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.exceptions.DatabaseException;
import com.devsuperior.demo.exceptions.ResourceNotFoundException;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;

import jakarta.websocket.server.PathParam;

@Service
public class CityService {
	
	@Autowired
	private CityRepository repository;
	
	@Autowired 
	private EventRepository eventRepository;
	
	@Transactional(readOnly=true)
	public List<CityDTO> findAll() {
		List<City> entityPage = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		return entityPage.stream().map(x -> new CityDTO(x)).toList();
	}
	
	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		repository.save(entity);
		return new CityDTO(entity);
	}
	
	@Transactional
	public void delete( Long id) {
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Not found.");
		}
		try {
		repository.deleteById(id);
		
		}catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial.");
		}
	}
	
	
	public boolean hasDependency (Long id) {
		EventDTO dto = new EventDTO();
		dto.setCityId(id);
		
		if(eventRepository.existsById(dto.getCityId())) {
			return true;
			
		}return false;
	}
}
