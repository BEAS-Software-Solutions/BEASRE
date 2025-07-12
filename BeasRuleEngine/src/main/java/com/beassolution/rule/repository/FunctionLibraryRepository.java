package com.beassolution.rule.repository;

import com.beassolution.rule.model.FunctionLibrary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface FunctionLibraryRepository extends MongoRepository<FunctionLibrary, UUID> {
    List<FunctionLibrary> findByContainerName(String containerName);
}
