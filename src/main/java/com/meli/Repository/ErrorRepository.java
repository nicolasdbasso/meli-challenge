package com.meli.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.meli.Models.Error;

@Repository
public interface ErrorRepository extends CrudRepository<Error, Integer>{

}
