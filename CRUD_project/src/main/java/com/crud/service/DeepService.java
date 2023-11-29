package com.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.entity.Deep;
import com.crud.repository.DeepRepository;

@Service
public class DeepService {

	@Autowired
    private DeepRepository deepRepository;

    public void imgSave(Deep deep) {
    	deepRepository.save(deep);
    }
}
