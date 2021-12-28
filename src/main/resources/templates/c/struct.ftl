#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <string.h>
<#list entities as entity>
    struct ${entity.name}
    {
        <#list entity.fields as field>
            <#if field.type == "STRING">
                char ${field.name}[255];
            <#else>
                ${field.type.description} ${field.name};
            </#if>
        </#list>
    };

    typedef struct ${entity.name} ${entity.name?cap_first};
    ${entity.name?cap_first} ${entity.name}s[100];
    <#if (entity.menus?filter(it -> it.value == "add")?size > 0)>
        void add_${entity.name}();
    </#if>
    <#if (entity.menus?filter(it -> it.value == "edit")?size > 0)>
        void update_${entity.name}();
    </#if>
    <#if (entity.menus?filter(it -> it.value == "detail")?size > 0)>
        <#list entity.fields?filter(it -> it.searchable) as field>
            int find_${entity.name}_by_${field.name}();
        </#list>
    </#if>
    <#if (entity.menus?filter(it -> it.value == "delete")?size > 0)>
        <#list entity.fields?filter(it -> it.searchable) as field>
            void delete_${entity.name}_by_${field.name}();
        </#list>
    </#if>
    <#if entity.fileOperation>
        void save_${entity.name}_to_file();
        void load_${entity.name}_from_file();
    </#if>
    void ${entity.name}_to_string(${entity.name?cap_first} ${entity.name});
    void list_${entity.name}();
</#list>
int current = 0;
<#list entities as entity>
    <#if (entity.menus?filter(it -> it.value == "add")?size > 0)>
        void add_${entity.name}()
        {
            <#list entity.fields as field>
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
    <#if (entity.menus?filter(it -> it.value == "edit")?size > 0)>
        void update_${entity.name}()
        {
            <#assign updateField>${entity.fields?filter(it -> it.searchable)[0].name}</#assign>
            int index = find_${entity.name}_by_${updateField}();
            <#list entity.fields?filter(it -> it.name != updateField) as field>
                printf("请输入${field.comment}: ");
                <#switch field.type>
                    <#case "INT">
                        scanf("%d", &${entity.name}s[index].${field.name});
                        <#break>
                    <#case "STRING">
                        scanf("%s", ${entity.name}s[index].${field.name});
                        <#break>
                    <#case "CHAR">
                        scanf("%c", &${entity.name}s[index].${field.name});
                        <#break>

                </#switch>
            </#list>
            printf("修改成功.\n");
        }
    </#if>
    <#if (entity.menus?filter(it -> it.value == "detail")?size > 0)>
        <#list entity.fields?filter(it -> it.searchable) as field>
            int find_${entity.name}_by_${field.name}()
            {
                <#if field.type == "STRING">
                    char keyword[255];
                <#else>
                    ${field.type.description} keyword;
                </#if>
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
                            ${entity.name}_to_string(${entity.name}s[i]);
                            return i;
                        }
                    <#else>
                        if (${entity.name}s[i].${field.name} == keyword)
                        {
                            ${entity.name}_to_string(${entity.name}s[i]);
                            return i;
                        }
                    </#if>
                }
                printf("记录不存在\n");
                return -1;
            }
        </#list>
    </#if>
    <#if (entity.menus?filter(it -> it.value == "delete")?size > 0)>
        <#list entity.fields?filter(it -> it.searchable) as field>
            void delete_${entity.name}_by_${field.name}()
            {
                int index = find_${entity.name}_by_${field.name}();
                int i = 0;
                if (index == -1)
                {
                    printf("记录不存在\n");
                    return;
                }
                for (; i < current - 1; i++)
                {
                    ${entity.name}s[i] = ${entity.name}s[i + 1];
                }
                current--;
                printf("删除成功\n");
            }
        </#list>
    </#if>
    <#if entity.fileOperation>
        void save_${entity.name}_to_file()
        {
            FILE* pf;
            int i;
            pf = fopen("${entity.name}.txt", "w+");
            if (!pf)
            {
                return;
            }
            <#assign format>
                <#list entity.fields as field>
                    <#switch field.type>
                        <#case "INT">
                            %d<#t>
                            <#break>
                        <#case "STRING">
                            %s<#t>
                            <#break>
                        <#case "CHAR">
                            %c<#t>
                            <#break>
                    </#switch>
                    <#if field_has_next> </#if><#t>
                </#list>
            </#assign>
            <#assign params>
                <#list entity.fields as field>
                    ${entity.name}s[i].${field.name}<#t>
                    <#if field_has_next>, </#if><#t>
                </#list>
            </#assign>
            for (i = 0; i < current; i++)
            {
                fprintf(pf, "${format}\n", ${params});
            }
            fclose(pf);
            printf("保存成功\n");
        }
        void load_${entity.name}_from_file()
        {
            FILE* pf;
            pf = fopen("${entity.name}.txt", "r");
            <#assign format>
                <#list entity.fields as field>
                    <#switch field.type>
                        <#case "INT">
                            %d<#t>
                            <#break>
                        <#case "STRING">
                            %s<#t>
                            <#break>
                        <#case "CHAR">
                            %c<#t>
                            <#break>
                    </#switch>
                    <#if field_has_next> </#if><#t>
                </#list>
            </#assign>
            <#assign params>
                <#list entity.fields as field>
                    <#switch field.type>
                        <#case "INT">
                            &${entity.name}s[current].${field.name}<#t>
                            <#break>
                        <#case "STRING">
                            ${entity.name}s[current].${field.name}<#t>
                            <#break>
                        <#case "CHAR">
                            &${entity.name}s[current].${field.name}<#t>
                            <#break>
                    </#switch>
                    <#if field_has_next>, </#if><#t>
                </#list>
            </#assign>
            while (fscanf(pf, "${format}\n", ${params}) != EOF)
            {
                current++;
            }
            fclose(pf); //关闭文件
            printf("加载成功\n");
        }
    </#if>
    void ${entity.name}_to_string(${entity.name?cap_first} ${entity.name})
    {
        <#assign format>
            <#list entity.fields as field>
                ${field.name}: <#t>
                <#switch field.type>
                    <#case "INT">
                        %d<#t>
                        <#break>
                    <#case "STRING">
                        %s<#t>
                        <#break>
                    <#case "CHAR">
                        %c<#t>
                        <#break>
                </#switch>
                <#if field_has_next>,</#if><#t>
            </#list>
        </#assign>
        <#assign params>
            <#list entity.fields as field>
                ${entity.name}.${field.name}<#t>
                <#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        printf("${format}\n", ${params});
    }
    void list_${entity.name}()
    {
        int i;
        for (i = 0;i < current;i++)
        {
            ${entity.name}_to_string(${entity.name}s[i]);
        }
    }
</#list>
void menu()
{
    printf("${name}\n");
    <#assign i = 0>
    <#list entities as entity>
    <#list entity.menus as menu>
        <#assign i = i + 1>
        printf("${i}. ${menu.name}\n");
    </#list>
    <#if !entity_has_next>
        printf("${i + 1}. 退出\n");
    </#if>
    </#list>
}
int main()
{
    int select_menu = 0;
    while (1)
    {
        menu();
        scanf("%d", &select_menu);

        <#assign i = 0>
        <#list entities as entity>
        <#list entity.menus as menu>
            <#assign i = i + 1>
            if (select_menu == ${i})
            {
                ${menu.method};
            }
        </#list>
        </#list>
    }
}
