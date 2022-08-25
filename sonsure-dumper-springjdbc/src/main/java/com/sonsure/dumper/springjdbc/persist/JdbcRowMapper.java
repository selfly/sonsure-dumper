/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sonsure.dumper.springjdbc.persist;

import com.sonsure.dumper.core.mapping.MappingHandler;
import com.sonsure.dumper.springjdbc.convert.LocalDateTimeConverter;
import com.sonsure.dumper.springjdbc.convert.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 重写了spring的{@link BeanPropertyRowMapper}.
 * 在原来的基础上添加了自定义映射以及注解的支持
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @since 2.5
 */
public class JdbcRowMapper<T> implements RowMapper<T> {

    /**
     * Logger available to subclasses
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String dialect;

    private MappingHandler mappingHandler;

    /**
     * The class we are mapping to
     */
    private Class<T> mappedClass;

    /**
     * Whether we're strictly validating
     */
    private boolean checkFullyPopulated = false;

    /**
     * Whether we're defaulting primitives when mapping a null value
     */
    private boolean primitivesDefaultedForNullValue = false;

    /**
     * Map of the fields we provide mapping for
     */
    private Map<String, PropertyDescriptor> mappedFields;

    /**
     * Set of bean properties we provide mapping for
     */
    private Set<String> mappedProperties;

    /**
     * The Type converters.
     */
    private List<TypeConverter> typeConverters;

    /**
     * Create a new {@code BeanPropertyRowMapper} for bean-style configuration.
     *
     * @see #setCheckFullyPopulated
     */
    public JdbcRowMapper() {
    }

    /**
     * Create a new {@code BeanPropertyRowMapper}, accepting unpopulated
     * properties in the target bean.
     * <p>Consider using the {@link #newInstance} factory method instead,
     * which allows for specifying the mapped type once only.
     *
     * @param dialect        the dialect
     * @param mappedClass    the class that each row should be mapped to
     * @param mappingHandler the mapping handler
     */
    public JdbcRowMapper(String dialect, Class<T> mappedClass, MappingHandler mappingHandler) {
        initialize(dialect, mappedClass, mappingHandler);
    }

//    /**
//     * Create a new {@code BeanPropertyRowMapper}.
//     *
//     * @param mappedClass         the class that each row should be mapped to
//     * @param mappingHandler      the mapping handler
//     * @param checkFullyPopulated whether we're strictly validating that
//     *                            all bean properties have been mapped from corresponding database fields
//     */
//    public JdbcRowMapper(Class<T> mappedClass, MappingHandler mappingHandler, boolean checkFullyPopulated) {
//        initialize(dialect,mappedClass, mappingHandler);
//        this.checkFullyPopulated = checkFullyPopulated;
//    }

