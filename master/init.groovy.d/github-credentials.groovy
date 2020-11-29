import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.Secret
import jenkins.model.Jenkins

def jenkinsKeyUsernameWithPasswordParameters = [
  description:  'Github login credentials',
  id:           'github',
  secret:       'c7617ea71e62e0eabbb0023126b1018c543e8db8',
  userName:     'vcs-cloudstart-io'
]

Jenkins jenkins = Jenkins.getInstance()

def domain = Domain.global()
def store = jenkins.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

def jenkinsKeyUsernameWithPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  jenkinsKeyUsernameWithPasswordParameters.id,
  jenkinsKeyUsernameWithPasswordParameters.description,
  jenkinsKeyUsernameWithPasswordParameters.userName,
  jenkinsKeyUsernameWithPasswordParameters.secret
)

store.addCredentials(domain, jenkinsKeyUsernameWithPassword)

jenkins.save()
