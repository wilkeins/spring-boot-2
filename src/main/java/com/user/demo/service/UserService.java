package com.user.demo.service;

import com.user.demo.config.AppConfig;
import com.user.demo.dto.PhoneDTO;
import com.user.demo.dto.UserDto;
import com.user.demo.dto.UserLoginResponseDTO;
import com.user.demo.dto.UserResponseDTO;
import com.user.demo.exceptions.CustomException;
import com.user.demo.model.Phone;
import com.user.demo.model.Users;
import com.user.demo.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AppConfig appConfig;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserResponseDTO registerUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new CustomException("El correo ya registrado", HttpStatus.CONFLICT);
        }

        String passwordPattern = appConfig.getPasswordPattern();

        if (!userDto.getPassword().matches(passwordPattern)) {
            throw new CustomException("La contraseña no cumple con los requisitos", HttpStatus.CONFLICT);
        }

        Users users = new Users();
        users.setUuid(UUID.randomUUID());
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(encodePassword(userDto.getPassword()));
        users.setActive(true);
        users.setLastLogin(LocalDateTime.now());

        if (userDto.getPhones() != null) {
            for (PhoneDTO phoneDTO : userDto.getPhones()) {
                Phone phone = convertToEntity(phoneDTO);
                users.addPhone(phone);
            }
        }
        users.setToken(tokenService.generateToken(users));
        Users savedUsers = userRepository.save(users);
        return convertToUserCreationResponseDTO(savedUsers);
    }

    public UserLoginResponseDTO getUserByToken(String token) {
        String email = tokenService.validateAndParseToken(token);
        Users user = userRepository.findByEmail(email).orElseThrow(() ->
                new CustomException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        user.setToken(tokenService.generateToken(user));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return convertToUserLoginResponseDTO(user);
    }

    private String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private Phone convertToEntity(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        phone.setNumber(phoneDTO.getNumber());
        phone.setCitycode(phoneDTO.getCitycode());
        phone.setCountrycode(phoneDTO.getCountrycode());
        return phone;
    }

    private UserResponseDTO convertToUserCreationResponseDTO(Users user) {
        UserResponseDTO dto = new UserResponseDTO();
        UserResponseDTO commonDto = convertToUserResponseDTOCommon(user);
        BeanUtils.copyProperties(commonDto, dto);
        return dto;
    }
    private List<UserLoginResponseDTO.PhoneDTO> convertToPhoneDTOList(Set<Phone> phones) {
        return phones.stream().map(phone -> {
            UserLoginResponseDTO.PhoneDTO phoneDTO = new UserLoginResponseDTO.PhoneDTO();
            phoneDTO.setNumber(phone.getNumber());
            phoneDTO.setCitycode(phone.getCitycode());
            phoneDTO.setCountrycode(phone.getCountrycode());
            return phoneDTO;
        }).collect(Collectors.toList());
    }

    private UserLoginResponseDTO convertToUserLoginResponseDTO(Users user) {
        UserLoginResponseDTO dto = new UserLoginResponseDTO();
        UserResponseDTO commonDto = convertToUserResponseDTOCommon(user);
        BeanUtils.copyProperties(commonDto, dto);
        dto.setPassword(user.getPassword());
        dto.setPhones(convertToPhoneDTOList(user.getPhones()));
        return dto;
    }

    public UUID getUserUuid(Users user) {
        return user.getUuid();
    }

    private UserResponseDTO convertToUserResponseDTOCommon(Users user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(getUserUuid(user));
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreated(user.getCreated());
        dto.setModified(user.getModified());
        dto.setLastLogin(user.getLastLogin());
        dto.setToken(user.getToken());
        dto.setIsActive(user.isActive());
        return dto;
    }
    public void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new CustomException("Token no proporcionado", HttpStatus.BAD_REQUEST);
        }
    }

}

