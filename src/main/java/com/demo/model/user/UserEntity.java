package com.demo.model.user;

import com.demo.model.cart.CartEntity;
import com.demo.model.order.OrderEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @Email(message = "Email is not valid")
    private String email;

    private String username;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CartEntity cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
