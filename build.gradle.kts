plugins {
    application
}

repositories {
    mavenCentral()
    flatDir{
        dirs("local/bridge")
    }
}

dependencies {
    implementation("bridge:bridge-0.0.0-alpha.0.2.0")
    testImplementation("bridge:bridge-0.0.0-alpha.0.2.0")
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "org.example.Main"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

