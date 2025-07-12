package com.beassolution.rule.service;

import com.beassolution.rule.model.RuleHelper;
import com.beassolution.rule.repository.RuleHelperRepository;
import com.beassolution.rule.service.base.BaseService;
import cz.jirutka.rsql.parser.RSQLParser;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class RuleHelperService extends BaseService<RuleHelper> {

    public RuleHelperService(RSQLParser rsqlParser,
                             RuleHelperRepository ruleHelperRepository,
                             ModelMapper modelMapper,
                             MongoTemplate mongoTemplate) {
        super(rsqlParser, ruleHelperRepository, modelMapper, mongoTemplate, RuleHelper.class);
    }
}
