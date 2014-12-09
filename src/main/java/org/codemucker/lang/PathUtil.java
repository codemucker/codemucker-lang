package org.codemucker.lang;

import java.io.File;
import java.io.IOException;


public class PathUtil {

    /**
     * Return the package name part of the given file path or null if no package name could be determined
     */
	public static String filePathToPackagePathOrNull(String path) {
		path = toForwardSlashes(path);
		if(path.charAt(0) == '/'){  //e.g   /Foo/Bar - > Foo/Bar, Foo/Bar --> Foo/Bar
			path = path.substring(1);
		}
		int last = path.lastIndexOf('/'); 
		if (last == -1 ) { //no package part
			return null;
		}
		String pkg = path.substring(0, last); //e.g  Foo/Bar - > Foo
		pkg = pkg.replace('/', '.');
		if(pkg.length() == 0){
			return null;
		}
		return pkg;
	}

	/**
	 * Return the class name of the given java source or class file as suggested by the file path, or null if the class name could not be determined
	 * 
	 * @param filePathWithExtension
	 * @return
	 */
    public static String filePathToClassNameOrNull(String filePathWithExtension) {
        if (filePathWithExtension.endsWith(".java") || filePathWithExtension.endsWith(".class")) {
            String classPath = stripExtension(filePathWithExtension);
            String className = filePathToClassPath(classPath);
            return className;
        }
        return null;
    }

	private static String filePathToClassPath(String path) {
		path = toForwardSlashes(path);
		if(path.charAt(0) == '/'){
			path = path.substring(1);
		}
		return path.replace('/', '.');
	}
	
	public static String toForwardSlashes(String path) {
        if (path == null) {
            return null;
        }
        return path.replace('\\', '/');
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
