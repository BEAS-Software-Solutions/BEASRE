package com.beassolution.rule.repository;

import com.beassolution.rule.model.RuleLibrary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface RuleLibraryRepository extends MongoRepository<RuleLibrary, UUID> {
    List<RuleLibrary> findByContainerName(String containerName);
}
