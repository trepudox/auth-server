package com.trepudox.authserver.dataprovider.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trepudox.authserver.dataprovider.enums.EnProfile;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@JsonIgnoreProperties(value = "password")
@EntityListeners(AuditingEntityListener.class)
public class UserModel implements Serializable {

    private static final long serialVersionUID = -6898167379430660643L;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private EnProfile profile;

    @Column(name = "score")
    private Long score;

    @LastModifiedDate
    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;

    @CreatedDate
    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

}
