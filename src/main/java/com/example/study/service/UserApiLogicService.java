package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.User;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;

    // 1. request data 가져오기
    // 2. user 생성
    // 3. 생성된 데이터 -> UserApiResponse return
    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        //1.
        UserApiRequest userApiRequest = request.getData();

        //2.
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status("REGISTERED") // todo : 요청에 없기 때문에 직접 입력? 이해 안됨. 요청하는 데이터가 아니라서 그런듯 이해완료
                .email(userApiRequest.getEmail())
                .phoneNumber(userApiRequest.getPhoneNumber())
                .registeredAt(LocalDateTime.now())
                .build();
        User newUser = userRepository.save(user);

        //3.
        return response(newUser);

    }

    @Override
    public Header<UserApiResponse> read(Long id) {

        //id -> repository 에서 getOne, getById 통해 가져온다.
        Optional<User> optional = userRepository.findById(id);

        //user -> userApiResponse return
        return optional
                .map(user -> response(user)) //map : 형 변환 리턴
                .orElseGet(
                        () -> Header.ERROR("데이터 없음")
                );
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        //1. 데이터 가져오기
        UserApiRequest userApiRequest = request.getData();
        //2. id-> user data 찾기
        Optional<User> optional = userRepository.findById(userApiRequest.getId());

        return optional.map(user->{
            //3. update
            user.setAccount(userApiRequest.getAccount())
                    .setPassword(userApiRequest.getPassword())
                    .setStatus(userApiRequest.getStatus())
                    .setPhoneNumber(userApiRequest.getPhoneNumber())
                    .setEmail(userApiRequest.getEmail())
                    .setRegisteredAt(userApiRequest.getRegisteredAt())
                    .setUnregisteredAt(userApiRequest.getUnRegisteredAt());

            return user;
        })
                .map(user->userRepository.save(user)) // save update user into userRepository and return update user
                .map(updateUser->response(updateUser)) //4. updateUser userApiResponse return
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {

        //1. id -> repository -> user
        Optional<User> optional = userRepository.findById(id);
        //2. repository -> delete
        return optional.map(user->{
            userRepository.delete(user);
            //3. response return
            return Header.OK();
        })
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    // 3번에 해당하는 메소드
    private Header<UserApiResponse> response(User user){

        UserApiResponse userApiResponse = UserApiResponse.builder()
                .account(user.getAccount())
                .id(user.getId())
                .password(user.getPassword()) //todo : 암호화 or 길이 리턴
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .email(user.getEmail())
                .registeredAt(user.getRegisteredAt())
                .unRegisteredAt(user.getUnregisteredAt())
                .build();

        //Header + data return
        return Header.OK(userApiResponse);
    }
}
