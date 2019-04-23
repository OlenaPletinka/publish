package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.*;
import com.myFirstProject.myFirstProject.repository.AccountRepository;
import com.myFirstProject.myFirstProject.repository.PaymentRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BasketService basketService;
    @Mock
    private DeletedBasketService deletedBasketService;
    @Mock
    private CurrencyRateService currencyRateService;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void updateAccountIfItNotNull() {
//        GIVEN
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setUserRepository(userRepository);
        PaymentReq paymentReq = buildPaymentReq();
        User user = buildUser();
        Mockito.when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        accountService.setAccountRepository(accountRepository);
        Account account = buildAccount();
        Mockito.when(accountRepository.findAccountByUserId(user.getId())).thenReturn(account);
        accountService.setCurrencyRateService(currencyRateService);
        Mockito.when(currencyRateService.getRateFromRepository()).thenReturn(buildCurrencyRates());
        accountService.setPaymentRepository(paymentRepository);
//        WHEN
        accountService.updateAccount(paymentReq);
//        THEN
        Mockito.verify(paymentRepository).save(any());
        Assert.assertNotNull(account);
        Assert.assertEquals(BigDecimal.valueOf(11), account.getSum());
        Assert.assertEquals(2, account.getPayments().size());
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setUser(buildUser());
        account.setSum(BigDecimal.ONE);
        List<Payment>payments = new ArrayList<>();
        Payment payment = new Payment();
        payments.add(payment);
        account.setPayments(payments);
        return account;
    }

    public PaymentReq buildPaymentReq() {
        PaymentReq paymentReq = new PaymentReq();
        paymentReq.setUserId(1L);
        paymentReq.setSum(BigDecimal.TEN);
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCurrency(Currency.USD);
        paymentReq.setCurrencyEntity(currencyEntity);
        return paymentReq;
    }

    public User buildUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    @Test
    public void updateAccountIfItNull() {
//        GIVEN
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setUserRepository(userRepository);
        PaymentReq paymentReq = buildPaymentReq();
        User user = buildUser();
        Mockito.when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        accountService.setAccountRepository(accountRepository);
        Mockito.when(accountRepository.findAccountByUserId(user.getId())).thenReturn(null);
         accountService.setCurrencyRateService(currencyRateService);
         Mockito.when(currencyRateService.getRateFromRepository()).thenReturn(buildCurrencyRates());
         accountService.setPaymentRepository(paymentRepository);
//        WHEN
        accountService.updateAccount(paymentReq);
//        THEN
        //не знаю чи треба перевіряти метод кріейт акаунт чи підійде підставити any()
        Mockito.verify(paymentRepository).save(any());
       Mockito.verify(accountRepository).save(any());

    }

    @Test(expected = UserNotFoundException.class)
    public void updateAccountWithException() {
//        GIVEN
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setUserRepository(userRepository);
        Mockito.when(userRepository.findById(1l)).thenReturn(Optional.empty());
//        WHEN
        accountService.updateAccount(buildPaymentReq());
    }

    @Test
    public void currencyConverter() {
    }

    @Test
    public void calculateTaxPerMonth() {
        //Given
        List<Payment> payments = buildPaymentList();
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setCurrencyRateService(currencyRateService);
        Mockito.when(currencyRateService.getRateFromRepository()).thenReturn(buildCurrencyRates());
        //WHEN
        BigDecimal actualResult = accountService.calculateTaxPerMonth(payments);
// Then
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(BigDecimal.valueOf(0.5), actualResult);
    }

    private List<CurrencyRate> buildCurrencyRates() {
        CurrencyRate currencyRate = new CurrencyRate();
        CurrencyEntity original = new CurrencyEntity();
        original.setCurrency(Currency.USD);
        CurrencyEntity destination = new CurrencyEntity();
        destination.setCurrency(Currency.EUR);
        currencyRate.getCompositeId().setOriginal(original);
        currencyRate.getCompositeId().setDestination(destination);
        currencyRate.setRate(BigDecimal.valueOf(0.5));
        List<CurrencyRate>currencyRates = new ArrayList<>();
        currencyRates.add(currencyRate);

        return currencyRates;
    }

    private List<Payment> buildPaymentList() {
        List<Payment>paymentList = new ArrayList<>();
        for (int i = 0; i <3 ; i++) {
            Payment payment = new Payment();
            payment.setSum(BigDecimal.valueOf(1L+i));
            CurrencyEntity currencyEntity = new CurrencyEntity();
            if (i%2==0) {
                currencyEntity.setCurrency(Currency.USD);
            }else {
                currencyEntity.setCurrency(Currency.EUR);
            }

            payment.setCurrencyEntity(currencyEntity);
            paymentList.add(payment);
        }
        return paymentList;
    }

    @Test
    public void payForArticles() {
    }

    @Test
    public void getAmountOfSpentMoney() {
    }

    @Test
    public void getTotalRevenue() {
    }
}