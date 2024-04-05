import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.approval
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.UntrustedBuildsSettings
import jetbrains.buildServer.configs.kotlin.projectFeatures.untrustedBuildsSettings
import jetbrains.buildServer.configs.kotlin.triggers.retryBuild
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs

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

version = "2024.03"

project {

    buildType(Dep)
    buildType(Buntrstd)

    features {
        untrustedBuildsSettings {
            id = "PROJECT_EXT_5"
            defaultAction = UntrustedBuildsSettings.DefaultAction.APPROVE
            enableLog = true
            approvalRules = "user:admin"
            manualRunsApproved = false
        }
    }
}

object Buntrstd : BuildType({
    name = "buntrstd"

    vcs {
        root(DslContext.settingsRoot)

        showDependenciesChanges = true
    }

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "exit 1"
        }
    }

    triggers {
        vcs {
            enabled = false
        }
        retryBuild {
            moveToTheQueueTop = true
        }
        schedule {
            schedulingPolicy = cron {
                minutes = "02"
                hours = "17"
            }
            triggerBuild = always()
            param("minute", "55")
            param("hour", "16")
        }
    }

    features {
        pullRequests {
            provider = github {
                authType = token {
                    token = "credentialsJSON:39d57c0f-ec6c-4f11-a56c-72ca76e32445"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
            }
        }
        approval {
            enabled = false
            approvalRules = "user:admin"
            timeout = 1
            manualRunsApproved = false
        }
    }
})

object Dep : BuildType({
    name = "dep"

    vcs {
        root(DslContext.settingsRoot)
    }

    features {
        pullRequests {
            provider = github {
                authType = token {
                    token = "credentialsJSON:39d57c0f-ec6c-4f11-a56c-72ca76e32445"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
            }
        }
    }
})
