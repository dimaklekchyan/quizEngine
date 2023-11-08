plugins {
    kotlin("jvm")
}

dependencies {
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val coroutinesVersion: String by project
    val janinoVersion: String by project
    val datetimeVersion: String by project

    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":lib-logging-common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    // logback
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation(kotlin("test-junit"))
}