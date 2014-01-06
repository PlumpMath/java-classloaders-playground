import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

public class Example {

  /**
   * Relevant JVM options are "-XX:+TraceClassLoading" and "-XX:+TraceClassUnloading".
   */
  public static void main(String[] args) throws Exception {
    MemoryPoolMXBean permGenPool = getPermGenPoolMXBean();
    long max = 0;
    for (int i = 0; i < 200000; i++) {
      ClassLoader classLoader = new TargetLoader(Target.class.getName(), TargetLoader.getClassDataAsBytes(Target.class));
      classLoader.loadClass(Target.class.getName());
      long used = permGenPool.getUsage().getUsed();
      if (used < max) {
        System.out.println("Deallocated");
      }
      max = used;
    }
  }

  private static MemoryPoolMXBean getPermGenPoolMXBean() {
    for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
      if (pool.getName().contains("Perm Gen")) {
        return pool;
      }
    }
    throw new IllegalStateException();
  }

}