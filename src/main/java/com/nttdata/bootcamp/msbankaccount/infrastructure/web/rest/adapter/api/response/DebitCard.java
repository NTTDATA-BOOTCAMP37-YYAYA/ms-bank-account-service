package com.nttdata.bootcamp.msbankaccount.infrastructure.web.rest.adapter.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {
	
	private String debidCardId;
	private String debitCardNumber;
	private String debitCardState;
	private String debitCardExpirationDate;
	private String debitCardVerificationCode;
	private String accountIdMainAssociated;
	private String debitCardCreateDate;
}
