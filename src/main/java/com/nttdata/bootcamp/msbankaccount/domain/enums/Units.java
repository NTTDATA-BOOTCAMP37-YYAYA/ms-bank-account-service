package com.nttdata.bootcamp.msbankaccount.domain.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**.*/
@AllArgsConstructor
@NoArgsConstructor
public enum Units {

  ONE("1"),
  MANY("*"),
  ZERO("0"),
  NOTHING("-");
  
  private String value;

  public String getValue() {
    return value;
  }



}
