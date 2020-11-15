package com.zshnb.projectgenerator.generator.io

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


@Component
class ZipFileWriter {
    fun createZipFile(zipFileName: String) {
        val zipFile = File(zipFileName)
        val fos = FileOutputStream(zipFile)
        val zos = ZipOutputStream(fos)

        val rootDir = File("temp")
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