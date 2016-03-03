package org.scalacourse

import com.synergygb.zordon.core.ServiceBoot
import com.synergygb.zordon.gen.routes.ApplicationRoutesConsolidated
import com.synergygb.zordon.gen.models.ProjectInfo
import spray.http.StatusCodes._

object Boot extends App with ServiceBoot with ApplicationRoutesConsolidated {

  import Context.keyValueStore
  import Context.executionContext
  
  implicit def context = Context
  
  protected def apiResourceClass = getClass
  
  def dataContext = Context
  
  override def handlePutCreaProyecto(project: ProjectInfo)() = {
    val key = project.nombreProyecto
    onSuccess(keyValueStore.write(key, project)) { x =>
      complete(Created, "Creacion exitosa del projecto")
    }
  }

  override def handlePostEditarProyecto(project: ProjectInfo)() = {
    val key = project.nombreProyecto
    onSuccess(keyValueStore.read[ProjectInfo](key)) { maybeProj  =>
      val proj = maybeProj.getOrElse(ProjectInfo(nombreProyecto="error",liderProyecto=None,fechaInicioProyecto=None,fechaFinProyecto=None,tareas=None))
      if(proj.nombreProyecto == key){
        onSuccess(keyValueStore.write(key, project)) { x =>
          complete(Created, "Actualizaciom exitosa del projecto")
        }
      }else complete(NotFound)
    }
  }
}
