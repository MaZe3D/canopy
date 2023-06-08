plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":filter_interface"))
    implementation(project(":default_filters"))

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    // Add Jackson for generating a tree
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.reflections:reflections:0.9.11")
}

application {
    // Define the main class for the application.
    mainClass.set("net.canopy.app.App")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

// configure javadoc to create single documentation for all subprojects: app, filter_interface, default_filters
gradle.projectsEvaluated {
    tasks.withType<Javadoc> {
        title = "Canopy Documentation"
        source(sourceSets["main"].allJava)
        source(project(":filter_interface").the<SourceSetContainer>()["main"].allJava)
        source(project(":default_filters").the<SourceSetContainer>()["main"].allJava)
        classpath = sourceSets["main"].compileClasspath
        classpath += project(":filter_interface").the<SourceSetContainer>()["main"].compileClasspath
        classpath += project(":default_filters").the<SourceSetContainer>()["main"].compileClasspath
    }
}
