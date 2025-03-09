package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.UpdateUserDTO;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.dtos.UserResponseDTO;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.UserRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Fetch all users
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Fetch user by ID
    public UserResponseDTO getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return convertToResponseDTO(user);
    }

    // Create new user with optional profile photo
    public User createUser(UserDTO userDTO, MultipartFile profilePhoto) {
        User user = convertFromDTO(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                user.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing profile photo");
            }
        }

        return userRepository.save(user);
    }

    // Update full user profile with optional profile photo
    public UserResponseDTO updateUser(Long id, UpdateUserDTO updatedUserDTO, MultipartFile profilePhoto) throws UserNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        updateExistingUser(existingUser, updatedUserDTO);

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                existingUser.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing profile photo");
            }
        }

        return convertToResponseDTO(userRepository.save(existingUser));
    }

    private void updateExistingUser(User user, UpdateUserDTO dto) {
        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null && !dto.getLastName().isBlank()) user.setLastName(dto.getLastName());
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) user.setPhone(dto.getPhone());
        if (dto.getStreet() != null && !dto.getStreet().isBlank()) user.setStreet(dto.getStreet());
        if (dto.getApartment() != null && !dto.getApartment().isBlank()) user.setApartment(dto.getApartment());
        if (dto.getCity() != null && !dto.getCity().isBlank()) user.setCity(dto.getCity());
        if (dto.getState() != null && !dto.getState().isBlank()) user.setState(dto.getState());
        if (dto.getZipcode() != null && !dto.getZipcode().isBlank()) user.setZipcode(dto.getZipcode());
        if (dto.getCountry() != null && !dto.getCountry().isBlank()) user.setCountry(dto.getCountry());
        if (dto.getBio() != null && !dto.getBio().isBlank()) user.setBio(dto.getBio());
    }

    // PATCH: Update partial fields dynamically, including profile photo
    public UserResponseDTO patchUser(Long id, Map<String, String> updates, MultipartFile profilePhoto) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> {
                    if (!value.isBlank()) user.setFirstName(value);
                }
                case "lastName" -> {
                    if (!value.isBlank()) user.setLastName(value);
                }
                case "phone" -> {
                    if (!value.isBlank()) user.setPhone(value);
                }
                case "street" -> {
                    if (!value.isBlank()) user.setStreet(value);
                }
                case "apartment" -> {
                    if (!value.isBlank()) user.setApartment(value);
                }
                case "city" -> {
                    if (!value.isBlank()) user.setCity(value);
                }
                case "state" -> {
                    if (!value.isBlank()) user.setState(value);
                }
                case "zipcode" -> {
                    if (!value.isBlank()) user.setZipcode(value);
                }
                case "country" -> {
                    if (!value.isBlank()) user.setCountry(value);
                }
                case "bio" -> {
                    if (!value.isBlank()) user.setBio(value);
                }
                case "birthday" -> {
                    if (!value.isBlank()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date birthday = null;
                        try {
                            birthday = formatter.parse(value);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        user.setBirthday(birthday);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                user.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing profile photo");
            }
        }

        return convertToResponseDTO(userRepository.save(user));
    }

    // Delete user
    public void deleteUser(Long id) throws UserNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(existingUser);
    }

    // Convert User Entity to Response DTO
    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStreet(user.getStreet());
        dto.setApartment(user.getApartment());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setZipcode(user.getZipcode());
        dto.setCountry(user.getCountry());

        // Convert profile photo to Base64 string
        if (user.getProfilePhoto() != null) {
            dto.setProfilePhoto(Base64.getEncoder().encodeToString(user.getProfilePhoto()));
        } else {
            dto.setProfilePhoto(null);
        }

        dto.setBio(user.getBio());
        dto.setJoinedClubs(user.getJoinedClubs());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    // Convert User DTO to Entity
    private User convertFromDTO(UserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setStreet(dto.getStreet());
        user.setApartment(dto.getApartment());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setZipcode(dto.getZipcode());
        user.setCountry(dto.getCountry());
        user.setBio(dto.getBio());
        user.setRole(dto.getRole());
        user.setBirthday(dto.getBirthday());
        return user;
    }

    public List<UserResponseDTO> getAllUsersStartingWithEmail(String email) {
        return userRepository.findByEmailStartingWith(email)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    /*




     */

        // Get user role by email (for JWT token processing)
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User role not found for email: " + email);
        }
        return user.getRole().toString();
    }

    // Load user for Spring Security Authentication
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))
        );
    }

    // Update user by Admin (full update)
    public UserResponseDTO updateUserFromUserDTO(Long id, UserDTO userDTO, MultipartFile profilePhoto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return convertToResponseDTO(updateUserByAdmin(userDTO, existingUser, profilePhoto));
    }

    private User updateUserByAdmin(UserDTO userDTO, User existingUser, MultipartFile profilePhoto) {
        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isBlank())
            existingUser.setFirstName(userDTO.getFirstName());

        if (userDTO.getLastName() != null && !userDTO.getLastName().isBlank())
            existingUser.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank())
            existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPhone() != null && !userDTO.getPhone().isBlank())
            existingUser.setPhone(userDTO.getPhone());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank())
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getStreet() != null && !userDTO.getStreet().isBlank())
            existingUser.setStreet(userDTO.getStreet());

        if (userDTO.getApartment() != null && !userDTO.getApartment().isBlank())
            existingUser.setApartment(userDTO.getApartment());

        if (userDTO.getCity() != null && !userDTO.getCity().isBlank())
            existingUser.setCity(userDTO.getCity());

        if (userDTO.getState() != null && !userDTO.getState().isBlank())
            existingUser.setState(userDTO.getState());

        if (userDTO.getZipcode() != null && !userDTO.getZipcode().isBlank())
            existingUser.setZipcode(userDTO.getZipcode());

        if (userDTO.getCountry() != null && !userDTO.getCountry().isBlank())
            existingUser.setCountry(userDTO.getCountry());

        if (userDTO.getBio() != null && !userDTO.getBio().isBlank())
            existingUser.setBio(userDTO.getBio());

        if (userDTO.getRole() != null)
            existingUser.setRole(userDTO.getRole());

        if (userDTO.getBirthday() != null)
            existingUser.setBirthday(userDTO.getBirthday());

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                existingUser.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing profile photo");
            }
        }

        return userRepository.save(existingUser);
    }

    public UserResponseDTO patchUserProfilePhoto(Long id, MultipartFile profilePhoto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                user.setProfilePhoto(profilePhoto.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error processing profile photo");
            }
        }

        return convertToResponseDTO(userRepository.save(user));
    }
}
