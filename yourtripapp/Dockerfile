FROM eclipse-temurin:21
#FROM registry.gitlab.com/armdev/dockers/zulujdk:21
#FROM ghcr.io/graalvm/jdk-community:21
COPY target/yourtripadvisor.jar /opt/yourtripadvisor.jar

#ENTRYPOINT ["java", "--add-opens", "java.base/java.time.zone=ALL-UNNAMED", "--add-opens", "java.base/java.time=ALL-UNNAMED", \
#            "--add-opens", "java.base/java.math=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
#            "--add-opens", "java.management/sun.management=ALL-UNNAMED", \
#            "--add-opens", "java.base/java.time.LocalDateTime=ALL-UNNAMED", \
#            "-XX:+UseZGC", "-XX:+ZGenerational", "-Xss3m", "-XX:MinHeapSize=1G", "-XX:InitialHeapSize=1G", "-XX:MaxHeapSize=4G", \
#            "-XX:TieredStopAtLevel=1", "-XX:CICompilerCount=1", \
#            "-XX:ParallelGCThreads=100", "-XX:ConcGCThreads=30", "--enable-preview", \
#            "-XX:SoftRefLRUPolicyMSPerMB=0","-XX:ZCollectionInterval=30", "-XX:ZFragmentationLimit=10", \
#            "-XX:MaxGCPauseMillis=5", "-XX:+UseCompressedClassPointers", "-XX:-UseCompressedOops", "-XX:ZUncommitDelay=30", \
#            "-XX:InidtiatingHeapOccupancyPercent=35", "-XX:G1HeapRegionSize=16M", \
#            "-XX:MinMetaspaceFreeRatio=50", "-XX:MaxMetaspaceFreeRatio=100",  \
#            "-XX:+HeapDumpOnOutOfMemoryError", "-Dsun.net.inetaddr.ttl=60", "-Dsun.net.client.defaultConnectTimeout=10000", "-Dsun.net.client.defaultReadTimeout=30000", \
#            "-XX:+UnlockExperimentalVMOptions", "-XshowSettings:vm" ,"-jar","/opt/yourtripadvisor.jar"]

EXPOSE 8081