package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.exceptions.ResourceNotFoundException;
import com.devsuperior.demo.repositories.EventRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;

	@Transactional(readOnly = true)
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = repository.getReferenceById(id);
			DtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new EventDTO(entity);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Not found.");
		}
	}

	public void DtoToEntity(EventDTO dto, Event entity) {
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.getCity().setId(dto.getCityId());
	}

}
