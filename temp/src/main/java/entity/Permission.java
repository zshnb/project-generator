package .entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Permission extends Model<Permission> {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
        private String role;
        private String model;
        private String operation;
        public Integer getId() {
            return this.id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getRole() {
            return this.role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public String getModel() {
            return this.model;
        }
        public void setModel(String model) {
            this.model = model;
        }
        public String getOperation() {
            return this.operation;
        }
        public void setOperation(String operation) {
            this.operation = operation;
        }
}