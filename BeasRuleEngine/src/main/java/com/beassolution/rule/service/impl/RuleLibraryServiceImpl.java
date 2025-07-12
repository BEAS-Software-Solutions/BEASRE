package com.beassolution.rule.service.impl;

import com.beassolution.rule.repository.RuleLibraryRepository;
import com.beassolution.rule.service.RuleLibraryService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RuleLibraryServiceImpl extends RuleLibraryService {

    public RuleLibraryServiceImpl(RSQLParser rsqlParser, RuleLibraryRepository repository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        super(rsqlParser, repository, modelMapper, mongoTemplate);
    }
}
