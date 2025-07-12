package com.beassolution.rule.service;

import com.beassolution.rule.model.RuleLibrary;
import com.beassolution.rule.repository.RuleLibraryRepository;
import com.beassolution.rule.service.base.BaseService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class RuleLibraryService extends BaseService<RuleLibrary> {

    public RuleLibraryService(RSQLParser rsqlParser,
                              RuleLibraryRepository repository,
                              ModelMapper modelMapper,
                              MongoTemplate mongoTemplate) {
        super(rsqlParser, repository, modelMapper, mongoTemplate, RuleLibrary.class);
    }
}
