package com.zshnb.projectgenerator.generator.io

import com.zshnb.projectgenerator.web.config.ProjectConfig
import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Component
import java.io.*
import java.util.zip.*

@Component
class ZipFileWriter(private val projectConfig: ProjectConfig) {
    fun createZipFile(zipFileName: String, rootDirName: String) {
        val zipFile = File(projectConfig.tempDir, zipFileName)
		zipFile.parentFile.mkdir()
        val fos = FileOutputStream(zipFile)
        val zos = ZipOutputStream(fos)

        val rootDir = File(rootDirName)
		compress(rootDir, zos, rootDir.name)
		zos.close()
    }

	@Throws(Exception::class)
	fun compress(sourceFile: File,
				 zos: ZipOutputStream,
				 name: String) {
		if (sourceFile.isFile) {
			zos.putNextEntry(ZipEntry(name))
			zos.write(FileUtils.readFileToByteArray(sourceFile))
			zos.closeEntry()
		} else {
			val listFiles = sourceFile.listFiles()
			if (listFiles == null || listFiles.isEmpty()) {
				zos.putNextEntry(ZipEntry("$name/"))
				zos.closeEntry()
			} else {
				for (file in listFiles) {
					compress(file, zos, "$name/${file.name}")
				}
			}
		}
	}
}