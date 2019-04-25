package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.enums.Currency;
import com.myFirstProject.myFirstProject.enums.PaymentSystem;
import com.myFirstProject.myFirstProject.exception.NotEnoughManyOnAccountException;
import com.myFirstProject.myFirstProject.exception.UserHaveNoAccountException;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.*;
import com.myFirstProject.myFirstProject.repository.AccountRepository;
import com.myFirstProject.myFirstProject.repository.PaymentRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    //    каптор для того щоб перехопити обєкт який буде виконуватися в методі, в мене використ тому що там
//    встановл локал дейт нав
    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;

    @Test
    public void updateAccountIfItNotNull() {
//        GIVEN
        MockitoAnnotations.initMocks(this);
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
        Mockito.verify(paymentRepository).save(paymentArgumentCaptor.capture());
        Payment payment = paymentArgumentCaptor.getValue();
        Assert.assertNotNull(payment.getTimeOfPayment());
        Assert.assertEquals(BigDecimal.TEN, payment.getSum());
        Assert.assertEquals(account, payment.getAccount());
        Assert.assertEquals(buildCurrencyEntity(), payment.getCurrencyEntity());
        Assert.assertEquals(BigDecimal.ONE, payment.getRate());
        Assert.assertNotNull(account);
        Assert.assertEquals(BigDecimal.valueOf(11), account.getSum());
        Assert.assertEquals(2, account.getPayments().size());
    }


    private Account buildAccount() {
        Account account = new Account();
        account.setUser(buildUser());
        account.setSum(BigDecimal.ONE);
        List<Payment> payments = new ArrayList<>();
        Payment payment = new Payment();
        payments.add(payment);
        account.setPayments(payments);
        return account;
    }

    private Account buildNewAccount() {
        Account account = new Account();
        account.setSum(BigDecimal.TEN);
        account.setUser(buildUser());
        return account;
    }

    private List<Payment> buildPayments() {
        List<Payment> payments = new ArrayList<>();
        Payment payment = new Payment();
        payment.setSum(BigDecimal.TEN);
        payment.setCurrencyEntity(buildCurrencyEntity());
        payment.setRate(BigDecimal.ONE);
        PaymentSystemEntity paymentSystemEntity = new PaymentSystemEntity();
        paymentSystemEntity.setPaymentSystem(PaymentSystem.PAYPAL);
        payment.setPaymentSystemEntity(paymentSystemEntity);
        payments.add(payment);
        return payments;
    }


    public PaymentReq buildPaymentReq() {
        PaymentReq paymentReq = new PaymentReq();
        paymentReq.setUserId(1L);
        paymentReq.setSum(BigDecimal.TEN);
        CurrencyEntity currencyEntity = buildCurrencyEntity();
        paymentReq.setCurrencyEntity(currencyEntity);

        return paymentReq;
    }

    public CurrencyEntity buildCurrencyEntity() {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCurrency(Currency.USD);
        return currencyEntity;
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
        Mockito.when(accountRepository.save(buildNewAccount())).thenReturn(buildNewAccount());
//        WHEN
        accountService.updateAccount(paymentReq);
//        THEN
        //не знаю чи треба перевіряти метод кріейт акаунт чи підійде підставити any()
        Mockito.verify(paymentRepository).save(paymentArgumentCaptor.capture());
        Payment payment = paymentArgumentCaptor.getValue();
        Assert.assertNotNull(payment);
        Assert.assertNotNull(payment.getTimeOfPayment());
        Assert.assertEquals(BigDecimal.TEN, payment.getSum());
        Assert.assertEquals(buildCurrencyEntity(), payment.getCurrencyEntity());
        Assert.assertEquals(BigDecimal.ONE, payment.getRate());
        Assert.assertNotNull(payment.getAccount());

        Mockito.verify(accountRepository).save(eq(buildNewAccount()));

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
        //Given
        BigDecimal sum = BigDecimal.TEN;
        CurrencyEntity givenCurrency = new CurrencyEntity();
        givenCurrency.setCurrency(Currency.EUR);
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setCurrencyRateService(currencyRateService);
        Mockito.when(currencyRateService.getRateFromRepository()).thenReturn(buildCurrencyRates());
//        When
        BigDecimal actualResult = accountService.currencyConverter(sum, givenCurrency);
//        Then
        Assert.assertNotNull(actualResult);
        Assert.assertEquals(BigDecimal.valueOf(5.0), actualResult);

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
        List<CurrencyRate> currencyRates = new ArrayList<>();
        currencyRates.add(currencyRate);

        return currencyRates;
    }

    private List<Payment> buildPaymentList() {
        List<Payment> paymentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Payment payment = new Payment();
            payment.setSum(BigDecimal.valueOf(1L + i));
            CurrencyEntity currencyEntity = new CurrencyEntity();
            if (i % 2 == 0) {
                currencyEntity.setCurrency(Currency.USD);
            } else {
                currencyEntity.setCurrency(Currency.EUR);
            }

            payment.setCurrencyEntity(currencyEntity);
            paymentList.add(payment);
        }
        return paymentList;
    }

    @Test
    public void payForArticles() {
        //Given
        BigDecimal tax = BigDecimal.valueOf(0.1);
        Long id = 1L;
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setAccountRepository(accountRepository);
        Account account = buildAccount();
        Mockito.when(accountRepository.findAccountByUserId(id)).thenReturn(account);
//        у тестах конфігурабельні поля не доступні(з  @Value("${negative.balanceOfUser}"), тому їх сетимо
//        за допомогою рефлекшен ютілс
        ReflectionTestUtils.setField(accountService, "negativeBalanceOfUser", BigDecimal.valueOf(5));
//        When
        accountService.payForArticles(tax, id);
//        Then
        Assert.assertEquals(BigDecimal.valueOf(0.9), account.getSum());
    }

    @Test(expected = UserHaveNoAccountException.class)
    public void payForArticlesWhenThereIsNoAccount() {
//        Given
        BigDecimal tax = BigDecimal.ONE;
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setAccountRepository(accountRepository);
        Mockito.when(accountRepository.findAccountByUserId(1L)).thenReturn(null);
//        When
        accountService.payForArticles(tax, 1L);
    }


    @Test(expected = NotEnoughManyOnAccountException.class)
    public void payForArticlesWhenNotEnoughMoney() {
//        Given
        BigDecimal tax = BigDecimal.TEN;
        Long id = 1L;
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setAccountRepository(accountRepository);
        Account account = buildAccount();
        Mockito.when(accountRepository.findAccountByUserId(id)).thenReturn(account);
        ReflectionTestUtils.setField(accountService, "negativeBalanceOfUser", BigDecimal.valueOf(5));
//        When
        accountService.payForArticles(tax, id);

    }

    @Test
    public void getAmountOfSpentMoney() {
//        Given
        Long id = 1L;
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setUserRepository(userRepository);
        Mockito.when(userRepository.existsById(id)).thenReturn(true);
        accountService.setBasketService(basketService);
        Mockito.when(basketService.getAmountOfSpentMoneyFromBaskets(id)).thenReturn(BigDecimal.TEN);
        accountService.setDeletedBasketService(deletedBasketService);
        Mockito.when(deletedBasketService.getAmountOfSpentMoneyFromDeletedBaskets(id)).thenReturn(BigDecimal.ONE);
//        When
        BigDecimal actualResult = accountService.getAmountOfSpentMoney(id);
//        Then
        Assert.assertEquals(BigDecimal.valueOf(11), actualResult);
    }

    @Test(expected = UserNotFoundException.class)
    public void getAmountOfSpentMoneyWhenUserNotFound() {
        //        Given
        Long id = 1L;
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setUserRepository(userRepository);
        Mockito.when(userRepository.existsById(id)).thenReturn(false);
//        When
        BigDecimal actualResult = accountService.getAmountOfSpentMoney(id);
    }

    @Test
    public void getTotalRevenue() {
//        Given
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setBasketService(basketService);
        Mockito.when(basketService.revenueFromBaskets()).thenReturn(BigDecimal.TEN);
        accountService.setDeletedBasketService(deletedBasketService);
        Mockito.when(deletedBasketService.revenueFromDeletedBaskets()).thenReturn(BigDecimal.TEN);
//        When
        BigDecimal actualResult = accountService.getTotalRevenue();
//        Then
        Assert.assertEquals(BigDecimal.valueOf(20), actualResult);
    }

}