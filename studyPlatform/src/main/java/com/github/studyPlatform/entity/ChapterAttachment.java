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

public class ChapterAttachment extends Model<ChapterAttachment> {

    private static final long serialVersionUID = 1L;

        private Integer chapterId;
        private Integer attachmentId;
            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT)
        private LocalDateTime createAt;
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
            @TableField(fill = FieldFill.INSERT_UPDATE)
        private LocalDateTime updateAt;
        public Integer getChapterId() {
            return this.chapterId;
        }
        public void setChapterId(Integer chapterId) {
            this.chapterId = chapterId;
        }
        public Integer getAttachmentId() {
            return this.attachmentId;
        }
        public void setAttachmentId(Integer attachmentId) {
            this.attachmentId = attachmentId;
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