# Mock Service
* [Introduction](#introduction)
* [Prerequisites](#prerequisites)
    * [Development Tools](#development-tools)
    * [Additional Tools](#additional-tools)
* [Build Project](#build-project)
* [Run Project](#run-project)
    * [Run Microservice using JAR](#run-microservice-using-JAR)
    * [Run Microservice using Docker Image](#run-microservice-using-docker-image)
* [Deployment](#deployment)
    * [On-Prem Deployment](#on-prem-deployment)
        * [Pre-Requisites](#pre-requisites)
        * [Deployment Steps](#deployment-steps)
    * [Azure Deployment](#azure-deployment)
        * [Pre-Requisites](#pre-requisite)
        * [Setting Azure Kubernetes Service](#setting-up-azure-kubernetes-service)
        * [Setting up API Gateway](#setting-up-api-gateway)
* [Useful Docker Commands](#useful-docker-commands)


## Introduction
This service provides collection of APIs to mock BPX APIs related activities. Use of the APIs provided as part of this service to link biller, retrieve the list of linked billers to a particular consumer, remove linking to a biller from consumer etc.

## Services

|Purpose|**A**PI| **Description** | Exposed To |
|--------|--------|--------|--------|
| Payment Link | /zapp/bpx/v1/payment-links | This API is used to link a particular biller with consumer’s account. | BPA |
| Delete Links | /zapp/bpx/v1/payment-links/{scheme_tx_id} | This API is used to delete the linking of a biller with consumer’s account | BPA |
| Document Retrieval | /zapp/bpx/v1/payment-requests/{payment_request_id}/docs/{doc_id}/uris | This API will be used to get bills list for given debtor information | BPA |
| Payment Confirmation Advice | /zapp/bpx/v1/payment-requests/{scheme_tx_id}/advices | This API is used to confirm link between biller and consumer’s account | BPA |

## Prerequisites

### Development Tools


- [ ] **Open JDK** downstream distribution from Azul - [Azul Zulu-openjdk 11](https://www.azul.com/downloads/zulu-community/?architecture=x86-64-bit&package=jdk/) is used as a JDK.

- [ ] [Springboot-2.6.3](https://spring.io/blog/2021/03/18/spring-boot-2-6-3-available-now) is an open source Java-based framework used to create a micro Service. It is used to build stand-alone and production ready spring applications.

- [ ] [Maven 3.6](https://maven.apache.org/download.cgi) is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information.

- [ ] [Docker](https://www.docker.com/) daemon is used to build Docker image. For building docker image, [spotify/dockerfile-maven](https://github.com/spotify/dockerfile-maven) is integrated in the pom.xml.  Docker Engine for the respective platform can be downloaded from [here](https://docs.docker.com/engine/install/)

### Additional Tools

The following tools are optional. if there are some other alternative tools are available for disposal, developer is free to choose them. The project doesn't have any dependency on the tools mentioned below.

| Purpose                 | Description                                        | Tool                                                         |
| ----------------------- | -------------------------------------------------- | ------------------------------------------------------------ |
| Code Quality & Security | For performing static application security testing | [SonarQube](https://www.sonarqube.org/)                      |
| Artifacts Management    | For uploading and downloading dependencies         | [JFrog Artifactory](https://jfrog.com/artifactory/#:~:text=As%20the%20world%27s%20first%20universal,Control) |
| Docker Registry         | For managing the Docker images                     | [JFrog Docker Registry](https://www.jfrog.com/confluence/display/JFROG/Docker+Registry) |

Following section is applicable if you choose to use the above mentioned tools in the project.

Make sure that [SonarQube](https://www.sonarqube.org/) and [JFrog Artifactory](https://jfrog.com/artifactory/#:~:text=As%20the%20world%27s%20first%20universal,Control) configuration is not committed in the projected. It should be externalized through profile configuration available in maven using **settings.xml**. The location of **settings.xml** is `~/.m2/settings.xml`.

To configure the values for above mentioned file, you may need to get the information from IT staff who have provisioned the above tools

| Key                  | Value                                                       |
| -------------------- | ----------------------------------------------------------- |
| artifactory-url      | Artifactory Server used for Maven Dependency Resolution     |
| artifactory-username | Username to be used to authenticate with Artifactory Server |
| artifactory-password | Password to be used to authenticate with Artifactory Server |
| sonarqube-url        | Sonarqube Server URL used for Static Code Analysis          |
| sonar-login          | Sonarqube Login Token Created by the user                   |

- Sample settings looks like the one shown below.

```xml

<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd">
  <servers>
     <server>
        <username><!-- artifactory-username --></username>
        <password><!-- artifactory-password --></password>
        <id>central</id>
     </server>
     <server>
        <username><!-- artifactory-username --></username>
        <password><!-- artifactory-password --></password>
        <id>snapshots</id>
     </server>
  </servers>
  <profiles>
     <profile>
        <id>sonar</id>
        <activation>
           <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
           <!-- Optional URL to server. Default value is http://localhost:9000 -->
           <sonar.host.url>https://<sonarqube-url>.com</sonar.host.url>
           <sonar.login><!-- create token for yourself and paste the token here --></sonar.login>
        </properties>
     </profile>
     <profile>
        <repositories>
           <repository>
              <snapshots>
                 <enabled>false</enabled>
              </snapshots>
              <id>central</id>
              <name>mavenrepo</name>
              <url>https://<artifactory-url>/artifactory/mavenrepo</url>
           </repository>
           <repository>
              <snapshots />
              <id>snapshots</id>
              <name>mavenrepo</name>
              <url>https://<artifactory-url>/artifactory/mavenrepo</url>
           </repository>
        </repositories>
        <pluginRepositories>
           <pluginRepository>
              <snapshots>
                 <enabled>false</enabled>
              </snapshots>
              <id>central</id>
              <name>mavenrepo</name>
              <url>https://<artifactory-url>/artifactory/mavenrepo</url>
           </pluginRepository>
           <pluginRepository>
              <snapshots />
              <id>snapshots</id>
              <name>mavenrepo</name>
              <url>https://<artifactory-url>/artifactory/mavenrepo</url>
           </pluginRepository>
        </pluginRepositories>
        <id>artifactory</id>
     </profile>
  </profiles>
  <activeProfiles>
     <activeProfile>artifactory</activeProfile>
  </activeProfiles>
</settings>
```
## Build Project
- Some of the most important phases in the *default* build lifecycle:
    - **validate**: check if all information necessary for the build is available
    - **compile**: compile the source code
    - **test-compile**: compile the test source code
    - **test**: run unit tests
    - **package**: package compiled source code into the distributable format (jar, war, …)

- Run below maven commands from ***project_root*** directory for various tasks.

    * **clean project**: This will clean the **Target** directory (aka output folder).

      ```
      mvnw clean
      ```

    * **clean & compile project**: This will clean the **Target** directory and only compile the project

      ```
      mvn clean compile
      ```

    * **clean & build project**: This will perform the **clean** task followed by **build** process. The will  clean,build, run test-cases, calculate code coverage and generate the artifact file in the **target** directory.

      ```
      mvnw clean package
      ```

    * **Skip Test Case during build** This will perform the **clean** task followed by **build** process; skips the test cases execution and code coverage,

      ```
      mvn clean package -Dmaven.test.skip=true
      ```

    * **Create docker image:**  This will perform the **clean** task followed by **build** process. Then it creates **docker** Image. Please make sure that the Docker Service is running locally before running this goal.

      ```
      mvnw clean package dockerfile:build
      ```

## Run Project
### Run Microservice using JAR

If you need to perform deployment on local system, it can be done by running jar directly or using a Docker image. Use the below mentioned command to run the microservice directly using jar file.

```
java -jar <app-name>.jar &
```

Note the **&** at the end of the command, this is to run the process in background

By default, the server runs on port ***8080***, we can change that by passing the port number as below (make sure to kill the above process if it is already running)

```
java -Dserver.port=9023 -jar <app-name>.jar &
```

### Run Microservice using Docker Image
Once the **Docker** image is available, we can run the Docker container by using the following command.

For **Windows**, run the following command

```bash
winpty docker run -p 8081:8080 -it <image-name>
```

For **Linux**, run the following command

```bash
docker run -p 8081:8080 -it <image-name>
```
## Deployment

* ### On-Prem Deployment

  #### Pre-Requisites:

    - [ ] **Docker Daemon Engine** is required to run the docker image generated as a part of the output. Docker Engine can be download from [here](https://docs.docker.com/engine/install/).

  #### Deployment Steps

    1. Build the Docker Image as mentioned in [Build Project](#build-project) section.

    2. In order to start the microservice from the docker image generated as part of step 1, run the following command:

       ```
       docker run -p 8081:8084 -it <image-name>
       ```

    3. Set environment variables mentioned in the section[Environment variables](#environment-variables)  Please refer [Set environment variables]([docker run | Docker Documentation](https://docs.docker.com/engine/reference/commandline/run/#set-environment-variables--e---env---env-file)) on how to pass environment variables to **Docker** Image for further details.

  ### Azure Deployment

  #### Pre-Requisite:

    * **Azure RBAC:** Make sure you have the necessary role assignments assigned to you by your administrator for the various resource creation in Azure.

  #### Setting Up Azure Kubernetes Service

    * **Provision Cluster**: Please refer this [guide](https://docs.microsoft.com/en-us/azure/aks/kubernetes-walkthrough-portal) to provision a Azure Kubernetes Cluster.

    * **Cluster Details**:  Make sure to specify the region same as that of resource group in which AKS service is being created.

    * **Primary Node Pool**:

        * Select a VM Node size for the **AKS** nodes.
        * Select the number of nodes to deploy into the cluster. For this QuickStart, set Node count to 1. Node count can be adjusted after the cluster has been deployed. On the Node pools page, keep the default options. At the bottom of the screen, click Next: Authentication.
        * On the Authentication page, configure the following options:
            * Create a new service principal by leaving the Service Principal field with (new) default service principal. Or you can choose Configure service principal to use an existing one. If you use an existing one, you will need to provide the SPN client ID and secret.
            * Enable the option for Kubernetes role-based access control (RBAC). This will provide more fine-grained control over access to the Kubernetes resources deployed in your AKS cluster.

    * Provision **Networking, RBAC, Container Registry** access etc.

    * Configure **inbound IP address for Proxy**

    * Configure **outbound IP address through NAT**

    * **Container Storage Interface(CSI) Driver (Optional):**

        * CSI Drivers enable secrets in case the secrets are being fetched from Azure Key Vault.

        * Azure Key Vault provider for Secrets Store CSI driver allows you to get secret contents stored in Azure Key Vault instance and use the Secrets Store CSI driver interface to mount them into Kubernetes pods. By default, Basic networking is used, and Azure Monitor for containers is enabled.

        * **Installing the Chart**

            * This chart installs the secrets-store-csi-driver and the Azure Key Vault provider for the driver

              ```sh
              $ helm repo add csi-secrets-store-provider-azure https://raw.githubusercontent.com/Azure/secrets-store-csi-driver-provider-azure/master/charts

              $ helm install csi-secrets-store-provider-azure/csi-secrets-store-provider-azure --generate-name
              ```

            * Please refer the below link to install the Secrets Store CSI driver
              https://github.com/Azure/secrets-store-csi-driver-provider-azure/blob/master/charts/csi-
              secrets-store-provider-azure/README.md

            * Use the following detailed guide for step by step instructions and configuration details.
              https://github.com/Azure/secrets-store-csi-driver-provider-azure

            * In addition, if you are using Secrets Store CSI Driver and the Azure Key Vault Provider in a cluster with pod security policy enabled, review and create the following policy that enables the spec required for Secrets Store CSI Driver and the Azure Key Vault Provider to work:

              ```sh
              kubectl apply -f https://raw.githubusercontent.com/Azure/secrets-store-csi-driver-provider-azure/master/deployment/pod-security-policy.yaml
              ```

  #### Setting up API Gateway

  API gateway acts as a reverse proxy to accept all application programming interface (API) calls, aggregate the various services required to fulfill them, and return the appropriate result.

  Following the given set of instructions if you intend to use **Kong** as as **Ingress Controller** for Kubernetes [Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/).

    - Configure [plugins](https://docs.konghq.com/hub/), health checking, load balancing and more in Kong for Kubernetes Services, all using Custom Resource Definitions(CRDs) and Kubernetes-native tooling.

  **Provision Kong**

    * Deploy Kong Ingress Controller using Helm Chart:

      ```sh
        $ helm repo add kong https://charts.konghq.com
        $ helm repo update

        # Helm 2
        $ helm install kong/kong

        # Helm 3
        $ helm install kong/kong --generate-name --set ingressController.installCRDs=false

        $ kubectl get services -n kong
        NAME	TYPE	CLUSTER-IP	EXTERNAL-IP PORT(S)	AGE
        kong-proxy LoadBalancer 10.63.250.199 203.0.113.42 80:31929/TCP,443:31408/TCP 57d
      ```

    * Custom Resource Definitions (CRDs) are handled differently in Helm 2 vs Helm 3.

        * **Helm 2**
          If you want CRDs to be installed, make sure **ingressController.installCRDs** is set to **true** (the default value). Set this value to false to skip installing CRDs.
        * **Helm 3**
          Make sure **ingressController.installCRDs** is set to false, note that the default is true. You can do so either by passing in a custom values.yaml (-f when running helm) or by passing **--set ingressController.installCRDs=false** at the command line.
          • If you do not set this value to false, the helm chart will not install correctly.
          • Use helm CLI flag --skip-crds with helm install if you want to skip CRD creation while creating a release.

      **Setting Up Routes / Services / Authentication and Key Configuration**

        * Kong’s Admin API can be used to provision routes, services and various other metadata required for publishing the services and performing security hardening using plug-ins.

            * The required scripts to create above information is already mentioned in the open-api specification for the different URI's associated with the microservice.
        * This can be used to perform API management for a given environment.

            * Konga is more than just another GUI to Kong Admin API. It can be used to configure the above information in kong using GUI.

## Useful Docker Commands

List docker images

```bash
$ docker images
```

This will print output like the one shown below.
```
$ docker images
REPOSITORY                                 TAG          IMAGE ID            CREATED             SIZE
billpay-mock-service                      latest       fa256c6a5e56        52 minutes ago      643MB
```

Remove Docker image
```bash
docker rmi <image-name>
```

Run Docker image with name 'billpay-service'
```bash
$ winpty docker run -p 8080:8080 -it billpay-service
```

Tagging Docker image
```bash
docker tag billpay-service gcr.io/mastercard/billpay-service:0.1
```

* **Skip Test Case and Sonar analysis during build** This will perform the **clean** task followed by **build** process; skips the test cases execution and code coverage,

   ```
   mvn clean package -Dmaven.test.skip=true -Dsonar.skip=true
   ```