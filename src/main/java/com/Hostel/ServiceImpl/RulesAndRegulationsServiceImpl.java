package com.Hostel.ServiceImpl;


import com.Hostel.Entity.RulesAndRegulations;
import com.Hostel.Repository.RulesAndRegulationsRepository;
import com.Hostel.Service.RulesAndRegulationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RulesAndRegulationsServiceImpl implements RulesAndRegulationsService {

    @Autowired
    private RulesAndRegulationsRepository rulesAndRegulationsRepository;

    @Override
    public RulesAndRegulations addRule(RulesAndRegulations rule) {
        return rulesAndRegulationsRepository.save(rule);
    }

    @Override
    public List<RulesAndRegulations> getAllRules() {
        return rulesAndRegulationsRepository.findAll();
    }

    @Override
    public RulesAndRegulations getRuleById(Long ruleId) {
        return rulesAndRegulationsRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found with ID: " + ruleId));
    }

    @Override
    public RulesAndRegulations updateRule(Long ruleId, RulesAndRegulations rule) {
        RulesAndRegulations existingRule = getRuleById(ruleId);
        existingRule.setRuleDescription(rule.getRuleDescription());
        return rulesAndRegulationsRepository.save(existingRule);
    }

    @Override
    public void deleteRule(Long ruleId) {
        RulesAndRegulations rule = getRuleById(ruleId);
        rulesAndRegulationsRepository.delete(rule);
    }
}
