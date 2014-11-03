package org.codemucker.lang;

import java.io.File;
import java.io.IOException;


public class PathUtil {

    public static String toForwardSlashes(String path) {
        if (path == null) {
            return null;
        }
        return path.replace('\\', '/');
    }
    
    public static String filePathToClassPath(String path) {
        if (path.charAt(0) == '/') {
            return path.substring(1).replace('/', '.');
        } else {
            return path.replace('/', '.');
        }
    }
    
    public static String filePathToClassNameOrNull(String filePathWithExtension) {
        if (filePathWithExtension.endsWith(".java") || filePathWithExtension.endsWith(".class")) {
            String classPath = stripExtension(filePathWithExtension);
            String className = filePathToClassPath(classPath);
            return className;
        }
        return null;
    }

    public static String stripExtension(String path) {            
    	int dot = path.lastIndexOf('.');
    	if (dot != -1) {
    		return path.substring(0, dot);
    	}
    	return path;
    }

    /**
     * Create a new temporary directory within the given base dir, using the given name as part of the directory name
     *
     * @param baseDir
     * @param prefix
     * @param suffix
     * @return
     * @throws IOException
     */
    public static File newTmpDir(File baseDir,String prefix,String suffix) throws IOException {
        int count = 0;
        while (true) {
            String dirName = prefix + System.currentTimeMillis() + count + suffix;
            File newTmpDir = new File(baseDir, dirName);
            if (!newTmpDir.exists()) {
                if (!newTmpDir.mkdirs()) {
                    throw new IOException(String.format("Couldn't create temporary directory '%s' within '%s' ", dirName, baseDir.getAbsolutePath()));
                }
                return newTmpDir;
            }
            count++;
        }
    }
}
