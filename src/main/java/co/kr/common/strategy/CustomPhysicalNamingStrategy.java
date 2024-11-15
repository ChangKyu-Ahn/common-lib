package co.kr.common.strategy;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    private static final String REPLACE_JPA_ENTITY_NAME = "Data";

    public CustomPhysicalNamingStrategy() {
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        String tableName = StringUtils.replace(name.getText(), REPLACE_JPA_ENTITY_NAME, StringUtils.EMPTY);

        return super.toPhysicalTableName(Identifier.toIdentifier(tableName), jdbcEnvironment);
    }

}