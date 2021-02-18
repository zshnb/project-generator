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

public class Exam extends Model<Exam> {

    private static final long serialVersionUID = 1L;

        private Integer courseId;
        private Integer userId;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private LocalDateTime startAt;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        private LocalDateTime endAt;
        private Integer duration;
        private String problems;
            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createAt;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateAt;
        public Integer getCourseId() {
            return this.courseId;
        }
        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }
        public Integer getUserId() {
            return this.userId;
        }
        public void setUserId(Integer userId) {
            this.userId = userId;
        }
        public LocalDateTime getStartAt() {
            return this.startAt;
        }
        public void setStartAt(LocalDateTime startAt) {
            this.startAt = startAt;
        }
        public LocalDateTime getEndAt() {
            return this.endAt;
        }
        public void setEndAt(LocalDateTime endAt) {
            this.endAt = endAt;
        }
        public Integer getDuration() {
            return this.duration;
        }
        public void setDuration(Integer duration) {
            this.duration = duration;
        }
        public String getProblems() {
            return this.problems;
        }
        public void setProblems(String problems) {
            this.problems = problems;
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