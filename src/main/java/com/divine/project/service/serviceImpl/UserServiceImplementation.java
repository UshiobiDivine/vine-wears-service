package com.divine.project.service.serviceImpl;

import com.divine.project.exception.AppException;
import com.divine.project.exception.BadRequestException;
import com.divine.project.exception.PasswordMismatchException;
import com.divine.project.exception.VerificationCodeExpiredException;
import com.divine.project.model.user.Role;
import com.divine.project.model.user.RoleEnum;
import com.divine.project.model.user.User;
import com.divine.project.payload.requests.UpdateUserRequest;
import com.divine.project.payload.requests.UserChangePasswordRequest;
import com.divine.project.payload.requests.UserForgotPasswordRequest;
import com.divine.project.payload.responses.PagedResponse;
import com.divine.project.repository.RoleRepository;
import com.divine.project.repository.UserRepository;
import com.divine.project.service.UserService;
import com.divine.project.util.DateUtils;
import com.divine.project.util.mail.Mailjet;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private Mailjet mailjet;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, Mailjet mailjet) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.mailjet = mailjet;
    }

    @Override
    public User editUser(User user, UpdateUserRequest updateUserRequest) {
        if (user!=null){
            user.setEmail(updateUserRequest.getEmail());
            user.setName(updateUserRequest.getName());
            userRepository.save(user);
        }
        throw new AppException("Could not update user details");
    }

    @Override
    public boolean userChangePassword(User user, UserChangePasswordRequest userChangePasswordRequest) {

            String oldPassword = userChangePasswordRequest.getOldPassword();
            String userPassword = user.getPassword();
            boolean passwordMatches = passwordEncoder.matches(oldPassword, userPassword);

            String newPassword = userChangePasswordRequest.getNewPassword();
            String confirmNewPassword = userChangePasswordRequest.getConfirmNewPassword();

            if (!passwordMatches){
                throw new PasswordMismatchException("Old password is not correct");
            }
            if (!(newPassword.equals(confirmNewPassword))){
                throw new PasswordMismatchException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
    }

    @Override
    public boolean userForgotPassword(User user, UserForgotPasswordRequest forgotPasswordRequest) {
        String newPassword = forgotPasswordRequest.getNewPassword();
        String confirmNewPassword = forgotPasswordRequest.getConfirmNewPassword();

        if (!(newPassword.equals(confirmNewPassword))){
            throw new PasswordMismatchException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }


    public boolean sendAndSaveVerificationCode(String email){

        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            User user = userByEmail.get();

            Random random1 = new Random();
            int code = random1.ints(1, 187563, 986798)
                    .findFirst()
                    .getAsInt();
            System.out.println("PASSWORD RESET CODE IS " + code);

            int limit = 2;
            String timeLimit = DateUtils.verificationCodeExpiryTimeLimit(limit);

            String mailBody = String.format("\n\nYour password reset code is %s \n IT WILL EXPIRE IN %s minutes", code, limit);

            try {
                mailjet.sendMail(email, user.getName(), "Password Reset", mailBody);
            } catch (MailjetSocketTimeoutException e) {
                e.printStackTrace();
            } catch (MailjetException e) {
                throw new AppException(e.getMessage());
            }
            user.setVerificationCodeExpiryDate(timeLimit);
            user.setVerificationCode(String.valueOf(code));
            userRepository.save(user);
            return true;
        }
        return false;

    }

    @Override
    public boolean checkVerificationCode(User user, String code) throws ParseException {
        System.out.println("THE ENTERED VERIFICATION CODE BY THE USER IS " + code);
        String currentTime = DateUtils.getCurrentTime();
        String verificationCodeExpiryDate = user.getVerificationCodeExpiryDate();
        Date now = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(currentTime);
        Date expDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(verificationCodeExpiryDate);

        long result = now.getTime() - expDate.getTime();
        if (result>0){
            throw new VerificationCodeExpiredException("Code has expired, please provide a valid code");
        }
        String verificationCode = user.getVerificationCode();
        System.out.println("VERIFICATION CODE ISSSSSS " + verificationCode);
        if (!verificationCode.equals(code)){
            throw new BadRequestException("Wrong password reset code");
        }
        return true;
    }

    @Override
    public boolean giveAdminRole(Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isPresent()){
            Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
            if (roleOptional.isPresent()) {
                List<Role> roles = userById.get().getRoles();
                if (roles.contains(roleOptional.get())){
                    throw new BadRequestException("The user is already an admin");
                }
                roles.add(roleOptional.get());
                userRepository.save(userById.get());
                return true;
            }
            throw new BadRequestException("ADMIN ROLE NOT SET");
        }
        return false;
    }

    @Override
    public boolean removeAdminRole(Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isPresent()){
            Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
            if (roleOptional.isPresent()) {
                List<Role> roles = userById.get().getRoles();
                if (!roles.contains(roleOptional.get())){
                    throw new BadRequestException("The user is NOT an admin");
                }

                roles.remove(roleOptional.get());
                userRepository.save(userById.get());
                return true;
            }
            throw new BadRequestException("ADMIN ROLE NOT SET");
        }
        return false;
    }

    @Override
    public PagedResponse<User> getAllUsers() {
        return null;
    }
}
