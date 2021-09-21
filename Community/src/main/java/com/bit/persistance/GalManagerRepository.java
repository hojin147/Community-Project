package com.bit.persistance;

import org.springframework.data.repository.CrudRepository;

import com.bit.domain.GalManager;

@Repository
public interface GalManagerRepository extends CrudRepository<GalManager, Integer> {

}