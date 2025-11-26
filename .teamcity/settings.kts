import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.activeStorage
import jetbrains.buildServer.configs.kotlin.projectFeatures.awsConnection
import jetbrains.buildServer.configs.kotlin.projectFeatures.s3Storage

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.11"

project {

    buildType(Build2)

    features {
        s3Storage {
            id = "PROJECT_EXT_2"
            storageName = "fff"
            bucketName = "chubatovawest1"
            bucketPrefix = "dfdfdfvba"
            forceVirtualHostAddressing = true
            awsEnvironment = default {
                awsRegionName = "eu-west-1"
            }
            connectionId = "Project2_AmazonWebServicesAws"
        }
        s3Storage {
            id = "PROJECT_EXT_20"
            storageName = "fffsdsds"
            bucketName = "chubatovawest1"
            bucketPrefix = "dfdfdfvbasdsd"
            forceVirtualHostAddressing = true
            connectionId = "Project2_AmazonWebServicesAws"
        }
        activeStorage {
            id = "PROJECT_EXT_5"
            activeStorageID = "PROJECT_EXT_20"
        }
        awsConnection {
            id = "Project2_AmazonWebServicesAws"
            name = "Amazon Web Services (AWS)"
            regionName = "eu-west-3"
            credentialsType = static {
                accessKeyId = "AKIA5JH2VERVDN3XW5GF"
                secretAccessKey = "credentialsJSON:8e64e60c-7b8d-4090-b6f6-a9d146f3dd06"
            }
            allowInSubProjects = true
            allowInBuilds = true
            stsEndpoint = "https://sts.eu-west-3.amazonaws.com"
        }
    }
}

object Build2 : BuildType({
    name = "Build2"

    artifactRules = "a.txt"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "echo %build.number% > a.txt"
        }
    }
})
