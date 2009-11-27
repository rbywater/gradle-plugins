package com.smokejumperit.gradle

import org.gradle.api.*
import org.gradle.api.plugins.*

class EnvPlugin extends SjitPlugin {
  void use(Project project, ProjectPluginsContainer projectPluginsHandler) { 
    project.ant.property(environment:'env')
    def envs = [:] 
    project.ant.properties.each { k,v ->
      if(k.startsWith('env.')) {
        envs.put("$k".replaceFirst(/^env\./, ""), v)
      }
    }
    logger.info("Environment keys: ${envs.keySet() as SortedSet}")
    project.metaClass.env = Collections.unmodifiableSortedMap(envs as SortedMap)
    project.metaClass.env = { k -> 
      k = k?.toString()
      if(!envs.containsKey(k)) {
        throw new Exception("Could not find system environment variable: $k")
      }
      envs[k] 
    }
  }
}
