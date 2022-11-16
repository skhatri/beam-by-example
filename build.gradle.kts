plugins {
  id("java")
  id("idea")
}

val beamVersion="2.42.0"

dependencies {
  implementation("org.slf4j:slf4j-api:1.7.36")
  implementation("org.slf4j:log4j-over-slf4j:1.7.36")
  implementation("ch.qos.logback:logback-classic:1.4.4")

  implementation("io.reactivex.rxjava3:rxjava:3.1.5")
  implementation("io.projectreactor.addons:reactor-adapter:3.4.8")
  
  listOf("beam-sdks-java-io-parquet", "beam-sdks-java-core", "beam-sdks-java-io-hadoop-common", 
   "beam-sdks-java-io-hadoop-file-system",
   "beam-sdks-java-extensions-json-jackson",
   "beam-runners-spark-3", "beam-runners-core-java", "beam-runners-direct-java").forEach {module ->
    implementation("org.apache.beam:$module:$beamVersion") 
  }
  implementation("org.apache.hadoop:hadoop-client:2.10.1")

  implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.4")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")

}

repositories {
  mavenCentral()
}

task("runApp", JavaExec::class) {
  main = "com.github.beam.tasks.WordCountApp"
  classpath = sourceSets["main"].runtimeClasspath
  jvmArgs = listOf("-Xms512m", "-Xmx512m")
}

