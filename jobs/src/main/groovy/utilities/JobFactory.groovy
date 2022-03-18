package utilities

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.WorkflowJob

class JobFactory {
  static WorkflowJob seedJob(Map job_map, DslFactory factory) {
    factory.pipelineJob(job_map.name) {
      description job_map.name
      definition {
        cpsScm {
          lightweight(true)
          scm {
            git {
              remote {
                url job_map.scm_url
                credentials job_map.scm_credentials
              }
              branch job_map.scm_branch
            }
            scriptPath job_map.pipeline_path
          }
        }
      }
    }
  }
  // build job class
  static WorkflowJob buildJob(Map job_map, DslFactory factory) {
    factory.pipelineJob(job_map.name) {
      description job_map.description
      if (job_map.containsKey("parameters")) {
        parameters getParams(job_map.parameters)
      }
      if (job_map.containsKey("cron_trigger")) {
        triggers {
          cron(job_map.cron_trigger)
        }
      }
      if (job_map.containsKey("log_rotation")) {
        logRotator {
          artifactDaysToKeep(job_map.log_rotation.artifact_days_to_keep)
          artifactNumToKeep(job_map.log_rotation.artifact_num_to_Keep)
          daysToKeep(job_map.log_rotation.days_to_keep)
          numToKeep(job_map.log_rotation.num_to_keep)
        }
      }
      throttleConcurrentBuilds {
        maxTotal(1)
      }
      definition {
        cpsScm {
          lightweight(true)
          scm {
            git {
              remote {
                url job_map.scm_url
                credentials job_map.scm_credentials
              }
              branch job_map.scm_branch
            }
          scriptPath job_map.pipeline_path
          }
        }
      }
    }
  }

  // Get parameters
  public static getParams(List params) {
    def job_params = {
      params.eachWithIndex{ param, index->
        switch(param.type) {
          case "stringParam":
            stringParam(param.name, param.default_value, param.description)
            break
          case "booleanParam":
            booleanParam(param.name, param.default_value, param.description)
            break
          case "choiceParam":
            choiceParam(param.name, param.options, param.description)
            break
        }
      }
    }
    return job_params
  }
}
