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
package sturesy.test.export;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXB;

import org.junit.Test;

import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;

public class QuestionModelJAxbTest
{

    @Test
    public void testMarshallUnmarshall()
    {

        List<String> a = Arrays.asList(new String[] { "A1", "A", "A3", "A$4" });

        QuestionModel model = new SingleChoiceQuestion("Hier < Frage?", a, 0, 120);

        QuestionSet expectedSet = new QuestionSet();
        expectedSet.addQuestionModel(model);
        expectedSet.addQuestionModel(model);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JAXB.marshal(expectedSet, baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        QuestionSet actualSet = JAXB.unmarshal(bais, QuestionSet.class);

        assertEquals(expectedSet, actualSet);

        try
        {
            baos.close();
            bais.close();
        }
        catch (IOException e)
        {
        }

    }

}
