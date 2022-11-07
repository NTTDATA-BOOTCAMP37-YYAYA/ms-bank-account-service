package com.nttdata.bootcamp.msbankaccount.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
 
  private String creditId;
  private String customerId;
  private String productId;
  private String creditTotalPending;
  private String creditTotalPaid;

}
