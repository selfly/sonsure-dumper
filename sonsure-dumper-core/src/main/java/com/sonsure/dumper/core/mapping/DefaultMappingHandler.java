package com.sonsure.dumper.core.mapping;

/**
 * 默认名称处理handler
 * <p>
 * User: liyd
 * Date: 2/12/14
 * Time: 4:51 PM
 */
public class DefaultMappingHandler extends AbstractMappingHandler {

    public DefaultMappingHandler() {
        this(null);
    }

    public DefaultMappingHandler(String modelPackages) {
        super(modelPackages);
    }
}
