package ${packageName};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ${name?cap_first} extends Model<${name?cap_first}> {

    private static final long serialVersionUID = 1L;

    <#list fields! as field>
        <#if field.primary>
            @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        </#if>
        private ${field.type} ${field.name};
    </#list>
    <#list fields! as field>
        public ${field.type} get${field.name?cap_first}() {
            return this.${field.name};
        }
        public void set${field.name?cap_first}(${field.type} ${field.name}) {
            this.${field.name} = ${field.name};
        }
    </#list>
}