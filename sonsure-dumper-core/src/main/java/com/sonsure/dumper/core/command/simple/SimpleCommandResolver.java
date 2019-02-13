package com.sonsure.dumper.core.command.simple;

import com.sonsure.commons.spring.scan.ClassPathBeanScanner;
import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.command.sql.CommandResolver;
import com.sonsure.dumper.core.command.sql.CommandToSqlTranslator;
import com.sonsure.dumper.core.command.sql.JSqlParserCommandToSqlTranslator;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.mapping.MappingHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCommandResolver implements CommandResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleCommandResolver.class);

    /**
     * 扫描的包
     */
    protected String modelPackages;

    /**
     * 类名称映射
     */
    protected Map<String, Class<?>> simpleClassMapping = new HashMap<>();

    /**
     * 自定义类名称映射
     */
    protected Map<String, Class<?>> customClassMapping;

    protected CommandToSqlTranslator commandToSqlTranslator;

    protected static boolean initialized = false;

    public SimpleCommandResolver() {
        this(null);
    }

    public SimpleCommandResolver(String modelPackages) {
        this.modelPackages = modelPackages;
        this.commandToSqlTranslator = new JSqlParserCommandToSqlTranslator();
    }

    /**
     * 初始化类映射等
     */
    public void init() {

        if (initialized) {
            return;
        }

        initialized = true;

        if (StringUtils.isBlank(modelPackages)) {
            return;
        }

        String[] pks = StringUtils.split(modelPackages, ",");
        for (String pk : pks) {
            List<String> classes = ClassPathBeanScanner.scanClasses(pk);
            for (String clazz : classes) {

                int index = StringUtils.lastIndexOf(clazz, ".");
                String simpleName = StringUtils.substring(clazz, index + 1);

                if (simpleClassMapping.containsKey(simpleName)) {
                    LOG.warn("短类名相同，使用时请自定义短类名或使用完整类名:class1:{},class2:{}", simpleClassMapping.get(simpleName), clazz);
                } else {
                    Class<?> aClass = ClassUtils.loadClass(clazz);
                    simpleClassMapping.put(simpleName, aClass);
                }
            }
        }
    }

    @Override
    public String resolve(String command, CommandTable commandTable, MappingHandler mappingHandler) {
        this.init();
        return commandToSqlTranslator.getSql(command, mappingHandler, simpleClassMapping, customClassMapping);
    }

    public void setModelPackages(String modelPackages) {
        this.modelPackages = modelPackages;
    }

    public void setCustomClassMapping(Map<String, Class<?>> customClassMapping) {
        this.customClassMapping = customClassMapping;
    }

    public void setCommandToSqlTranslator(CommandToSqlTranslator commandToSqlTranslator) {
        this.commandToSqlTranslator = commandToSqlTranslator;
    }
}
