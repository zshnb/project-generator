#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <string.h>
<#list entities as entity>
    struct ${entity.name}
    {
        <#list entity.fields as field>
            ${field.type.description} ${field.name};
        </#list>
    };

    typedef struct ${entity.name} ${entity.name?cap_first};
    ${entity.name?cap_first} ${entity.name}s[100];
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("add")>
        void save_${entity.name}();
    </#if>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("edit")>
        void update_${entity.name}();
    </#if>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("detail")>
        <#list entity.fields?filter(it -> it.searchable) as field>
            int find_by_${field.name}();
        </#list>
    </#if>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("delete")>
        <#list entity.fields?filter(it -> it.searchable) as field>
            void delete_by_${field.name}();
        </#list>
    </#if>
    void to_string(${entity.name?cap_first} ${entity.name});
</#list>
int current = 0;
<#list entities as entity>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("add")>
        void save_${entity.name}()
        {
            <#list entity.fields?filter(it -> it.searchable) as field>
                printf("请输入${field.comment}: ");
                <#switch field.type>
                    <#case "INT">
                        scanf("%d", &${entity.name}s[current].${field.name});
                        <#break>
                    <#case "STRING">
                        scanf("%s", ${entity.name}s[current].${field.name});
                        <#break>
                    <#case "CHAR">
                        scanf("%c", &${entity.name}s[current].${field.name});
                        <#break>

                </#switch>
            </#list>
            printf("添加成功.\n");
            current++;
        }
    </#if>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("edit")>
        void update_${entity.name}();
    </#if>
    <#if menus?filter(it -> it.entity == entity.name)?seq_contains("detail")>
        <#list entity.fields?filter(it -> it.searchable) as field>
            int find_by_${field.name}()
            {
                ${field.type.description} keyword;
                int i;
                printf("请输入${field.comment}: ");
                <#switch field.type>
                    <#case "INT">
                        scanf("%d", &keyword);
                        <#break>
                    <#case "STRING">
                        scanf("%s", keyword);
                        <#break>
                    <#case "CHAR">
                        scanf("%c", &keyword);
                        <#break>
                </#switch>
                for (i = 0;i < current;i++)
                {
                    <#if field.type == "STRING">
                        if (strcmp(${entity.name}s[i].${field.name}, keyword) == 0)
                        {
                            return i;
                        }
                    </#if>
                }
                return -1;
            }
        </#list>
    </#if>
        <#if menus?filter(it -> it.entity == entity.name)?seq_contains("delete")>
        <#list entity.fields?filter(it -> it.searchable) as field>
            void delete_by_${field.name}();
        </#list>
    </#if>
    void to_string(${entity.name?cap_first} ${entity.name});
</#list>