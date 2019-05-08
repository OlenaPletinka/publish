package com.myFirstProject.myFirstProject.service;

import com.myFirstProject.myFirstProject.dto.PaymentReq;
import com.myFirstProject.myFirstProject.enums.PromoType;
import com.myFirstProject.myFirstProject.exception.NotEnoughManyOnAccountException;
import com.myFirstProject.myFirstProject.exception.PromoCodeNotValidException;
import com.myFirstProject.myFirstProject.exception.UserHasNotAccountException;
import com.myFirstProject.myFirstProject.exception.UserNotFoundException;
import com.myFirstProject.myFirstProject.model.*;
import com.myFirstProject.myFirstProject.repository.AccountRepository;
import com.myFirstProject.myFirstProject.repository.PaymentRepository;
import com.myFirstProject.myFirstProject.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Value("${negative.balanceOfUser}")
    private BigDecimal negativeBalanceOfUser;

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private BasketService basketService;
    private DeletedBasketService deletedBasketService;
    private CurrencyRateService currencyRateService;
    private PaymentRepository paymentRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBasketService(BasketService basketService) {
        this.basketService = basketService;
    }

    @Autowired
    public void setDeletedBasketService(DeletedBasketService deletedBasketService) {
        this.deletedBasketService = deletedBasketService;
    }

    @Autowired
    public void setCurrencyRateService(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void updateAccount(PaymentReq paymentReq) {
        User user = getUser(paymentReq);
        Account account = accountRepository.findAccountByUserId(paymentReq.getUserId());
        if (account != null) {
            BigDecimal paymentReqSum = paymentReq.getSum();
            CurrencyEntity paymentReqCurrencyEntity = paymentReq.getCurrencyEntity();
            BigDecimal sum = currencyConverter(paymentReqSum, paymentReqCurrencyEntity);
            BigDecimal presentAmount = account.getSum();
            BigDecimal result = presentAmount.add(sum);
            account.setSum(result);
            Payment payment = getPayment(paymentReq, account, sum);
            account.getPayments().add(payment);
            //тут не виклик в репозиторія сейв бо зміни відбулися в транзакції і
            //обєкт уже існує в репозиторії
        } else {
            //викликали сейв бо акаутна у юзера не було
            account = accountRepository.save(createAccount(paymentReq, user));
            List<Payment> payments = new ArrayList<>();
            payments.add(getPayment(paymentReq, account, currencyConverter(paymentReq.getSum(), paymentReq.getCurrencyEntity())));
            account.setPayments(payments);
        }
    }

    private Payment getPayment(PaymentReq paymentReq, Account account, BigDecimal sum) {
        Payment payment = new Payment();
        payment.setSum(sum);
        payment.setAccount(account);
        payment.setCurrencyEntity(paymentReq.getCurrencyEntity());
        payment.setRate(findRate(paymentReq.getCurrencyEntity()));
        payment.setTimeOfPayment(LocalDateTime.now());
        payment.setPaymentSystemEntity(paymentReq.getPaymentSystemEntity());
        paymentRepository.save(payment);
        return payment;
    }

    private BigDecimal findRate(CurrencyEntity currencyEntity) {
        List<CurrencyRate> currencyRates = getCurrencyRates();
        BigDecimal result = BigDecimal.ZERO;
        for (CurrencyRate currencyRate : currencyRates) {
            if (currencyRate.getCompositeId().getOriginal().equals(currencyEntity)) {
                result = BigDecimal.ONE;
            } else if (currencyRate.getCompositeId().getDestination().equals(currencyEntity)) {
                result = currencyRate.getRate();
            }
        }
        return result;
    }

    @Override
    @Transactional
    public BigDecimal currencyConverter(BigDecimal sum, CurrencyEntity currencyEntity) {
        List<CurrencyRate> currencyRates = getCurrencyRates();
        CurrencyEntity original = currencyRates.get(0).getCompositeId().getOriginal();

        if (currencyEntity.equals(original)) {
            logger.info(String.format("User put money to his account in %s, value - %s", currencyEntity, sum));
            return sum;
        } else {
            return convertSum(currencyRates, currencyEntity, sum);

        }

    }

    @Override
    public BigDecimal calculateTaxPerMonth(List<Payment> paymentPerMonth) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Payment payment : paymentPerMonth) {
            sum = sum.add(currencyConverter(payment.getSum(), payment.getCurrencyEntity()));
        }
        BigDecimal percent = BigDecimal.TEN;
        logger.info(String.format("Revenue of company per month is - %s", sum));
        BigDecimal tax = sum.divide(percent);
        logger.info(String.format("%s percent tax equals %s dollars", percent, tax));

        return tax;
    }

    private List<CurrencyRate> getCurrencyRates() {
        return currencyRateService.getRateFromRepository();
    }

    private BigDecimal convertSum(List<CurrencyRate> currencyRates, CurrencyEntity currencyEntity, BigDecimal sum) {
        BigDecimal result = BigDecimal.ZERO;
        logger.info("Convert currency from paymentReq.");
        for (CurrencyRate currencyRate : currencyRates) {
            if (currencyRate.getCompositeId().getDestination().equals(currencyEntity)) {
                logger.info(String.format("User put money to his account in %s, value - %s", currencyEntity, sum));
                result = sum.multiply(currencyRate.getRate());
                logger.info(String.format("It is - %s $", result));

            }
        }
        return result;
    }


    @Override
    @Transactional
    public void payForArticles(BigDecimal sum, Long id, PromoCode promoCode) {
        Account account = accountRepository.findAccountByUserId(id);
        checkAccount(account, sum);
        BigDecimal taxPlusDiscount = findSumOfOrder(promoCode, sum);
        BigDecimal subtract = account.getSum().subtract(taxPlusDiscount);
        account.setSum(subtract);
        logger.info(String.format("User with id - %d payed - %s, his account - %s", id, sum, subtract));
    }

    private BigDecimal findSumOfOrder(PromoCode promoCode, BigDecimal sum) {
        if (promoCode.getPromoType() == PromoType.PERCENT) {
            BigDecimal percent = promoCode.getValue().divide(BigDecimal.valueOf(100));
            BigDecimal fraction = BigDecimal.ONE.subtract(percent);
            return sum.multiply(fraction);
        } else {
            checkPromoCode(sum, promoCode);
            return sum.subtract(promoCode.getValue());
        }
    }

    private void checkPromoCode(BigDecimal sum, PromoCode promoCode) {
        if (promoCode.getValue().compareTo(sum) >= 0) {
            throw new PromoCodeNotValidException(String.format("PromoCode with id - %s is bigger then sum from basket.", promoCode.getId()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAmountOfSpentMoney(Long userId) {
        if (userRepository.existsById(userId)) {
            logger.info(String.format("User with id - %d totally spent %s", userId, basketService.getAmountOfSpentMoneyFromBaskets(userId).add(deletedBasketService.getAmountOfSpentMoneyFromDeletedBaskets(userId))));
            return basketService.getAmountOfSpentMoneyFromBaskets(userId).add(deletedBasketService.getAmountOfSpentMoneyFromDeletedBaskets(userId));
        } else {
            throw new UserNotFoundException(String.format("User with id - %d do not find", userId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal totalRevenue = basketService.revenueFromBaskets().add(deletedBasketService.revenueFromDeletedBaskets());
        logger.info(String.format("Total revenue of company is - %s", totalRevenue));

        return totalRevenue;
    }

    private void checkAccount(Account account, BigDecimal sum) {
        if (account == null) {
            throw new UserHasNotAccountException("User have not account");
        } else if (account.getSum().compareTo(sum.subtract(negativeBalanceOfUser)) <= 0) {
            throw new NotEnoughManyOnAccountException(String.format("%s it is does not enough to pay", account.getSum()));
        }
    }

    private Account createAccount(PaymentReq paymentReq, User user) {
        Account account = new Account();
        account.setSum(currencyConverter(paymentReq.getSum(), paymentReq.getCurrencyEntity()));
        account.setUser(user);
        return account;
    }

    private User getUser(PaymentReq paymentReq) {
        return userRepository.findById(paymentReq.getUserId())
                .orElseThrow(()
                        -> new UserNotFoundException(String.format("User with id %d not found", paymentReq.getUserId()))
                );
    }
}
