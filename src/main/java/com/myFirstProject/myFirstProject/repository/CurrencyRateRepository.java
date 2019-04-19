package com.myFirstProject.myFirstProject.repository;

import com.myFirstProject.myFirstProject.model.CurrencyRate;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, CurrencyRate.CompositeId> {
}
