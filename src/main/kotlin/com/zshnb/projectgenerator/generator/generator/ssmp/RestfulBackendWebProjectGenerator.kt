package com.zshnb.projectgenerator.generator.generator.ssmp

//@Component
//class RestfulBackendWebProjectGenerator(private val backendParser: BackendParser,
//                                        private val ioUtil: IOUtil,
//                                        private val pathConfig: PathConfig,
//                                        private val projectConfig: ProjectConfig,
//                                        private val configuration: Configuration) :
//    BaseWebProjectGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
//    override fun generateProject(json: String): WebProject {
//        val project = super.generateProject(json)
//        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESTFUL_CONTROLLER_TEMPLATE)
//        val entities = backendParser.parseEntities(project.tables)
//        entities.forEach {
//            ioUtil.writeTemplate(controllerTemplate, it,
//                "${pathConfig.controllerDir(project.config)}/${it.name.capitalize()}Controller.java")
//        }
//        return project
//    }
//}
