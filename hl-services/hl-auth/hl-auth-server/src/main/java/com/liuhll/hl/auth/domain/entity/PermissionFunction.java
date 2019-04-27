package com.liuhll.hl.auth.domain.entity;

import com.liuhll.hl.common.domain.auditing.AuditedEntity;
import javax.persistence.*;

@Table(name = "auth_permission_function")
public class PermissionFunction extends AuditedEntity {
    /**
     * 主键
     */
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    @Column(name = "PermissionId")
    private Long permissionid;

    @Column(name = "FunctionId")
    private Long functionid;

    /**
     * 获取主键
     *
     * @return Id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return PermissionId
     */
    public Long getPermissionid() {
        return permissionid;
    }

    /**
     * @param permissionid
     */
    public void setPermissionid(Long permissionid) {
        this.permissionid = permissionid;
    }

    /**
     * @return FunctionId
     */
    public Long getFunctionid() {
        return functionid;
    }

    /**
     * @param functionid
     */
    public void setFunctionid(Long functionid) {
        this.functionid = functionid;
    }
}