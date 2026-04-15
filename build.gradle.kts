plugins {
    id("java")
    id("application")
}

group = "com.programacion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    val javafxVersion = "21"
    val platform = "win"

    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.programacion.AppCriptografia")
}

tasks.named<JavaExec>("run") {
    doFirst {
        jvmArgs = listOf(
            "--module-path", classpath.asPath,
            "--add-modules", "javafx.controls,javafx.fxml"
        )
    }
}