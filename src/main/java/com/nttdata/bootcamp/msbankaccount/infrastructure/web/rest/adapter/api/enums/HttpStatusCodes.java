package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**.*/
@AllArgsConstructor
@NoArgsConstructor
public enum HttpStatusCodes {

  STATUS_NO_FOUND(404);
  
  private int value;

  public int getValue() {
    return value;
  }



}
