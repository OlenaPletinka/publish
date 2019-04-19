package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface AccountRepository  extends CrudRepository<Account, Long> {
    //дані блокуються в момент запису і до завершення транзакції
    //ніхто не може ні писати ні читати
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account findAccountByUserId(Long id);

}
