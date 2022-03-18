import utilities.JobFactory
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.error.YAMLException

def environ_vars = getBinding().getVariables()
String workspacePath = SEED_JOB.lastBuild.checkouts[0].workspace
Map yamlVars = [:]

try {
  out.println('Parsing job YAML file')
  String commonConfigContents = readFileFromWorkspace("jobs/config/jobs.yaml")
  Yaml yaml = new Yaml()
  yamlVars = yaml.load(commonConfigContents)
  out.println('Successfully parsed job YAML file')

} catch (YAMLException e) {
    throw new IllegalArgumentException("Unable to parse ${commonVarsFilePath}: ${e.message}")
}

yamlVars.SEED_JOBS.eachWithIndex{ job_map, index->
  JobFactory.seedJob(job_map, this)
}

yamlVars.PIPELINES.eachWithIndex{ job_map, index->
  JobFactory.buildJob(job_map, this)
}
