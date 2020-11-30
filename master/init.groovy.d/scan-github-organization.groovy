import org.kohsuke.github.*

String folder = 'jobs'                                              // folder to put your jobs into
String githubLogin = 'vcs-cloudstart-io'                            // github user login
String githubPassword = 'c7617ea71e62e0eabbb0023126b1018c543e8db8'  // github user personal access token
String githubOrganization = 'cloudstart-io                     // github organization
String scanCredentials = 'vcs-cloudstart-io'                       // credentials for scanning repository
String checkoutCredentials = 'c7617ea71e62e0eabbb0023126b1018c543e8db8'               // credentials for repository checkout
String includes = '*'                                                // What branches to include
String excludes = ''                                                // What branches to exclude
String numToKeep = '5'                                              // Number of recent builds to keep. -1 for all of them
String daysToKeep = '10'                                            // Number of days to keep recent builds. -1 for forever

// Please note that these are boolean, but you have to stringify them
String forkPr = 'true|false'                                        // Build fork PRs separately
String forkPrMerge = 'true|false'                                   // Build fork PRs after being merged to target branch
String originBranch = 'true|false'                                  // Build origin branches
String originBranchWithPr = 'true|false'                            // Build origin branches files as PRs
String originPrHead = 'true|false'                                  // Build origin PRs (unmerged head).
String originPrMerge = 'true|false'                                 // Build origin PRs (merged with base branch).

// Not much to edit below, but don't trust anyone and read it

String folderSource = '''
folder(':folder:') {
    description('Repository jobs for :organization: Github Organization')
}
'''
String dslSource = '''
multibranchPipelineJob(':folder:/:jobName:') {
    description(':description:')
    branchSources {
        github {
            repoOwner(':organization:')
            repository(':repository:')
            scanCredentialsId(':scanCredentials:')
            checkoutCredentialsId(':checkoutCredentials:')
            buildForkPRHead(:forkPr:)
            // Build fork PRs (merged with base branch).
            buildForkPRMerge(:forkPrMerge:)
            // Build origin branches.
            buildOriginBranch(:originBranch:)
            // Build origin branches also filed as PRs.
            buildOriginBranchWithPR(:originBranchWithPr:)
            // Build origin PRs (unmerged head).
            buildOriginPRHead(:originPrHead:)
            // Build origin PRs (merged with base branch).
            buildOriginPRMerge(:originPrMerge:)
            includes(':includes:')
            excludes(':excludes:')
        }
    }
    orphanedItemStrategy {
        discardOldItems {
            numToKeep(:numToKeep:)
            daysToKeep(:daysToKeep:)
        }
    }
}
'''

List dslScripts = []
int rateLimitBefore = 0

node('master') {
    stage('Scan') {
        GitHub github = GitHub.connectUsingPassword(githubLogin, githubPassword)
        rateLimitBefore = github.getRateLimit().remaining
        echo "API requests before: ${rateLimitBefore}"

        // you can say that using .each({ repo -> .... }) would make sense
        // I would say that too.
        // But Jenkins does not agree with us
        // so @see: https://issues.jenkins-ci.org/browse/JENKINS-26481
        List repositories = github.getOrganization(githubOrganization).listRepositories(100).asList()

        for (int i = 0; i < repositories.size(); i++) {
            def repo = repositories[i]
            echo "Scanning repository ${repo.getFullName()}"

            String name = repo.getName()

            // this is called Elvis Operator
            String description = repo.getDescription() ?: ''

            // Prepare the template
            String dslScript = dslSource.replaceAll(':folder:', folder)
                .replaceAll(':organization:', githubOrganization)
                .replaceAll(':repository:', name)
                .replaceAll(':jobName:', name)
                .replaceAll(':description:', description)
                .replaceAll(':scanCredentials:', scanCredentials)
                .replaceAll(':checkoutCredentials:', checkoutCredentials)
                .replaceAll(':includes:', includes)
                .replaceAll(':excludes:', excludes)
                .replaceAll(':numToKeep:', numToKeep)
                .replaceAll(':daysToKeep:', daysToKeep)
                .replaceAll(':forkPr:', forkPr)
                .replaceAll(':forkPrMerge:', forkPrMerge)
                .replaceAll(':originBranch:', originBranch)
                .replaceAll(':originBranchWithPr:', originBranchWithPr)
                .replaceAll(':originPrHead:', originPrHead)
                .replaceAll(':originPrMerge:', originPrMerge)

            dslScripts.add dslScript
        }
    }
    stage('Create jobs') {
        // Anything generated?
        if (dslScripts.size() > 0) {
            // Add folder dsl
            String folderDsl = folderSource.replaceAll(':folder:', folder)
                .replaceAll(':organization:', githubOrganization)
            // Put it all together
            String dslOutput =  folderDsl + dslScripts.join('\n')
            // Write the dsl to generate the jobs
            // If you would like to write the file in the scope where github, repositories and repo variables are present
            // you will get stuck with https://issues.jenkins-ci.org/browse/JENKINS-26481 again.
            writeFile(file: 'sourceOfGeneratedJobs.groovy', text: dslOutput)
            // Generate the jobs
            jobDsl failOnMissingPlugin: true,  unstableOnDeprecation: true, targets: 'sourceOfGeneratedJobs.groovy'
            // Archive the artifacts
            archiveArtifacts(artifacts: 'sourceOfGeneratedJobs.groovy', fingerprint: true, onlyIfSuccessful: true)
        } else {
            echo "Well-well-well, nothing has been generated for you. Exactly 0 repositories were found in ${githubOrganization} Github organization."
        }

        int rateLimitAfter = GitHub.connectUsingPassword(githubLogin, githubPassword).getRateLimit().remaining
        echo "API requests after: ${rateLimitAfter}"
        int consumed = rateLimitBefore - rateLimitAfter
        echo "API requests consumed: ${consumed}"
    }
}
