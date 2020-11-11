Simple PoC to demonstrate double invocation of `ItemStream.close()`: First by `AbstractStep.execute()`, then by Spring's `DisposableBeanAdapter` during application context shutdown.

Depending on the underlying implementation, the second invocation can result in a warning log message. For example, a `StaxEventItemWriter` bean will produce this message:
```
2020-11-11 20:19:55.160  WARN 25948 --- [           main] o.s.b.f.support.DisposableBeanAdapter    : Destroy method 'close' on bean with name 'scopedTarget.writer' threw an exception: java.lang.NullPointerException: Cannot invoke "java.io.Writer.flush()" because "this.fWriter" is null
```
