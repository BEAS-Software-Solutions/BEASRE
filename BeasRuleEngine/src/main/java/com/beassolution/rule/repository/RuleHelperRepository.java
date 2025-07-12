package com.beassolution.rule.repository;

import com.beassolution.rule.model.RuleHelper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface RuleHelperRepository extends MongoRepository<RuleHelper, UUID> {
    List<RuleHelper> findByContainerName(String containerName);
}
