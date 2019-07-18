# 扩展 CommandExecutor

通过`JdbcDao`操作Insert、Select、Update、Delete、nativeExecutor、mybatisExecutor本质上都是创建了一个`CommandExecutor`的实现。

`CommandExecutor`的生产是在`CommandExecutorFactory`内进行的，`CommandExecutorFactory`会找到对应的`CommandExecutorBuilder`然后构建出相应的CommandExecutor对象。

对于上述几个内置的CommandExecutor对象都是由`CommandExecutorBuilderImpl`来进行构建的，也可以实现自己的`CommandExecutor`来整合不同的持久化执行方式。

