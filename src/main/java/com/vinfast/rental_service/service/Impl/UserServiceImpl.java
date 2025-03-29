package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.RentalHistoryResponse;
import com.vinfast.rental_service.dtos.response.UserResponse;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.enums.UserStatus;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.RentalHistoryMapper;
import com.vinfast.rental_service.mapper.UserMapper;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.model.User;
import com.vinfast.rental_service.repository.RentalOrderRepository;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.repository.specification.UserSpecificationBuilder;
import com.vinfast.rental_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vinfast.rental_service.utils.AppConst.SEARCH_SPEC_OPERATOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RentalOrderRepository rentalOrderRepository;

    private final UserMapper userMapper;

    private final RentalHistoryMapper rentalHistoryMapper;


    private User getUserById(long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public PageResponse<?> getAll(Pageable pageable, String[] users) {
        Page<User> listUser;
        if(users != null){
            UserSpecificationBuilder builder = new UserSpecificationBuilder();
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String u : users){
                Matcher matcher = pattern.matcher(u);
                if(matcher.find()){
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                }
            }
            listUser = userRepository.findAll(Objects.requireNonNull(builder.build()), pageable);
        }else {
            listUser = userRepository.findAll(pageable);
        }
        List<UserResponse> list = listUser.stream().map(userMapper::toDTO).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(listUser.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public UserResponse getUser(long userId) {
        return userMapper.toDTO(getUserById(userId));
    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);

        log.info("Change status successfully!");
    }

    @Override
    public PageResponse<?> rentalHistory(long userId, Pageable pageable) {
        Page<RentalOrder> rentalOrderList = rentalOrderRepository.findAllByUserId(userId, pageable);

        List<RentalHistoryResponse> list = rentalOrderList.stream().map(rentalHistoryMapper::toResponse).toList();
        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(rentalOrderList.getTotalPages())
                .items(list)
                .build();
    }

}
