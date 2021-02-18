package com.github.studyPlatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

        private String username;
        private String password;
        private String role;
        private String name;
        private String sex;
        private Integer schoolId;
        private Integer collegeId;
        private Integer majorId;
            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createAt;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateAt;
        public String getUsername() {
            return this.username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getRole() {
            return this.role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getSex() {
            return this.sex;
        }
        public void setSex(String sex) {
            this.sex = sex;
        }
        public Integer getSchoolId() {
            return this.schoolId;
        }
        public void setSchoolId(Integer schoolId) {
            this.schoolId = schoolId;
        }
        public Integer getCollegeId() {
            return this.collegeId;
        }
        public void setCollegeId(Integer collegeId) {
            this.collegeId = collegeId;
        }
        public Integer getMajorId() {
            return this.majorId;
        }
        public void setMajorId(Integer majorId) {
            this.majorId = majorId;
        }
        public Integer getId() {
            return this.id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public LocalDateTime getCreateAt() {
            return this.createAt;
        }
        public void setCreateAt(LocalDateTime createAt) {
            this.createAt = createAt;
        }
        public LocalDateTime getUpdateAt() {
            return this.updateAt;
        }
        public void setUpdateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
        }
}