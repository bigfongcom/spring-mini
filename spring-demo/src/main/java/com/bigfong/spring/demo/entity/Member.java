package com.bigfong.spring.demo.entity;

import com.bigfong.spring.framework.annotation.orm.Entity;
import com.bigfong.spring.framework.annotation.orm.Id;
import com.bigfong.spring.framework.annotation.orm.Table;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 * @author bigfong
 * @since 2019/10/6
 */
@Entity
@Table("t_member")
@Data
public class Member implements Serializable {
    @Id
    private Long id;
    private String name;
    private String addr;
    private Integer age;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", age=" + age +
                '}';
    }
}
