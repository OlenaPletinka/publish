package com.myFirstProject.myFirstProject.repository;


import com.myFirstProject.myFirstProject.model.Source;
import org.springframework.data.repository.CrudRepository;

public interface SourceRepository extends CrudRepository <Source, Long> {
}
