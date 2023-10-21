plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()

    // Paper 
    maven("https://repo.papermc.io/repository/maven-public/")

    // Item NBT
    maven("https://repo.codemc.io/repository/maven-public/")

}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
    
    // PaperMC
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    // External
    implementation(group = "de.tr7zw", name = "item-nbt-api-plugin", version = "2.12.0")
}

application {
    // Define the main class for the application.
    mainClass.set("next.App")
}

val myJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().output)
    archiveBaseName.set("SkillScheme") // Replace with your desired JAR name
    destinationDirectory.set(file("C:\\liam\\git\\skill-scheme\\server\\plugins")) // Set the output directory to 'C:/out/'
}

tasks.build {
    dependsOn(myJar)
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}