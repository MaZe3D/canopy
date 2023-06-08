plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":filter_api"))

    // Add Jackson for generating a tree
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}
