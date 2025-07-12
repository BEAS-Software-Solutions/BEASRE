package com.beassolution.rule.service.impl;

import com.beassolution.rule.repository.RuleHelperRepository;
import com.beassolution.rule.service.RuleHelperService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RuleHelperServiceImpl extends RuleHelperService {

    public RuleHelperServiceImpl(RSQLParser rsqlParser, RuleHelperRepository ruleHelperRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        super(rsqlParser, ruleHelperRepository, modelMapper, mongoTemplate);
    }
}
