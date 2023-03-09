package com.happy3friends.toiletmapbackend.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "Role", schema = "dbo", catalog = "ToiletMap")
public class RoleEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Id", nullable = false)
    private int id;
    @Basic
    @Column(name = "Name", nullable = false, length = 10)
    private String name;
    @OneToMany(mappedBy = "roleByRoleId")
    private Collection<AccountEntity> accountsById;

}
