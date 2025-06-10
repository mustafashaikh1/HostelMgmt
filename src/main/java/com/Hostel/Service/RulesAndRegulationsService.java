package com.Hostel.Service;


import com.Hostel.Entity.RulesAndRegulations;

import java.util.List;

public interface RulesAndRegulationsService {
    RulesAndRegulations addRule(RulesAndRegulations rule);
    List<RulesAndRegulations> getAllRules();
    RulesAndRegulations getRuleById(Long ruleId);
    RulesAndRegulations updateRule(Long ruleId, RulesAndRegulations rule);
    void deleteRule(Long ruleId);
}

