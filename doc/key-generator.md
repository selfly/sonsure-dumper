# 扩展主键生成

`KeyGenerator`是定义的主键生成器接口，里面就一个方法：

    /**
     * 生成主键值
     *
     * @param clazz the clazz
     * @return value
     */
    Object generateKeyValue(Class<?> clazz);
    
传入当前的实体类class对象，返回生成的主键值，主键值可以是Long、String等类型。

以下列举了两种不同的实现供参考。

## UUID主键生成

    public class UUIDKeyGenerator implements KeyGenerator {
    
        @Override
        public Object generateKeyValue(Class<?> clazz) {
            return UUIDUtils.getUUID32();
        }
    }
    
直接返回生成的UUID字符串值即可。

## Oracle序列主键生成

这里使用了oracle的序列，以下代码遵守了序列命名约定：序列名 = seq_ + tableName

    public class OracleKeyGenerator implements KeyGenerator {
    
        @Override
        public Object generateKeyValue(Class<?> clazz) {
            //根据实体名获取主键序列名
            String tableName = NameUtils.getUnderlineName(clazz.getSimpleName());
            return String.format("`{{SEQ_%s.NEXTVAL}}`", tableName);
        }
    }

Oracle的序列主键比较特别，并不像其它主键一样直接生成值使用，而是传入相关的序列名称，在数据库实际执行sql时才拿到的主键值。

这里用`{{ }}`包围了返回的序列名，表示不使用传参。如果使用传参，数据库会把它当作普通字符串处理。

`号包围则是seq_table.nextval这种格式在程序中进行sql解析时会误认为是表名.列名，这里把它表示成是一个整体。

*以上两种实现已包含在组件当中，如果需要直接使用即可。*

*当不指定时，默认使用的是数据库自增主键。*