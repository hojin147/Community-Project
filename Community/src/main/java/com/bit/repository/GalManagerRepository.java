package com.bit.repository;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.bit.domain.GalManager;

@Repository
public interface GalManagerRepository extends CrudRepository<GalManager, Integer> {

}
