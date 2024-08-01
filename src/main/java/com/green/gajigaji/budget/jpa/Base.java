package com.green.gajigaji.budget.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Base {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime inputDt;

    @LastModifiedDate
    private LocalDateTime updateDt;
}
