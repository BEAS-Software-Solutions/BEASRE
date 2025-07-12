package com.beassolution.rule.service;

import com.beassolution.rule.model.FunctionLibrary;
import com.beassolution.rule.repository.FunctionLibraryRepository;
import com.beassolution.rule.service.base.BaseService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class FunctionLibraryService extends BaseService<FunctionLibrary> {

    public FunctionLibraryService(RSQLParser rsqlParser,
                                  FunctionLibraryRepository repository,
                                  ModelMapper modelMapper,
                                  MongoTemplate mongoTemplate) {
        super(rsqlParser, repository, modelMapper, mongoTemplate, FunctionLibrary.class);
    }
}
