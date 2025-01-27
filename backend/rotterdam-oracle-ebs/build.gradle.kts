/*
 * Copyright 2015-2025 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("org.openapi.generator") version "7.10.0"
}

dockerCompose {
    setProjectName("rotterdam-oracle-ebs")
    isRequiredBy(project.tasks.integrationTesting)

    tasks.integrationTesting {
        useComposeFiles.addAll("$rootDir/docker-resources/docker-compose-base-test.yml", "docker-compose-override.yml")
    }
}

dependencies {
    implementation("com.ritense.valtimo:core")
    implementation("com.ritense.valtimo:plugin-valtimo")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("io.github.microutils:kotlin-logging")

    // Testing
    testImplementation("com.ritense.valtimo:document")
    testImplementation("com.ritense.valtimo:local-resource")
    testImplementation("com.ritense.valtimo:process-document")
    testImplementation("com.ritense.valtimo:test-utils-common")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.mockito:mockito-core")
    testImplementation("org.hamcrest:hamcrest-library")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation("org.postgresql:postgresql")

    testImplementation("com.squareup.okhttp3:mockwebserver")
    testImplementation("com.squareup.okhttp3:okhttp")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

}

apply(from = "gradle/publishing.gradle")

openApiGenerate {
    inputSpec.set("$rootDir/backend/rotterdam-oracle-ebs/src/main/resources/verkoopfactuur_journaalpost.yaml")
    generatorName.set("kotlin")
    outputDir.set("${getLayout().buildDirectory.get()}/generated")
    apiPackage.set("com.rotterdam.oracle-ebs.api")
    invokerPackage.set("com.rotterdam.oracle-ebs.invoker")
    modelPackage.set("com.rotterdam.oracle-ebs.model")
}

sourceSets {
    main {
        java {
            srcDir("${getLayout().buildDirectory.get()}/generated/src/main")
        }
    }
}

tasks.named("compileKotlin") {
    dependsOn(
        "openApiGenerate",
    )
}

tasks.named("sourcesJar") {
    dependsOn(
        "openApiGenerate",
    )
}
