package org.example.smartgarage.controllers.mvc;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.QuotationDto;
import org.example.smartgarage.events.EmailQuotationEvent;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.QuotationService;
import org.example.smartgarage.services.contracts.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
@RequestMapping("/garage")
public class QuotationMvcController {

    private final OrderTypeService orderTypeService;
    private final UserService userService;
    private final QuotationService quotationService;
    private final ApplicationEventPublisher eventPublisher;

    public QuotationMvcController(OrderTypeService orderTypeService, UserService userService, QuotationService quotationService, ApplicationEventPublisher eventPublisher) {
        this.orderTypeService = orderTypeService;
        this.userService = userService;
        this.quotationService = quotationService;
        this.eventPublisher = eventPublisher;
    }

    @ModelAttribute("availableServices")
    public List<ServiceType> getAvailableServices() {
        return orderTypeService.getAll();
    }

    @ModelAttribute("availableCurrencies")
    public List<CurrencyCode> getCurrencies() {
        return List.of(CurrencyCode.values());
    }

    @PreAuthorize("hasRole('CUSTOMER') or isAnonymous()")
    @GetMapping("/quotation")
    public ModelAndView getQuotePage(@ModelAttribute("quotationDto") QuotationDto dto,
                                     @AuthenticationPrincipal CustomUserDetails principal) {

        if (principal != null) {
            UserEntity user = userService.getById(principal.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
        }


        return new ModelAndView("quote");
    }

    @PostMapping("/quotation")
    public String sendQuotation(@Valid @ModelAttribute("quotationDto") QuotationDto dto,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "quote";
        }

        UserEntity user = userService.findByEmail(dto.getEmail());
        if (user == null) {
            user = new UserEntity(dto.getFirstName(), dto.getLastName(), dto.getEmail(),
                    null, null, null);
        }

        boolean supportedVehicle = quotationService
                .confirmVehicle(dto.getCarBrand(), dto.getCarModel(), dto.getCarYear());

        ByteArrayOutputStream pdf;
        if (supportedVehicle) {
            pdf = quotationService.prepareQuotation(user,
                    dto.getDesiredDate(), dto.getCarBrand(), dto.getCarModel(), dto.getCarYear(),
                    dto.getRequiredServices(), dto.getDesiredCurrency());
        } else {
            pdf = quotationService.prepareQuotation(user, dto.getDesiredDate(),
                    dto.getRequiredServices(), dto.getDesiredCurrency());
        }

        eventPublisher.publishEvent(new EmailQuotationEvent(pdf,user));

        return "redirect:/garage/quotation?sent=true";
    }
}
