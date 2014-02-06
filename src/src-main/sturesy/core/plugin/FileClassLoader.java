/*
 * Taken from:
 * Tutorials & Code Camps
 * Writing Your Own ClassLoader
 * By MageLang Institute 
 * http://java.sun.com/developer/onlineTraining/Security/Fundamentals/magercises/ClassLoader/help.html
 * Copyright © 1998 MageLang Institute. All Rights Reserved. 
 */
package sturesy.core.plugin;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A Classloader to load Classes not on the java.classpath
 * 
 * @author MageLang Institute
 * @version 1998
 */
public class FileClassLoader extends ClassLoader
{

    private String _rootDir;

    /**
     * Creates a new FileClassLoader with a given root directory
     * 
     * @param rootDir
     */
    public FileClassLoader(String rootDir)
    {
        if (rootDir == null)
            throw new IllegalArgumentException("Null root directory");
        _rootDir = rootDir;
    }

    /**
     * Loads a Class specified by name
     * 
     */
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
    {
        Class<?> c = findLoadedClass(name);
        if (c == null)
        {
            try
            {
                c = findSystemClass(name);
            }
            catch (ClassNotFoundException cnfe)
            {

            }
        }

        if (c == null)
        {
            String filename = name.replace('.', File.separatorChar) + ".class";

            try
            {
                byte data[] = loadClassData(filename);

                c = defineClass(name, data, 0, data.length);
                if (c == null)
                    throw new ClassNotFoundException(name);
            }
            catch (IOException e)
            {
                throw new ClassNotFoundException("Error reading file: " + filename);
            }
        }

        if (resolve)
            resolveClass(c);

        return c;
    }

    private byte[] loadClassData(String filename) throws IOException
    {

        File f = new File(_rootDir, filename);
        int size = (int) f.length();
        byte buff[] = new byte[size];

        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);

        dis.readFully(buff);
        dis.close();
        return buff;
    }
}