package .entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
        private Integer parentId;
        private String name;
        private String icon;
        private String role;
        private String href;
        public Integer getId() {
            return this.id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public Integer getParentId() {
            return this.parentId;
        }
        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getIcon() {
            return this.icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public String getRole() {
            return this.role;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public String getHref() {
            return this.href;
        }
        public void setHref(String href) {
            this.href = href;
        }
}