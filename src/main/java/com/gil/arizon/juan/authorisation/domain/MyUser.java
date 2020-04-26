package com.gil.arizon.juan.authorisation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
// this annotation is necessary for field role with default value
@DynamicInsert
public class MyUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long $id;

  @Column(nullable = false, unique = true)
  private String userName;

  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  // trying default role
  @ManyToOne(fetch = FetchType.LAZY )
  @JoinColumn(name="role", columnDefinition = "varchar(255) not null default 'COMMON_USER'")
  private Role $role;

  @Column(nullable = false)
  private String email;

  @Column(name="enabled", nullable = false)
  private Boolean $isEnabled = Boolean.TRUE;


  public Long getId() {
    return $id;
  }

  public void setId(Long $id) {
    this.$id = $id;
  }

  public Role getRole() {
    return $role;
  }

  public void setRole(Role $role) {
    this.$role = $role;
  }

  public Boolean getIsEnabled() {
    return $isEnabled;
  }

  public void setIsEnabled(Boolean $isEnabled) {
    this.$isEnabled = $isEnabled;
  }
}
