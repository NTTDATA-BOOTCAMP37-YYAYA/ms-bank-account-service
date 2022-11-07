package com.nttdata.bootcamp.msbankaccount.infrastructure.persistence.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nttdata.bootcamp.msbankaccount.domain.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document (collection = "Account")
public class AccountEntity {

  @Id
  private String accountId;
  private String accountNumber;
  private String customerId;
  private String productId;
  private String customerTypeId;
  private String customerCategoryId;
  private String accountCurrencyType;
  private String accountDayMovement;
  private String accountCci;
  private String accountState;
  private String accountCreateDate;

  

  /**.*/
  public static Account toAccount(AccountEntity accountEntity) {
    Account account = new Account();
    BeanUtils.copyProperties(accountEntity, account);
    return account;
  }
  
  /**.*/
  public static AccountEntity toAccountEntity(Account account) {
    AccountEntity accountEntity = new AccountEntity();
    BeanUtils.copyProperties(account, accountEntity);
    return accountEntity;
  }
}
