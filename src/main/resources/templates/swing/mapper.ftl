package ${packageName};
<#assign name>${entity.name?cap_first}</#assign>
import ${entityPackageName}.${name};
import org.apache.ibatis.annotations.Param;

public interface ${name}Mapper {
    ${name} selectById(int id);
    List<${name}> list();
    void insert(${name} ${name?uncap_first});
    void update(${name} ${name?uncap_first});
    void deleteById(int id);
}
