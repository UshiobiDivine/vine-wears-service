package com.divine.project.model.user;
import com.divine.project.model.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User extends DateAudit {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String providerId;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expire_date")
    private String verificationCodeExpiryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
