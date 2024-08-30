package org.example.smartgarage.services;

import org.example.smartgarage.dtos.VisitOutDto;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.repositories.VisitRepository;
import org.example.smartgarage.services.contracts.CurrencyService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.example.smartgarage.utils.filtering.VisitSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;
    private final CurrencyService currencyService;

    public VisitServiceImpl(VisitRepository visitRepository, CurrencyService currencyService) {
        this.visitRepository = visitRepository;
        this.currencyService = currencyService;
    }

    @Override
    public List<Visit> findAll(VisitFilterOptions visitFilterOptions) {
        VisitSpecification visitSpecification = new VisitSpecification(visitFilterOptions);

        Sort.Direction direction;
        if (visitFilterOptions.getSortOrder().isPresent()) {
            direction = Sort.Direction.fromString(visitFilterOptions.getSortOrder().get());
        } else {
            direction = Sort.Direction.ASC;
        }

        String sortBy;
        if (visitFilterOptions.getSortBy().isPresent()) {
            sortBy = visitFilterOptions.getSortBy().get();
        } else {
            sortBy = "scheduleDate";
        }

        return visitRepository.findAll(visitSpecification, Sort.by(direction, sortBy));
    }

    @Override
    public Visit findById(long visitId) {
        return visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Visit", visitId));
    }

    @Override
    public List<VisitOutDto> calculateCost(List<VisitOutDto> visitOutDtos, String exchangeCurrency) throws IOException {
        if (exchangeCurrency == null) return visitOutDtos;

        double exchangeRate = currencyService.getConversionRate(exchangeCurrency);

        return visitOutDtos.stream().peek(dto -> {
            dto.setExchangeRate(exchangeRate);
            dto.setTotalCost(dto.getTotalCost().multiply(BigDecimal.valueOf(exchangeRate))
                    .setScale(2, RoundingMode.HALF_UP));
            dto.setCurrency(exchangeCurrency.toUpperCase());
        }).toList();
    }
}
