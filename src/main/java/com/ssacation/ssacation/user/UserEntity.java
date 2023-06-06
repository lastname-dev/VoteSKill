package com.ssacation.ssacation.user;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User") // 테이블과 클래스명이 같을 경우 생략 가능
@Entity
public class UserEntity {
  
  // @GeneratedValue(strategy = GenerationType.xxx) : Primary Key의 키 생성 전략(Strategy)을 설정하고자 할 때 사용
  // GenerationType.IDENTITY : MySQL의 AUTO_INCREMENT 방식을 이용
  // GenerationType.AUTO(default) : JPA 구현체(Hibernate)가 생성 방식을 결정
  // GenerationType.SEQUENCE : DB의 SEQUENCE를 이용해서 키를 생성. @SequenceGenerator와 같이 사용
  // GenerationType.TABLE : 키 생성 전용 테이블을 생성해서 키 생성. @TableGenerator와 함께 사용
  
  @Id
  private String id;

  @Column(length = 20, nullable = false) // Column과 반대로 테이블에 컬럼으로 생성되지 않는 필드의 경우엔 @Transient 어노테이션을 적용한다.
  private String password;
  
  @Column(length = 20, nullable = false)
  private String name;
  
  @Column(length = 20, nullable = false, unique = true) // 닉네임을 유니크 키로 설정
  private String nickname;
  
  @Column(length = 200, nullable = false)
  private String email;
  
  @Column(length = 20, nullable = false)
  private Date birthDay;
  
  @Column(length = 200, nullable = false)
  private String phoneNum;
  
  @Column(length = 200, nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // Timestamp의 값을 현재 시간으로 자동 설정
  private Timestamp regDate;
}