/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2013  StuReSy-Team
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
package sturesy;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestCyclicDependencies
{
    private static JDepend jdepend = null;
    private static Collection<JavaPackage> packages = null;
    private JavaPackage pack1 = null;

    @SuppressWarnings("unchecked")
    @Parameterized.Parameters
    public static Collection<JavaPackage[]> data() throws IOException
    {
        Collection<JavaPackage[]> result = new ArrayList<JavaPackage[]>();
        jdepend = new JDepend();
        jdepend.addDirectory("target/testbuild/sturesy");
        packages = jdepend.analyze();
        for (JavaPackage p : packages)
        {
            result.add(new JavaPackage[] { p });
        }
        return result;
    }

    public TestCyclicDependencies(JavaPackage pack)
    {
        pack1 = pack;
    }

    @Test
    public void cycleTest()
    {
        List<JavaPackage> cycle = new ArrayList<JavaPackage>();
        pack1.collectCycle(cycle);

        assertFalse(pack1.getName() + " failed: \n" + buildUsefulString(cycle), pack1.containsCycle());
    }

    private String buildUsefulString(List<JavaPackage> arg0)
    {
        StringBuffer result = new StringBuffer();
        for (JavaPackage oneString : arg0)
        {
            JavaPackage pack = (JavaPackage) oneString;
            result.append(pack.getName() + "\n");
        }

        return result.toString();
    }
}
