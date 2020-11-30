import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def jenkinsParameters = [
  email:  'Jay Amorin <jay.amorin@gmail.com>',
  url:    'http://jenkins.jayamorin.com/'
]

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()

jenkinsLocationConfiguration.setUrl(jenkinsParameters.url)
jenkinsLocationConfiguration.setAdminAddress(jenkinsParameters.email)
jenkinsLocationConfiguration.save()
