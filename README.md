Simple PoC to demonstrate double invocation of `ItemStream.close()`. First by `AbstractStep.execute()`, second by Spring's `DisposableBeanAdapter` during application context shutdown.

The second invocation results in a warning log message:
```
2020-11-11 20:19:55.160  WARN 25948 --- [           main] o.s.b.f.support.DisposableBeanAdapter    : Destroy method 'close' on bean with name 'scopedTarget.writer' threw an exception: java.lang.NullPointerException: Cannot invoke "java.io.Writer.flush()" because "this.fWriter" is null
```
