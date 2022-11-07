package com.nttdata.bootcamp.msbankaccount.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalance {
  
	private String accountBalanceId;
	private String accountId;
	private double accountBalanceAmount;
	private long accountBalanceQuantityTransaction;
	private String accountBalanceState;  
	private String accountBalanceCreateDate;
}
