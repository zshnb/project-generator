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

public class Submission extends Model<Submission> {

    private static final long serialVersionUID = 1L;

        private Integer userId;
        private String details;
        private Integer score;
        private Integer associateId;
        private Integer type;
            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createAt;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateAt;
        public Integer getUserId() {
            return this.userId;
        }
        public void setUserId(Integer userId) {
            this.userId = userId;
        }
        public String getDetails() {
            return this.details;
        }
        public void setDetails(String details) {
            this.details = details;
        }
        public Integer getScore() {
            return this.score;
        }
        public void setScore(Integer score) {
            this.score = score;
        }
        public Integer getAssociateId() {
            return this.associateId;
        }
        public void setAssociateId(Integer associateId) {
            this.associateId = associateId;
        }
        public Integer getType() {
            return this.type;
        }
        public void setType(Integer type) {
            this.type = type;
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