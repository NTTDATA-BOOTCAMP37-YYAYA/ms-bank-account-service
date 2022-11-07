package com.nttdata.bootcamp.msbankaccount.application.outgoing;

import com.nttdata.bootcamp.msbankaccount.domain.model.AccountBalance;

public interface SendAccountBalancePort {


	void sendAccountBalance(AccountBalance accountBalance);
}
