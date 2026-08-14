import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

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

version = "2026.1"

project {

    vcsRoot(HttpsGithubComChubatovaTigerGradleTestsRefsHeadsMain)

    buildType(Build2)
    buildType(Build)
}

object Build : BuildType({
    name = "Build1"

    buildNumberPattern = "${Build2.depParamRefs.buildNumber}"

    vcs {
        root(HttpsGithubComChubatovaTigerGradleTestsRefsHeadsMain, "+:src => .")
        root(DslContext.settingsRoot, "+:. => maven")
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "ls maven"
        }
    }

    dependencies {
        dependency(Build2) {
            snapshot {
            }

            artifacts {
                buildRule = lastFinished()
                artifactRules = "a.txt"
            }
        }
        retrySettings {
            maxAttempts = 2
            retryOnSameFailure = false
        }
    }
})

object Build2 : BuildType({
    name = "Build2"

    artifactRules = "a.txt"
    buildNumberPattern = "a-%build.counter%"

    vcs {
        root(HttpsGithubComChubatovaTigerGradleTestsRefsHeadsMain, "+:src => .")
        root(DslContext.settingsRoot, "+:. => maven")
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = """
                echo a > a.txt
                if (( RANDOM % 2 )); then
                  exit 1
                fi
            """.trimIndent()
        }
    }
})

object HttpsGithubComChubatovaTigerGradleTestsRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/GradleTests#refs/heads/main"
    url = "https://github.com/ChubatovaTiger/GradleTests"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})
