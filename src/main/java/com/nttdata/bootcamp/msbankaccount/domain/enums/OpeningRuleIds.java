package com.nttdata.bootcamp.msbankaccount.domain.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**.*/
@AllArgsConstructor
@NoArgsConstructor
public enum OpeningRuleIds {

  MAXQUANTPRODUCT("1"),
  MINCREDCARD("2"),
  MAXCREDDEBT("3");
  
  private String value;
    
  public String getValue() {
    return value;
  }

}
