package com.divine.project.security.oauth2;

import com.divine.project.exception.RoleNotFoundException;
import com.divine.project.model.user.Role;
import com.divine.project.model.user.RoleEnum;
import com.divine.project.model.user.User;
import com.divine.project.exception.OAuth2AuthenticationProcessingException;
import com.divine.project.model.user.AuthProvider;
import com.divine.project.repository.RoleRepository;
import com.divine.project.repository.UserRepository;
import com.divine.project.security.UserPrincipal;
import com.divine.project.security.oauth2.user.OAuth2UserInfo;
import com.divine.project.security.oauth2.user.OAuth2UserInfoFactory;
import com.divine.project.util.CapcaseString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.build(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());

        user.setName(CapcaseString.capitalizeString(oAuth2UserInfo.getName()));
        user.setEmail(oAuth2UserInfo.getEmail().toLowerCase());

        if(userRepository.count()==0) {
            List<Role> rolesList = new ArrayList<>();
            Optional<Role> adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
            if (adminRole.isPresent()) {
                rolesList.add(adminRole.get());
                user.setRoles(rolesList);

            }else {
                throw new RoleNotFoundException("Role could not be fetched from the database");
            }

        }else {
            List<Role> rolesList = new ArrayList<>();
            Optional<Role> userRole = roleRepository.findByName(RoleEnum.ROLE_USER);
            if (userRole.isPresent()) {
                rolesList.add(userRole.get());
                user.setRoles(rolesList);
            }else {
                throw new RoleNotFoundException("Role could not be fetched from the database");
            }
        }
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());

        return userRepository.save(existingUser);
    }

}