    /**
     * Get the class that we are mapping to.
     */
    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    /**
     * Set whether we're strictly validating that all bean properties have been mapped
     * from corresponding database fields.
     * <p>Default is {@code false}, accepting unpopulated properties in the target bean.
     */
    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    /**
     * Return whether we're strictly validating that all bean properties have been
     * mapped from corresponding database fields.
     */
    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    /**
     * Set whether we're defaulting Java primitives in the case of mapping a null value
     * from corresponding database fields.
     * <p>Default is {@code false}, throwing an exception when nulls are mapped to Java primitives.
     */
    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    /**
     * Return whether we're defaulting Java primitives in the case of mapping a null value
     * from corresponding database fields.
     */
    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    /**
     * Initialize the mapping metadata for the given class.
     *
     * @param dialect        the dialect
     * @param mappedClass    the mapped class
     * @param mappingHandler the mapping handler
     */
    protected void initialize(String dialect, Class<T> mappedClass, MappingHandler mappingHandler) {
        this.dialect = dialect.toLowerCase();
        this.mappedClass = mappedClass;
        this.mappingHandler = mappingHandler;
        this.mappedFields = new HashMap<>();
        this.mappedProperties = new HashSet<>();
        this.initializeTypeConverter();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(lowerCaseName(pd.getName()), pd);
                this.mappedProperties.add(pd.getName());
            }
        }
    }

    protected void initializeTypeConverter() {
        if (this.dialect.contains("sqlite")) {
            this.typeConverters = new ArrayList<>();
            this.typeConverters.add(new LocalDateTimeConverter());
        }
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding underscore.
     *
     * @param name the original name
     * @return the converted name
     * @see #lowerCaseName
     * @since 4.2
     */
    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    /**
     * Convert the given name to lower case.
     * By default, conversions will happen within the US locale.
     *
     * @param name the original name
     * @return the converted name
     * @since 4.2
     */
    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    /**
     * Extract the values for all columns in the current row.
     * <p>Utilizes public setters and result set metadata.
     *
     * @see ResultSetMetaData
     */
    @Override
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated() ? new HashSet<String>() : null);

        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(rsmd, index);
            String field = column.replaceAll(" ", "");
            field = lowerCaseName(mappingHandler.getField(this.mappedClass, field));
            PropertyDescriptor pd = this.mappedFields.get(field);
            if (pd != null) {
                try {
                    Object value = getColumnValue(rs, index, pd);
                    if (rowNumber == 0 && logger.isDebugEnabled()) {
                        logger.debug("Mapping column '" + column + "' to property '" + pd.getName() + "' of type ["
                                + ClassUtils.getQualifiedName(pd.getPropertyType()) + "]");
                    }
                    if (this.typeConverters != null) {
                        for (TypeConverter typeConverter : this.typeConverters) {
                            if (typeConverter.support(pd.getPropertyType(), value)) {
                                value = typeConverter.convert(pd.getPropertyType(), value);
                            }
                        }
                    }
                    try {
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (TypeMismatchException ex) {
                        if (value == null && this.primitivesDefaultedForNullValue) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Intercepted TypeMismatchException for row " + rowNumber + " and column '"
                                        + column + "' with null value when setting property '" + pd.getName()
                                        + "' of type [" + ClassUtils.getQualifiedName(pd.getPropertyType())
                                        + "] on object: " + mappedObject, ex);
                            }
                        } else {
                            throw ex;
                        }
                    }
                    if (populatedProperties != null) {
                        populatedProperties.add(pd.getName());
                    }
                } catch (NotWritablePropertyException ex) {
                    throw new DataRetrievalFailureException("Unable to map column '" + column + "' to property '"
                            + pd.getName() + "'", ex);
                }
            } else {
                // No PropertyDescriptor found
                if (rowNumber == 0 && logger.isDebugEnabled()) {
                    logger.debug("No property found for column '" + column + "' mapped to field '" + field + "'");
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields "
                    + "necessary to populate object of class ["
                    + this.mappedClass.getName() + "]: " + this.mappedProperties);
        }

        return mappedObject;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>The default implementation calls
     * {@link JdbcUtils#getResultSetValue(ResultSet, int, Class)}.
     * Subclasses may override this to check specific value types upfront,
     * or to post-process values return from {@code getResultSetValue}.
     *
     * @param rs    is the ResultSet holding the data
     * @param index is the column index
     * @param pd    the bean property that each result object is expected to match
     *              (or {@code null} if none specified)
     * @return the Object value
     * @throws SQLException in case of extraction failure
     * @see org.springframework.jdbc.support.JdbcUtils#getResultSetValue(ResultSet, int, Class)
     */
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    /**
     * Static factory method to create a new {@code BeanPropertyRowMapper}
     * (with the mapped class specified only once).
     *
     * @param <T>            the type parameter
     * @param dialect        the dialect
     * @param mappedClass    the class that each row should be mapped to
     * @param mappingHandler the mapping handler
     * @return the jdbc row mapper
     */
    public static <T> JdbcRowMapper<T> newInstance(String dialect, Class<T> mappedClass, MappingHandler mappingHandler) {
        return new JdbcRowMapper<T>(dialect, mappedClass, mappingHandler);
    }

}
