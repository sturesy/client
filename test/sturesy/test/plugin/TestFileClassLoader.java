/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2014  StuReSy-Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy.test.plugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import sturesy.core.plugin.FileClassLoader;

public class TestFileClassLoader
{

    @Test(expected = IllegalArgumentException.class)
    public void testNullDir()
    {
        new FileClassLoader(null);
    }

    @Test
    public void testFileLoad() throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException,
            IllegalAccessException
    {
        String rootdir = "test/plugin/testmaterial";
        FileClassLoader loader = new FileClassLoader(rootdir);
        Method methodToCall = loadMethodToCall(loader);
        Object result = methodToCall.invoke(loader, "sturesy.test.plugin.testmaterial.FileClassLoaderTestClass",
                Boolean.TRUE);
        assertTrue(result instanceof Class<?>);
        Class<?> resultAsClass = (Class<?>) result;
        assertEquals("sturesy.test.plugin.testmaterial.FileClassLoaderTestClass", resultAsClass.getName());
    }

    @Test(expected = InvocationTargetException.class)
    public void testFileLoadWithInvalidClassName() throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException
    {
        String rootdir = "test/plugin/testmaterial";
        FileClassLoader loader = new FileClassLoader(rootdir);
        Method methodToCall = loadMethodToCall(loader);
        Object result = methodToCall.invoke(loader, "FileClassLoaderTestClass", Boolean.TRUE);
        assertTrue(result instanceof Class<?>);
        Class<?> resultAsClass = (Class<?>) result;
        assertEquals("test.plugin.testmaterial.FileClassLoaderTestClass", resultAsClass.getName());
    }

    private Method loadMethodToCall(FileClassLoader loader)
    {
        Method methodToCall = null;
        Method[] declaredMethods = loader.getClass().getDeclaredMethods();
        for (Method oneMethod : declaredMethods)
        {
            if ("loadClass".equals(oneMethod.getName()) && oneMethod.getParameterTypes().length == 2)
            {
                methodToCall = oneMethod;
            }
        }
        assertNotNull(methodToCall);
        methodToCall.setAccessible(true);
        return methodToCall;
    }
}
