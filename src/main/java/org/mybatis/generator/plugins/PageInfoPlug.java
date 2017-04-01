package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by J lai on 2017/3/28 0028.
 */
public class PageInfoPlug extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement conditions = getSelectConditionBean(document,introspectedTable);
        document.getRootElement().getElements().add(conditions);
        return true;
    }

    public XmlElement getSelectConditionBean(Document document, IntrospectedTable introspectedTable) {
        String formattedContent = document.getFormattedContent();
        String content = "";
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        for(IntrospectedColumn column : allColumns){
            content += "<if test=\"entity."+column.getJavaProperty()+" != null\">\n" +
                    column.getActualColumnName()+" = #{entity."+column.getJavaProperty()+",jdbcType="+column.getJdbcType()+"}\n"+
                    "</if>\n";
        }
        content += "<where>\n" +
                "<if test=\"entity != null\">\n"+content+"</if>\n" +
                    "</where>";

        XmlElement conditions = new XmlElement("sql");
        conditions.addAttribute(new Attribute("id","selectConditionsBean"));
        conditions.getElements().add(new TextElement(content));

        return conditions;
    }

    @Override
    public boolean clientGenerated(Interface interfaze,
                                   TopLevelClass topLevelClass,
                                   IntrospectedTable introspectedTable) {
        return true;
    }
}
