package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.GenerateKey;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.management.ClassField;
import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.core.persist.KeyGenerator;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liyd on 17/4/14.
 */
public class InsertCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "insert into ";

    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {

        InsertContext insertContext = (InsertContext) executorContext;
        CommandContext commandContext = getCommonCommandContext(insertContext);
        MappingHandler mappingHandler = jdbcEngineConfig.getMappingHandler();
        String pkField = this.getPkField(insertContext.getModelClass(), mappingHandler);
        String pkColumn = mappingHandler.getColumn(insertContext.getModelClass(), pkField);
        GenerateKey generateKey = new GenerateKey();
        generateKey.setClazz(insertContext.getModelClass());
        generateKey.setColumn(pkColumn);

        commandContext.setGenerateKey(generateKey);

        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        StringBuilder argsCommand = new StringBuilder("(");

        command.append(this.getModelName(insertContext.getModelClass())).append(" (");

        boolean hasPkParam = false;
        for (ClassField classField : insertContext.getInsertFields()) {
            String fieldName = classField.getName();
            if (StringUtils.equalsIgnoreCase(pkField, fieldName)) {
                hasPkParam = true;
            }
            command.append(fieldName).append(",");
            argsCommand.append("?").append(",");
            commandContext.addParameter(classField.getValue());
        }
        if (!hasPkParam) {
            KeyGenerator keyGenerator = jdbcEngineConfig.getKeyGenerator();
            if (keyGenerator != null) {
                Object generateKeyValue = keyGenerator.generateKeyValue(insertContext.getModelClass());
                generateKey.setValue(generateKeyValue);
                generateKey.setParameter(keyGenerator.isParameter());
                //设置主键值，insert之后返回用
                commandContext.setGenerateKey(generateKey);
                //传参
                if (keyGenerator.isParameter()) {
                    command.append(pkField).append(",");
                    argsCommand.append("?").append(",");
                    commandContext.addParameter(generateKeyValue);
                } else {
                    //不传参方式，例如是oracle的序列名
                    command.append(pkField).append(",");
                    argsCommand.append(generateKeyValue).append(",");
                }
            }
        }
        command.deleteCharAt(command.length() - 1);
        argsCommand.deleteCharAt(argsCommand.length() - 1);
        argsCommand.append(")");
        command.append(")").append(" values ").append(argsCommand);
        commandContext.setCommand(command.toString());
        return commandContext;
    }
}
