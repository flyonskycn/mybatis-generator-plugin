package com.flyonsky.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 自定义过滤条件
 * @author Administrator
 *
 */
public class CustomWherePlugin extends PluginAdapter{

	
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addField(topLevelClass, introspectedTable, "customWhere");  
		return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
	}
	
	@Override
	public boolean sqlMapExampleWhereClauseElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		XmlElement isNotNullElement = new XmlElement("if"); 
        isNotNullElement.addAttribute(new Attribute("test", "customWhere != null"));
        isNotNullElement.addElement(new TextElement(" ${customWhere} "));
        XmlElement whereElement = (XmlElement)element.getElements().get(0);
        whereElement.addElement(isNotNullElement);
		return super.sqlMapExampleWhereClauseElementGenerated(element, introspectedTable);
	}
	
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
    private void addField(TopLevelClass topLevelClass,  
            IntrospectedTable introspectedTable, String name) {  
        CommentGenerator commentGenerator = context.getCommentGenerator();  
        Field field = new Field();  
        field.setVisibility(JavaVisibility.PROTECTED);  
        field.setType(PrimitiveTypeWrapper.getStringInstance());  
        field.setName(name);  
        commentGenerator.addFieldComment(field, introspectedTable);  
        topLevelClass.addField(field);  
        char c = name.charAt(0);  
        String camel = Character.toUpperCase(c) + name.substring(1);  
        Method method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setName("set" + camel);  
        method.addParameter(new Parameter(PrimitiveTypeWrapper.getStringInstance(), name));  
        method.addBodyLine("this." + name + "=" + name + ";");  
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
        method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setReturnType(PrimitiveTypeWrapper.getStringInstance());  
        method.setName("get" + camel);  
        method.addBodyLine("return " + name + ";");  
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
    } 
}
