package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.RentalOrderCreateRequest;
import com.vinfast.rental_service.dtos.request.SpecialRequest;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderResponse;
import com.vinfast.rental_service.enums.DiscountType;
import com.vinfast.rental_service.enums.PaymentMethod;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.RentalOrderMapper;
import com.vinfast.rental_service.model.*;
import com.vinfast.rental_service.repository.*;
import com.vinfast.rental_service.repository.specification.RentalOrderSpecificationBuilder;
import com.vinfast.rental_service.service.RentalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vinfast.rental_service.utils.AppConst.SEARCH_SPEC_OPERATOR;
import static com.vinfast.rental_service.utils.OrderCodeUtil.generateOrderCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalOrderServiceImpl implements RentalOrderService {
    private final RentalOrderRepository rentalOrderRepository;

    private final RentalOrderMapper rentalOrderMapper;

    private final SearchRepository searchRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final PaymentRepository paymentRepository;

    private final PickupLocationRepository pickupLocationRepository;

    private final InsuranceOptionRepository insuranceOptionRepository;

    private final PromotionRepository promotionRepository;

    private final OrderInsuranceOptionRepository orderInsuranceOptionRepository;

    private final PromotionApplicableModelRepository promotionApplicableModelRepository;

    @Override
    public PageResponse<?> getAll(Pageable pageable, String[] orders, String[] cars) {
        Page<RentalOrder> records;
        if(orders != null && cars != null){
            return searchRepository.searchRentalOrderWithJoin(pageable, orders, cars);
        } else if(orders != null){
            RentalOrderSpecificationBuilder builder = new RentalOrderSpecificationBuilder();
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String o : orders){
                Matcher matcher = pattern.matcher(o);
                if(matcher.find()){
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                }
            }
            records = rentalOrderRepository.findAll(Objects.requireNonNull(builder.build()), pageable);
        } else {
            records = rentalOrderRepository.findAll(pageable);
        }

        List<RentalOrderResponse> results = records.stream().map(rentalOrderMapper::toDTO).toList();
        return PageResponse.builder()
                .page(records.getNumber())
                .size(records.getTotalPages())
                .totalPage(records.getTotalPages())
                .items(results)
                .build();
    }

    @Override
    public void updateRentalOrderStatus(long orderId, RentalOrderStatus status) {
        RentalOrder order = rentalOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        validateStatusTransition(order.getStatus(), status);

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        // Log status change
        // Create admin log
        // Additional business logic
        rentalOrderRepository.save(order);
        log.info("Update order status successfully");
    }
    private void validateStatusTransition(RentalOrderStatus currentStatus, RentalOrderStatus newStatus) {
        if (currentStatus == newStatus) {
            throw new InvalidDataException("Order is already in " + newStatus + " status");
        }
    }

    @Override
    public void addSpecialRequest(long orderId, SpecialRequest request) {
        RentalOrder order = rentalOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setSpecialRequests(request.getSpecialRequest());

        rentalOrderRepository.save(order);

        log.info("Add special request successfully!");
    }

    @Override
    @Transactional
    public void createOrder(RentalOrderCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        if (!request.getStartDateTime().isAfter(now)) {
            throw new InvalidDataException("Start date must be in the future.");
        }

        if (!request.getStartDateTime().isBefore(request.getEndDateTime())) {
            throw new InvalidDataException("Start date must be before end date.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new InvalidDataException("User not found with userId: " + request.getUserId()));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new InvalidDataException("Car not found with carId: " + request.getCarId()));

        PickupLocation pickupLocation = pickupLocationRepository.findById(request.getPickupLocationId())
                .orElseThrow(() -> new InvalidDataException("PickupLocation not found with pickupLocationId: " + request.getPickupLocationId()));

        InsuranceOption insuranceOption = insuranceOptionRepository.findById(request.getInsuranceOptionId())
                .orElseThrow(() -> new InvalidDataException("InsuranceOption not found with insuranceOptionId: " + request.getInsuranceOptionId()));

        Promotion promotion = promotionRepository.findByCode(request.getPromotionCode())
                .orElseThrow(() -> new InvalidDataException("Promotion not found with promotionCode: " + request.getPromotionCode()));

        BigDecimal discountAmount = calculateDiscountAmount(promotion, request.getBasePrice());
        BigDecimal totalPrice = calculateTotalPrice(request.getBasePrice(), insuranceOption.getDailyRate(), discountAmount);

        RentalOrder rentalOrder = RentalOrder.builder()
                .orderCode(generateOrderCode())
                .startDatetime(request.getStartDateTime())
                .endDatetime(request.getEndDateTime())
                .rentalType(request.getRentalType())
                .basePrice(request.getBasePrice())
                .specialRequests(request.getSpecialRequest())
                .totalPrice(totalPrice)
                .build();
        rentalOrder.setUser(user);
        rentalOrder.setCar(car);
        rentalOrder.setPickupLocation(pickupLocation);
        rentalOrder.setInsuranceFee(insuranceOption.getDailyRate());
        rentalOrder.setDiscountAmount(discountAmount);

        rentalOrderRepository.save(rentalOrder);

        car.setPickupLocation(pickupLocation);
        carRepository.save(car);

        OrderInsuranceOption orderInsuranceOption = OrderInsuranceOption.builder()
                .order(rentalOrder)
                .insuranceOption(insuranceOption)
                .fee(insuranceOption.getDailyRate())
                .build();

        orderInsuranceOptionRepository.save(orderInsuranceOption);

        CarModel carModel = car.getCarModel();
        PromotionApplicableModel promotionApplicableModel = PromotionApplicableModel.builder()
                .promotion(promotion)
                .carModel(carModel)
                .build();
        promotionApplicableModelRepository.save(promotionApplicableModel);

        Payment payment = Payment.builder()
                .order(rentalOrder)
                .amount(totalPrice)
                .paymentMethod(PaymentMethod.cash)
                .build();

        paymentRepository.save(payment);

        log.info("Create rental order successfully");
    }


    private BigDecimal calculateDiscountAmount(Promotion promotion, BigDecimal basePrice){
        BigDecimal result;
        if(promotion.getDiscountType() == DiscountType.percentage){
            result = basePrice.multiply(promotion.getDiscountValue());
            if(result.compareTo(promotion.getMaxDiscountAmount()) > 0){
                result = promotion.getMaxDiscountAmount();
            }
        }else {
            result = promotion.getDiscountValue();
        }

        return result;
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, BigDecimal insuranceFee, BigDecimal discountAmount){
        return basePrice.add(insuranceFee).subtract(discountAmount);
    }
}
