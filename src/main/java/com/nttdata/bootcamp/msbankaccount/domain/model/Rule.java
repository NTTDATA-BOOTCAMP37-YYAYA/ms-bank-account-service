package com.nttdata.bootcamp.msbankaccount.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

  private String actionId;
  private String ruleId;
  private String ruleName;
  private String ruleValue;
  private String ruleState;
  
}
