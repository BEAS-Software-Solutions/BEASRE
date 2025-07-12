package com.beassolution.rule.service.impl;

import com.beassolution.rule.repository.FunctionLibraryRepository;
import com.beassolution.rule.service.FunctionLibraryService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class FunctionLibraryServiceImpl extends FunctionLibraryService {

    public FunctionLibraryServiceImpl(RSQLParser rsqlParser, FunctionLibraryRepository repository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        super(rsqlParser, repository, modelMapper, mongoTemplate);
    }
}
