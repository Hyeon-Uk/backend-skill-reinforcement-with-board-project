package com.example.board.member.repository.infrastructure.entity;

import com.example.board.common.entity.BaseEntity;
import com.example.board.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "MEMBER")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "update MEMBER set DELETED_AT = NOW() WHERE MEMBER_PK = ?")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_PK")
    private Long id;

    @Column(name = "MEMBER_ID", unique = true, nullable = false, length = 20)
    private String userId;

    @Column(name = "MEMBER_PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "MEMBER_EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "MEMBER_NAME", nullable = false, length = 10)
    private String name;

    @Column(name = "MEMBER_NICKNAME", nullable = false, unique = true, length = 20)
    private String nickname;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public static MemberEntity fromDomain(Member member) {
        return MemberEntity.builder()
                .id(member.getPk())
                .userId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .name(member.getName())
                .password(member.getPassword())
                .build();
    }

    public Member toDomain() {
        return Member.builder()
                .pk(this.getId())
                .id(this.getUserId())
                .name(this.getName())
                .nickname(this.getNickname())
                .email(this.getEmail())
                .password(this.getPassword())
                .createdAt(super.getCreatedAt())
                .updatedAt(super.getUpdatedAt())
                .deletedAt(super.getDeletedAt())
                .build();
    }
}
